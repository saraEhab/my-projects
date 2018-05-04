package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import eg.edu.alexu.csd.oop.db.cs37_54.ColumnsAndValues;
import eg.edu.alexu.csd.oop.db.cs37_54.Contact;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by M.Sharaf on 20/11/2017.
 */

public class CreateQuery extends Query {

    private LinkedList<ColumnsAndValues> createList = new LinkedList<>();
    private String creationTableName;

    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

    public CreateQuery() throws ParserConfigurationException {
        super(Contact.createKeys.CREATE);
    }

    public void setCreateMap(LinkedList<ColumnsAndValues> m) {
        this.createList = m;
    }

    public LinkedList<ColumnsAndValues> getCreateMap() {
        return this.createList;
    }

    public void setCreationTableName(String table) {
        this.creationTableName = table;
    }

    public String getCreationTableName() {
        return this.creationTableName;
    }

    @Override
    public int getRowsCount() {
        return 9;
    }

    @Override
    public void execute() throws SQLException {
        if (super.getCreationDBName() == null || super.getCreationDBName().length() == 0){
            super.invalidType = true;
            return;
        }

        if (createList.isEmpty()) {

            if (super.getsecType().equals(Contact.createKeys.TABLE)){
                throw new SQLException();
            }

            //create db folder
            new File(getDBFolder() + File.separator + getCreationDBName()).mkdirs();
        } else {
            //create tables

            File file = new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getCreationTableName() + ".xml");
            if (file.exists()){
                super.tableExists = true;
                return;
            }

            Document doc = dBuilder.newDocument();

            Element table = doc.createElement("Table");
            Attr tableName = doc.createAttribute("name");
            tableName.setValue(getCreationTableName());
            table.setAttributeNode(tableName);

            doc.appendChild(table);

            for (ColumnsAndValues cell : createList) {

                Element column = doc.createElement(cell.getColumn());

                Attr columnType = doc.createAttribute("type");
                columnType.setValue(cell.getValue().trim());

                if (cell.getValue().trim().equals("int") || cell.getValue().trim().equals("varchar")) {
                    column.setAttributeNode(columnType);
                } else {
                    super.invalidType = true;
                    return;
                }

                table.appendChild(column);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = null;
                DOMSource source = new DOMSource(doc);

                StreamResult result = new StreamResult(
                        new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getCreationTableName() + ".xml"));
                try {
                    transformer = transformerFactory.newTransformer();
                    transformer.transform(source, result);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }

                BufferedReader br = null;
                String line;
                StringBuilder sb = new StringBuilder();
                try {
                    br = new BufferedReader(new FileReader(
                            new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getCreationTableName() + ".xml")));


                    while((line=br.readLine())!= null){
                        sb.append(line.trim());

                        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
                        dbfac.newDocumentBuilder();
                        sb.replace(sb.indexOf("UTF-8"), sb.indexOf("UTF-8") + 5, "ISO-8859-1");
                        Document convertedDoc = this.convertstrtodoc(sb.toString());
                        convertedDoc.setXmlStandalone(true);
                        DOMSource docSource = new DOMSource(convertedDoc);
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        StreamResult s = new StreamResult(
                                new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getCreationTableName() + ".xml"));
                        tr.transform(docSource, s);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TransformerConfigurationException e) {
                    e.printStackTrace();
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (br != null) br.close();
                } catch (IOException io) {
                    //log exception here
                }

            }
        }
    }
}
