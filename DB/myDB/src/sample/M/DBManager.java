package sample.M;

import java.sql.SQLException;

public class DBManager {
    private Compilor compilor =new Compilor();
    private Database db =new FinalDBSM();
//    DBManager(){
//        db.createDatabase("belal",false);
//    }
    public void manager(String query){
        query=query.toUpperCase();
        if (compilor.executeStructureQueryValidy(query)){
            try {
                db.executeStructureQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if (compilor.executeUpdateQueryValidation(query)){
            try {
                db.executeUpdateQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if (compilor.executeQueryValidation(query)){
            try {
                db.executeQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            throw null;
        }
    }
}
