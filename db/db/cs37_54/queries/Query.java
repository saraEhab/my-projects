package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import eg.edu.alexu.csd.oop.db.cs37_54.Contact;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by M.Sharaf on 20/11/2017.
 */
public abstract class Query {

    private String mainType;
    private static String creationDBName;
    public boolean tableExists;
    public boolean invalidType;

    public abstract int getRowsCount();

    public Query(String x) {
        this.mainType = x;
    }

    public String getMainType() {
        return this.mainType;
    }

    public void setCreationDBName(String creationDBName) {
        this.creationDBName = creationDBName;
    }

    public String getCreationDBName() {
        return this.creationDBName;
    }

    //TODO secType hould be in some queris only
    private String secType;
    private static Object[][] selected;

    public void setSelected (Object[][] selected){
        this.selected = selected;
    }

    public Object[][] returnSelected (){
        return this.selected;
    }

    public void setsecType(String x) {
        this.secType = x;
    }

    public String getsecType() {
        return this.secType;
    }

    public Document convertstrtodoc(String s) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        builder = factory.newDocumentBuilder();
        Document doc = builder.parse( new InputSource( new StringReader( s ) ) );

        return doc;

    }

    public String getDBFolder (){
        File file = new File(System.getProperty("user.dir") + File.separator +"database");
        if (!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public abstract void execute() throws SQLException;

}
