package eg.edu.alexu.csd.oop.db.cs37_54;

import eg.edu.alexu.csd.oop.db.cs37_54.queries.*;

import javax.xml.parsers.ParserConfigurationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by M.Sharaf on 21/11/2017.
 */
public class testXmlCreation {

    public static void main(String[] args) {
        CreateQuery createQuery = null;
        DeleteQuery deleteQuery = null;
        InsertQuery insertQuery = null;
        SelectQuery selectQuery = null;
        DropQuery dropQuery = null;
        try {
            createQuery = new CreateQuery();
            deleteQuery = new DeleteQuery();
            insertQuery = new InsertQuery();
            selectQuery = new SelectQuery();
            dropQuery = new DropQuery();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }


        //testing DB creation
        createQuery.setCreationDBName("year2018");
        try {
            createQuery.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //testing table creation (*DB folder must be existed)

        ColumnsAndValues node = null;
        ColumnsAndValues node1 = null;
        ColumnsAndValues node2 = null;
        ColumnsAndValues node3 = null;
        ColumnsAndValues node4 = null;


        LinkedList<ColumnsAndValues> list = new LinkedList<>();
//        for (int i = 0; i < 7; i++) {
//            String testing = "_" + i;
//            node = new ColumnsAndValues("name", "varchar" + testing);
//            node1 = new ColumnsAndValues("id", String.valueOf(i));
//            node2 = new ColumnsAndValues("phone", "varchar" + testing);
//            node3 = new ColumnsAndValues("title", "varchar" + testing);
//            node4 = new ColumnsAndValues("address", "varchar" + testing);
//            list.add(node);
//            list.add(node1);
//            list.add(node2);
//            list.add(node3);
//            list.add(node4);
//        }

//        node = new ColumnsAndValues("name", "varchar");
//        node1 = new ColumnsAndValues("id", "int");
//        node2 = new ColumnsAndValues("phone", "varchar");
//        node3 = new ColumnsAndValues("title", "varchar");
//        node4 = new ColumnsAndValues("address", "varchar");
//        list.add(node);
//        list.add(node1);
//        list.add(node2);
//        list.add(node3);
//        list.add(node4);


        createQuery.setCreationTableName("Class 1");
        createQuery.setCreateMap(list);
        //createQuery.execute();



        insertQuery.setInsertionMap(list);
        insertQuery.setInsertTableName("Class 1");
        //insertQuery.execute();

        deleteQuery.setCreationDBName("year2018");
        deleteQuery.setDelFrom("Class 1");
        deleteQuery.setWhere("id > 5");
        //deleteQuery.execute();

        ArrayList<String> array = new ArrayList<>();
        array.add("name");
        array.add("title");
        //array.add("id");

        selectQuery.setSelectionTableName("Class 1");
        selectQuery.setSelectionArray(array);
        selectQuery.setWhere("name > varchar_3");
//        Object[][] selected = selectQuery.returnedSelection();
//
//        for (int i = 0; i < selected.length; i++) {
//            for (int j = 0; j < selected[0].length; j++) {
//                System.out.printf(selected[i][j] + " ");
//            }
//            System.out.println();
//        }

        dropQuery.setDroppedName(dropQuery.getCreationDBName());
        //dropQuery.execute();

        //try to comment the test above and
        //create a validator obj
        //pass query (create)
        //return the createObj and call .execute
        //trust me xD

    }

}
