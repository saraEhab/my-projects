package sample.M;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Compilor {

    // Database myDatabase = new DatabaseImp();

    private String[] keyWords = new String[]{"CREATE", "DATABASE", "DROP", "TABLE", "INSERT", "INTO",
            "VALUES", "SELECT", "FROM", "*", "WHERE", "DELETE",
            "UPDATE", "SET"};

    public boolean executeStructureQueryValidy(String synatax) {
        synatax = synatax.toUpperCase();
        //CREATE DATABASE databasename;
        Pattern createDBPattern = Pattern.compile("\\s*CREATE\\s+DATABASE\\s+[a-zA-Z0-9. _+*&^%$#@!]+\\s*");

        //DROP DATABASE databasename;
        Pattern dropDBpattern = Pattern.compile("\\s*DROP\\s+DATABASE\\s+[a-zA-Z0-9. _+*&^%$#@!]+\\s*");

        //DROP TABLE table_name;
        Pattern dropTablePattern = Pattern.compile("\\s*DROP\\s+TABLE\\s+\\w+\\s*");

        //CREATE TABLE table_name (column1 datatype,column2 datatype,column3 datatype,....);
        Pattern createTablePattern = Pattern.compile("\\s*CREATE\\s+TABLE\\s+\\w+\\s*\\((\\s*\\w+\\s+(INT|VARCHAR)\\s*,)*\\s*\\w+\\s+(INT|VARCHAR)\\s*\\)\\s*");

        Matcher matcher = createDBPattern.matcher(synatax);

        if (matcher.matches()) {
            return true;
        }

        matcher = dropDBpattern.matcher(synatax);

        if (matcher.matches()) {
            return true;
        }

        matcher = dropTablePattern.matcher(synatax);

        if (matcher.matches()) {
            return true;
        }

        matcher = createTablePattern.matcher(synatax);

        return matcher.matches();


    }

    public boolean executeQueryValidation(String syntax) {
        syntax = syntax.toUpperCase();
        //SELECT * FROM table_name;

        //SELECT column1, column2, ... FROM table_name WHERE condition;
        Pattern selectPattern = Pattern.compile("\\s*SELECT\\s+(((\\w+\\s*,)*\\s*\\w+)|\\*)\\s+FROM\\s+\\w+\\s*(\\s+WHERE\\s+\\w+\\s*([=<>])\\s*(\\w+\\s*|('[a-zA-Z0-9-.*/&^%$#@! ]'\\s*))){0,1}");
        Matcher matcher = selectPattern.matcher(syntax);

        return matcher.matches();
    }

    public boolean executeUpdateQueryValidation(String syntax) {
        syntax = syntax.toUpperCase();
        //INSERT INTO table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...);
        //[!||&&]()*
        Pattern insertPattern = Pattern.compile("\\s*INSERT\\s+INTO\\s+\\w+\\s*(\\((\\s*\\w+\\s*,)*\\s*\\w+\\s*\\))*\\s+VALUES\\s+\\((\\s*('[a-zA-Z0-9. _+*&^%$#@!]+'|\\w+)\\s*,)*\\s*('[a-zA-Z0-9. _+*&^%$#@!]+'|\\w+)\\s*\\)\\s*");

        //DELETE FROM table_name WHERE condition;
        Pattern deletePattern = Pattern.compile("\\s*DELETE\\s+FROM\\s+\\w+\\s+WHERE\\s+\\w+\\s*([=><])\\s*(('[a-zA-Z0-9. _+*&^%$#@!]+')|(\\w+))\\s*");

        //DELETE * FROM table_name;
        Pattern deleteAllPattern = Pattern.compile("\\s*DELETE\\s+(\\*\\s+){0,1}FROM\\s+\\w+\\s*");

        //UPDATE table_name SET column1 = value1, column2 = value2, ... WHERE condition;
        Pattern updatePattern = Pattern.compile("\\s*UPDATE\\s+\\w+\\s+SET\\s+(\\s*\\w+\\s*=\\s*(('[a-zA-Z0-9. _+*&^%$#@!]+')|(\\w+))\\s*,)*\\s*\\w+\\s*=\\s*(('[a-zA-Z0-9. _+*&^%$#@!]+')|(\\w+))\\s*(\\s+WHERE\\s+\\w+\\s*([=><])\\s*(('[a-zA-Z0-9. _+*&^%$#@!]+')|(\\w+))\\s*){0,1}");

        Matcher matcher = insertPattern.matcher(syntax);
        if (matcher.matches()) {
            return true;
        }

        matcher = deletePattern.matcher(syntax);
        if (matcher.matches()) {
            return true;
        }

        matcher = deleteAllPattern.matcher(syntax);
        if (matcher.matches()) {
            return true;
        }

        matcher = updatePattern.matcher(syntax);

        return matcher.matches();
    }

    private boolean tableNameValidation(String word, LinkedList<String> tableNames) {
        for (String s : keyWords) {
            if (word.toUpperCase().equals(s)) {
                return false;
            }
        }

        if (!((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') || (word.charAt(0) >= 'A' && word.charAt(0) <= 'Z'))) {
            return false;
        }

        Pattern tableNameStructure = Pattern.compile("\\w+");
        Matcher matcher = tableNameStructure.matcher(word);

        if (!matcher.matches()) {
            return false;
        }

        for (String s : tableNames) {
            if (word.toUpperCase().equals(s.toUpperCase())) {
                return false;
            }
        }

        return true;
    }

//    public void findMethod(String syntax) {
//        Object[][] table = new Object[0][];
//        if (executeStructureQueryValidy(syntax)) {
//            try {
//                myDatabase.executeStructureQuery(syntax);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        } else if (executeQueryValidation(syntax)) {
//            try {
//              table = myDatabase.executeQuery(syntax);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
////            for (){
////                for (){
////
////                }
////            }
//        } else if (executeUpdateQueryValidation(syntax)) {
//            try {
//                myDatabase.executeUpdateQuery(syntax);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    public static void main(String[] args) {
//        Compilor o =new Compilor();
//        System.out.println(o.executeStructureQueryValidy("CREATE TABLE Persons ( PersonID int, LastName varchar, FirstName varchar );"));
////        LinkedList<String> s = new LinkedList();
////        s.add("belal");
////        s.add("sara");
////        s.add("Adel");
////        s.add("EHAB");
////        System.out.println(o.tableNameValidation("sara",s));
//        Scanner input = new Scanner(System.in);
//        System.out.println("sara");
////        String str = input.next();
////        if (str.equals("belal")){
////            System.out.println("sara");
////        }
////        bb.creatingScemaFile();
//    }

}
