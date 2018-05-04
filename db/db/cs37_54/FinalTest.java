package eg.edu.alexu.csd.oop.db.cs37_54;

import java.io.File;
import java.sql.SQLException;

import static org.junit.Assert.fail;

/**
 * Created by M.Sharaf on 26/11/2017.
 */
public class FinalTest {
    public static void main(String[] args) throws SQLException {
        MyDatabase db = new MyDatabase();

        db.createDatabase("sample\\76920.40168135888\\TestDB_Select", true);
        db.createDatabase("sample\\76920.40168135888\\TestDB_Select", true);
        db.executeStructureQuery("CREATE DATABASE testdb_select");
        db.executeStructureQuery("CREATE TABLE table_name13(column_name1 varchar, column_name2 int, column_name3 varchar)");
        db.executeUpdateQuery("INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
        db.executeUpdateQuery("INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value3')");
        db.executeUpdateQuery("INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
        db.executeUpdateQuery("INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");
        db.executeUpdateQuery("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 < 5");
        db.createDatabase("sample\\76920.40168135888\\TestDB_Select", true);
        db.executeStructureQuery("CREATE DATABASE testdb_select");
        db.executeStructureQuery("CREATE TABLE table_name13(column_name1 varchar, column_name2 int,column_name3 varchar)");
        db.executeUpdateQuery("INSERT INTO table_name13(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
        db.executeUpdateQuery("INSERT INTO table_name13(column_NAME1, column_name2, COLUMN_name3) VALUES ('value1', 4, 'value3')");
        db.executeUpdateQuery("INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value2', 'value4', 5)");
        db.executeUpdateQuery("INSERT INTO table_name13(column_name1, COLUMN_NAME3, column_NAME2) VALUES ('value5', 'value6', 6)");

        Object[][] f = db.executeQuery("SELECT column_name1 FROM table_name13 WHERE coluMN_NAME2 < 5");
        System.out.println(f.length);
        System.out.println("*************");
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[0].length; j++) {
                System.out.printf(f[i][j] + "\t");
            }
            System.out.println();
        }

			/* end of select */

    }
}