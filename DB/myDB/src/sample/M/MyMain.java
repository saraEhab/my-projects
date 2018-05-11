package sample.M;



import java.util.Scanner;

public class MyMain {
    public static void main(String[] args) {
        DBManager myManager = new  DBManager();
        Database db = new FinalDBSM();
        while (true){
            Scanner input = new Scanner(System.in);
            String query = input.nextLine();
            myManager.manager(query);
        }
    }
}
