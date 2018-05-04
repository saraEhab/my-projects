package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.regex.Pattern;

import eg.edu.alexu.csd.oop.db.cs37_54.Contact;

/**
 * Created by M.Sharaf on 21/11/2017.
 */
public class DropQuery extends Query {
    private String DroppedName;
    private String DroppedType;
    private String secType;
    File f;
    String[] directories;
    boolean databaseDeleted = false;
    boolean deleted = false;

    public void setsecType(String x) {
        this.secType = x;
    }

    public String getsecType() {
        return this.secType;
    }

    public DropQuery() {
        super(Contact.DropKeys.Drop);
        f = new File(getDBFolder());
        directories = f.list();
    }

    public void setDroppedName(String string) {
        this.DroppedName = string;
    }

    public String getDroppedName() {
        return this.DroppedName;
    }

    @Override
    public int getRowsCount() {
        return 9;
    }

    @Override
    public void execute(){

        if (getDroppedName().contains(File.separator)){
            String pattern = Pattern.quote(System.getProperty("file.separator"));
            String[] splittedFileName = getDroppedName().split(pattern);
            setDroppedName(splittedFileName[splittedFileName.length - 1]);
        }

        for (String x : directories) {
            if (x.equals(getDroppedName())) {
                File index = new File(getDBFolder() + File.separator + x);
                String[] entries = index.list();
                for (String s : entries) {
                    File currentFile = new File(index.getPath(), s);
                    currentFile.delete();
                }
                index.delete();
                databaseDeleted = true;
                deleted = true;
                super.setCreationDBName(null);
            }
        }

        if (!databaseDeleted) {
            File table = new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getDroppedName() + ".xml");
            System.out.println("table file" + table.getPath());
            if (table.exists()) {
                table.delete();
                deleted = true;
            }
        }

        if (!deleted){
            super.invalidType = true;
        }

    }
}
