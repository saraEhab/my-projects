package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import java.io.*;
import java.sql.SQLException;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.db.cs37_54.ColumnsAndValues;
import eg.edu.alexu.csd.oop.db.cs37_54.Contact;


/**
 * Created by M.Sharaf on 21/11/2017.
 */
public class InsertQuery extends Query {
    private String insertionTableName;
    private LinkedList<ColumnsAndValues> insertionList = new LinkedList<>();
    private int rowsCount;
    private int rowsBeforeInsert;

    public InsertQuery() throws ParserConfigurationException {
        super(Contact.insertKeys.INSERT);
    }

    public void setInsertTableName(String t) {
        this.insertionTableName = t;
    }

    public String getInsertTableName() {
        return this.insertionTableName;
    }

    public void setInsertionMap(LinkedList<ColumnsAndValues> m) {
        this.insertionList = m;
    }

    public LinkedList<ColumnsAndValues> getInsertionMap() {
        return this.insertionList;
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public void execute() throws SQLException {
        LinkedList<Integer> visitedNodes = new LinkedList<>();
        InputStream inputStream;
        Reader reader = null;
        try {
            inputStream = new FileInputStream(getDBFolder() + File.separator + getCreationDBName() + File.separator + getInsertTableName() + ".xml");
            try {
                reader = new InputStreamReader(inputStream, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new SQLException();
        }
        InputSource is = new InputSource(reader);
        is.setEncoding("ISO-8859-1");


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {

            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            Node table = doc.getFirstChild();
            NodeList columns = table.getChildNodes();
            rowsBeforeInsert = columns.item(0).getChildNodes().getLength();

            if (insertionList.get(0).getColumn() == null) {
                if (insertionList.size() == columns.getLength()) {
                    for (int i = 0; i < columns.getLength(); i++) {
                        Element element = doc.createElement("Element");
                        element.setTextContent(insertionList.get(i).getValue());
                        columns.item(i).appendChild(element);
                    }
                } else {
                    throw new SQLException();
                }
            } else {

                for (ColumnsAndValues x : insertionList) {
                    for (int i = 0; i < columns.getLength(); i++) {
                        Node c = columns.item(i);
                        if (c.getNodeType() == Node.ELEMENT_NODE) {
                            Element column = (Element) c;
                            String name = column.getTagName();
                            if (name.compareToIgnoreCase(x.getColumn()) == 0) {
                                visitedNodes.add(i);
                                Element element = doc.createElement("Element");
                                element.setTextContent(x.getValue());
                                column.appendChild(element);
                            }

                        }
                    }
                }

                for (int i = 0; i < columns.getLength(); i++) {
                    if (!visitedNodes.contains(i)) {
                        Node c = columns.item(i);
                        if (c.getNodeType() == Node.ELEMENT_NODE) {

                            Element column = (Element) c;
                            String name = column.getAttribute("name");

                            visitedNodes.add(i);
                            Element element = doc.createElement("Element");
                            element.setTextContent("");
                            column.appendChild(element);
                        }
                    }
                }
            }
            rowsCount = columns.item(0).getChildNodes().getLength() - rowsBeforeInsert;

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = null;
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                    new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getInsertTableName() + ".xml"));
            transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);


        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
