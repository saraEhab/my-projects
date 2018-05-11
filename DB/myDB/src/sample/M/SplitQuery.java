package sample.M;

import java.util.LinkedList;

public class SplitQuery {
    public LinkedList<String> split(String query) {
        LinkedList<String> splittedQuery = new LinkedList<>();
        for (int k = 0; k < query.length(); k++) {
            if (query.charAt(k) == '(' || query.charAt(k) == ',' || query.charAt(k) == ')' || query.charAt(k) == ';') {
                query = query.substring(0, k) + " " + query.substring(k + 1, query.length());
            } else if (query.charAt(k) == '=' || query.charAt(k) == '<' || query.charAt(k) == '>') {
                query = query.substring(0, k) + " " + query.charAt(k) + " " + query.substring(k + 1, query.length());
                k = k + 2;
            }
        }
        query = query.toUpperCase();
        String[] tmpSplit = query.split("\\s+");
        for (int i = 0; i < tmpSplit.length; i++) {
            if (tmpSplit[i].charAt(0) == '\'') {
                if (tmpSplit[i].charAt(tmpSplit[i].length() - 1) == '\'') {
                    splittedQuery.add(tmpSplit[i].substring(1, tmpSplit[i].length() - 1));
                } else {
                    String tmp = "";
                    tmp += tmpSplit[i] + " ";
                    for (int j = i + 1; j < tmpSplit.length; j++) {
                        if (tmpSplit[j].charAt(tmpSplit[j].length() - 1) == '\'') {
                            tmp += tmpSplit[j];
                            splittedQuery.add(tmp.substring(1, tmp.length() - 1));
                            i = j;
                            break;
                        } else {
                            tmp += tmpSplit[j];
                        }
                    }
                }
            } else {
                splittedQuery.add(tmpSplit[i]);

            }
        }

        return splittedQuery;
    }

//
////    INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country) VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');
    public static void main(String[] args) {
        SplitQuery o = new SplitQuery();
        for (String s : o.split("INSERT INTO Persons (personID, personID, firstName, firstName, lastName, lastName) VALUES ('31', '18', '7anan', 'youmna', '5teb', 'dawy');")){
            System.out.println(s);
        }
//        System.out.println("************************************");
//        ////SELECT CustomerName, City FROM Customers;
//        for (String s : o.split("SELECT CustomerName, City FROM Customers;")){
//            System.out.println(s);
//        }
//        System.out.println("**************************************");
//        for (String s : o.split("SELECT * FROM Customers;")){
//            System.out.println(s);
//        }
//        System.out.println("**************************************");
//        for (String s : o.split("DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';")){
//            System.out.println(s);
//        }
//        System.out.println("**************************************");
//        for (String s : o.split("DELETE FROM Customers WHERE CustomerName='Alfreds Futterkiste';")){
//            System.out.println(s);
//        }
//        System.out.println("**************************************");
//        for (String s : o.split("DELETE * FROM table_name;")){
//            System.out.println(s);
//        }
    }

}
