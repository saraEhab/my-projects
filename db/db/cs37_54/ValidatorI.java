package eg.edu.alexu.csd.oop.db.cs37_54;

import eg.edu.alexu.csd.oop.db.cs37_54.queries.Query;

import java.sql.SQLException;

public interface ValidatorI {
    /**
     * give it the query ..
     *
     * @param fullQuery
     * @return true if valid , else false
     * @throws Exception 
     */
    public boolean setQuery(String fullQuery) throws SQLException;

    /**
     * Query is abstract so you can call the get main type from it and
     *
     * @return
     */
    public Query getItExploded();
}