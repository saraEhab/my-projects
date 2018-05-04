package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.db.cs37_54.ColumnsAndValues;
import eg.edu.alexu.csd.oop.db.cs37_54.Contact;
 
 
/**
 * Created by M.Sharaf on 21/11/2017.
 */
public class UpdateQuery extends Query {
    private String updateTableName;
    private String whereCondition;
    private LinkedList<ColumnsAndValues> updateList = new LinkedList<>();
    private String theOperator;
    private String theConditionColumn;
    private String theConditionValue;
    private LinkedList<Integer> indexToUpdate = new LinkedList<>();
    private boolean indexFlag;
    private int rowsCount;
 
    public UpdateQuery() throws ParserConfigurationException {
        super(Contact.updatetKeys.UPDATE);
    }
 
    public void setUpdateTableName(String t) {
        this.updateTableName = t;
    }
 
    public String getetUpdateTableName() {
        return this.updateTableName;
    }
 
    public void setUpdateMap(LinkedList<ColumnsAndValues> m) {
        this.updateList = m;
    }
 
    public LinkedList<ColumnsAndValues> getUpdateMap() {
        return this.updateList;
    }
 
    public void setWhere(String x) {
        this.whereCondition = x;
    }
 
    public String getWhere() {
        return this.whereCondition;
    }
 
    private void splitCondition() {
        String[] splitCondition = null;
        LinkedList<String> operators = new LinkedList<>();
        operators.add(Contact.whereKeys.greater);
        operators.add(Contact.whereKeys.less);
        operators.add(Contact.whereKeys.equal);
 
        for (String operator : operators) {
            if (getWhere().contains(operator)) {
                splitCondition = getWhere().split(operator);
                theOperator = operator;
                break;
            }
 
        }
        theConditionColumn = splitCondition[0].trim().toLowerCase();
        theConditionValue = splitCondition[1].trim().toLowerCase();
    }
 
    @Override
    public int getRowsCount() {
        return rowsCount;
    }
 
    @Override
    public void execute() throws SQLException {
 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
 
        InputStream inputStream;
        Reader reader = null;
        try {
            inputStream = new FileInputStream(getDBFolder() + File.separator + getCreationDBName() + File.separator + getetUpdateTableName() + ".xml");
            try {
                reader = new InputStreamReader(inputStream, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
              throw new SQLException();
            }
        } catch (Exception e) {
            throw new SQLException();

           /* try {
                throw new SQLException();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }*/
        }
        InputSource is = new InputSource(reader);
        is.setEncoding("ISO-8859-1");
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Node table = doc.getFirstChild();
            NodeList columns = table.getChildNodes();
            if (getWhere() == null) {
                for (ColumnsAndValues x : updateList) { // loop on the map
                    for (int i = 0; i < columns.getLength(); i++) {// loop on the columns
                        Node c = columns.item(i);
                        if (c.getNodeType() == Node.ELEMENT_NODE) {
                            Element column = (Element) c;
                            String name = column.getTagName();
                            if (name.compareToIgnoreCase(x.getColumn()) == 0) { // get the cloumn to update
                                NodeList elements = column.getElementsByTagName("Element");
                                for (int j = 0; j < elements.getLength(); j++) {// update all the elements of the columns
                                    Node e = elements.item(j);
                                    if (e.getNodeType() == Node.ELEMENT_NODE) {
                                        Element element = (Element) e;
                                        element.setTextContent(x.getValue());
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                rowsCount = columns.item(0).getChildNodes().getLength();
            } else {
            	int numberOfElementsUpdated = 0;
                splitCondition();
                // getting the index to update
                for (int i = 0; i < columns.getLength(); i++) { // loop on columns
                    Node c = columns.item(i);
                    if (c.getNodeType() == Node.ELEMENT_NODE) {
                        Element column = (Element) c;
                        String name = column.getTagName();
                        NamedNodeMap columnAttr = column.getAttributes();
                        Node typeAttr = columnAttr.getNamedItem("type");
                        if (name.compareToIgnoreCase(theConditionColumn) == 0) { // the condition column
                            NodeList elements = column.getElementsByTagName("Element");
                            for (int j = 0; j < elements.getLength(); j++) { // loop on elements
                                Node e = elements.item(j);
                                if (e.getNodeType() == Node.ELEMENT_NODE) {
                                    Element element = (Element) e;
                                    if (theOperator.equals(Contact.whereKeys.equal)) {
                                        if (element.getTextContent().equals(theConditionValue)) {
                                            indexToUpdate.add(j);
                                            indexFlag = true;
                                        }
                                    } else if (theOperator.equals(Contact.whereKeys.greater)) {
                                        if (typeAttr.getTextContent().equals("int")) { // check the type of data
                                            int condValue = Integer.parseInt(theConditionValue);
                                            int elemValue = Integer.parseInt(element.getTextContent());
                                            if (elemValue > condValue) { // greater
                                                indexToUpdate.add(j);
                                                indexFlag = true;
                                            }
                                        } else {
                                            //throw new Exception("type error !!");
                                        }
                                    } else if (theOperator.equals(Contact.whereKeys.less)) {
                                        if (typeAttr.getTextContent().equals("int")) { // check the type of data
                                            int condValue = Integer.parseInt(theConditionValue);
                                            int elemValue = Integer.parseInt(element.getTextContent());
                                            if (elemValue < condValue) { //  less
                                                indexToUpdate.add(j);
                                                indexFlag = true;
                                            }
                                        } else {
                                           // throw new Exception("type error !!");
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
                // update
                int numOfUpdatedColumns = 0;
                if (indexFlag) {
                    for (ColumnsAndValues x : updateList) { // loop on map
                        for (int i = 0; i < columns.getLength(); i++) { // loop on columns
                            Node c = columns.item(i);
                            if (c.getNodeType() == Node.ELEMENT_NODE) {
                                Element column = (Element) c;
                                String name = column.getTagName();
                                if (name.compareToIgnoreCase(x.getColumn()) == 0) {
                                	numOfUpdatedColumns++; // increases with every updated column
                                    for (int d : indexToUpdate) {
                                        Node e = column.getElementsByTagName("Element").item(d);
                                        if (e.getNodeType() == Node.ELEMENT_NODE) {
                                            Element element = (Element) e;
                                            element.setTextContent(x.getValue());
                                            numberOfElementsUpdated++;
                                            // increases with every update
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                rowsCount = numberOfElementsUpdated/numOfUpdatedColumns;
            }
 
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                    new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getetUpdateTableName() + ".xml"));
            transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
        } catch (IOException e) {
            throw  new SQLException();
        } catch (ParserConfigurationException e) {
            throw  new SQLException();

        } catch (SAXException e) {
            throw  new SQLException();

        } catch (TransformerConfigurationException e) {
            throw  new SQLException();

        } catch (TransformerException e) {
            throw  new SQLException();

        } catch (Exception e1) {

        }
    }
}
