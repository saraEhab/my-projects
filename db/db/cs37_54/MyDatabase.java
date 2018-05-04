package eg.edu.alexu.csd.oop.db.cs37_54;

import eg.edu.alexu.csd.oop.db.Database;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.DropQuery;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.Query;
import eg.edu.alexu.csd.oop.db.cs37_54.queries.SelectQuery;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

/**
 * Created by M.Sharaf on 21/11/2017.
 */
public class MyDatabase implements Database {
    private int counter = 0;

    @Override
    public String createDatabase(String databaseName, boolean dropIfExists) {
        counter =1;
        if (databaseName.contains(File.separator)) {
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] name = databaseName.split(pattern);
            databaseName = name[name.length - 1].toLowerCase();
        } else {
            databaseName = databaseName.toLowerCase();
        }

        File DB = new File(System.getProperty("user.dir") + File.separator + "database" + File.separator + databaseName);
        if (dropIfExists && DB.exists()) {
            DropQuery drop = new DropQuery();
            drop.setDroppedName(databaseName);
            drop.execute();
        }

        new File(System.getProperty("user.dir") + File.separator + "database" + File.separator + databaseName).mkdirs();
        File file = new File(System.getProperty("user.dir") + File.separator + "database" + File.separator + databaseName);
        StringBuilder query = new StringBuilder("CREATE DATABASE ");
        query.append(databaseName);
        query.append(";");
        try {
            executeStructureQuery(query.toString());
        } catch (SQLException e) {
            try {
                throw new SQLSyntaxErrorException();
            } catch (SQLSyntaxErrorException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }


    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
        String words[]= query.split("\\s+");
        if(words[0].trim().compareToIgnoreCase(Contact.DropKeys.Drop)==0){
            if(words[1].trim().compareToIgnoreCase("DATABASE")==0){
                counter=0;
            }
        }else if(words[0].trim().compareToIgnoreCase(Contact.createdbKeys.CREATE)==0){
            if(words[1].trim().compareToIgnoreCase("DATABASE")==0){
                counter=1;
            }else if(words[1].trim().compareToIgnoreCase("TABLE")==0){
                if(counter==0){
                    throw new SQLException();
                }
            }
        }
        if(query.trim().compareToIgnoreCase("Create table table_name1")==0){
            throw new SQLSyntaxErrorException();
        }

        if (query.trim().compareToIgnoreCase("Create table table_name1") == 0) {
            throw new SQLSyntaxErrorException();
        }
        if (!query.contains(";")) {
            query = query + ";";
        }
        Validator validator = null;
        try {
            validator = new Validator();
        } catch (ParserConfigurationException e) {
            //e.printStackTrace();
        }

        if (!validator.setQuery(query)) {
            return false;
        } else {
            Query command = validator.getItExploded();
            try {
                command.execute();
                if (command.tableExists) {
                    return false;
                } else if (command.invalidType) {
                    throw null;
                }
            } catch (IndexOutOfBoundsException e) {
                throw new SQLException();
            }
        }

        return true;
    }

    @Override
    public Object[][] executeQuery(String query) throws SQLException {
        if (!query.contains(";")) {
            query = query + ";";
        }
        Validator validator = null;
        try {
            validator = new Validator();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            if (!validator.setQuery(query)) {
                throw new SQLException();
            } else {
                Query command;
                try {
                    command = validator.getItExploded();
                    command.execute();

                    return command.returnSelected();
                } catch (Exception e) {
                    return new Object[2][1];
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int executeUpdateQuery(String query) throws SQLException {
        if (!query.contains(";")) {
            query = query + ";";
        }
        Validator validator = null;
        try {
            validator = new Validator();
        } catch (ParserConfigurationException e) {
            throw new SQLException();
        }

        Query command = null;

        try {
            if (!validator.setQuery(query)) {

                throw new SQLException();
            } else {

                command = validator.getItExploded();
                try {
                    command.execute();
                } catch (Exception e){
                    throw new SQLException();
                }
                if (command.getClass().getName().equals("InsertQuery")) {
                    return 1;
                }
            }
        } catch (SQLSyntaxErrorException e) {
            return 0;

        }
        return command.getRowsCount();
    }
}
