package eg.edu.alexu.csd.oop.db.cs37_54;

public class ColumnsAndValues {


    private String column, value;

    public ColumnsAndValues() {

    }

    public ColumnsAndValues(String column, String value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}



