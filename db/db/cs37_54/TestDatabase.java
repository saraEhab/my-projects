package eg.edu.alexu.csd.oop.db.cs37_54;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

public class TestDatabase {

    public static void main(String[] args) throws ParserConfigurationException {
        Validator v = new Validator();
        //	boolean x = v.setQuery(" SELECT CustomerName, City  FROM Customers; ");
        //boolean y = v.setQuery("");


        boolean y = false;
		try {
			y = v.setQuery("CREATE TABLE table_name4(column_name1 varchar, column_name2 int, column_name3 varchar);");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(v.getItExploded().getClass().getSimpleName());
        //boolean z = v.setQuery("INSERT  INTO  table_name  (column1, column2,    column3, ...)  VALUES  (value1, value2 , value3, ...);  ");
        //boolean x = v.setQuery("create table tablename ((anjekfb  ekjbfwe,andh akjedh,alidh d);");
//		boolean x = v.setQuery("create table tablename (anjekfb ekjbfwe,andh akjedh,alidh d)");
//	boolean x = v.setQuery("create table tablename (anjekfb ekjbfwe,andhakjedh,alidh d);");
//			boolean x = v.setQuery("create tabletablename (anjekfb ekjbfwe,andh akjedh,alidh d);");
//		boolean x = v.setQuery("crate table tablename (anjekfb ekjbfwe,andh akjedh,alidh d);");
//		boolean x = v.setQuery("create  tablname (anjekfb ekjbfwe,andh akjedh,alidh d);");

//			boolean x = v.setQuery("create table ,mx d ();");
//	boolean x = v.setQuery("create\ttable\t    \ttablename\t(anjekfb ekjbfwe,andh\talidh);");
//			boolean x = v.setQuery("create  table n (anjekfb ekjbfwe,andh akjedh,alidh d); l;");
//			boolean x = v.setQuery("create table  o (anjekfb ekjbfwe,andh akjedh,alidh d); create\ttable m (anjekfb\tekjbfwe,andh akjedh,alidh d);");	


        System.out.println(y);

//			System.out.println(x +"and its order is :"+v.getOrder() + "  and the name is \""
//					+v.getcreationTableName()+ "\" and colunmns names and types:");
//			HashMap<String, String> h = v.getCreateMap();
//			for(Entry<String, String> e : h.entrySet()){
//				System.out.println(e.getKey()+"  /  "+e.getValue());
//			}
    }

}
