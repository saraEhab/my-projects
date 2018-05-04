package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import eg.edu.alexu.csd.oop.db.cs37_54.Contact;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
import java.util.LinkedList;

/**
 * Created by M.Sharaf on 20/11/2017.
 */
public class DeleteQuery extends Query {
    private String deletedFrom;
    private String whereCondition;
    private boolean deleteAll;
    private int rowsCount;
    private int rowsBeforeDelete;

    private String columnName;
    private String opertator;
    private String condition;

    private DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    public DeleteQuery() throws ParserConfigurationException {
        super(Contact.deleteKeys.DELETE);
    }


    public void setDelAll(boolean x) {
        this.deleteAll = x;
    }

    public boolean getDelAll() {
        return this.deleteAll;
    }

    public void setDelFrom(String s) {
        this.deletedFrom = s;
    }

    public String getDelFrom() {
        return this.deletedFrom;
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
            if (whereCondition.contains(operator)) {
                splitCondition = whereCondition.split(operator);
                opertator = operator;
                break;
            }
        }

        columnName = splitCondition[0].trim().toLowerCase();
        condition = splitCondition[1].trim().toLowerCase();
    }

    @Override
    public int getRowsCount() {
        return rowsCount;
    }

    @Override
    public void execute() throws SQLException {
        InputStream inputStream;
        Reader reader = null;
        try {
            inputStream = new FileInputStream(getDBFolder() + File.separator + getCreationDBName() + File.separator + getDelFrom() + ".xml");
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
        Document doc = null;
        try {
            doc = docBuilder.parse(is);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        Node table = doc.getFirstChild();
        rowsBeforeDelete = table.getChildNodes().item(0).getChildNodes().getLength();

        if (getWhere() == null || deleteAll) {
            while (table.hasChildNodes())
                table.removeChild(table.getFirstChild());

            rowsCount = rowsBeforeDelete;
        } else {
            splitCondition();
            LinkedList<Integer> deleteIndex = new LinkedList<>();

            Node delFrom = doc.getElementsByTagName(columnName).item(0);
            NodeList delFromElements = delFrom.getChildNodes();

            if (opertator.equals(Contact.whereKeys.greater)) {
                for (int i = delFromElements.getLength() - 1; i >= 0; i--) {
                    if (delFromElements.item(i).getTextContent().compareTo(condition) > 0) { //TODO check conditions
                        delFrom.removeChild(delFromElements.item(i));
                        deleteIndex.add(i);
                    }
                }
            } else if (opertator.equals(Contact.whereKeys.less)) {
                for (int i = delFromElements.getLength() - 1; i >= 0; i--) {
                    if (delFromElements.item(i).getTextContent().compareTo(condition) < 0) { //TODO check conditions
                        delFrom.removeChild(delFromElements.item(i));
                        deleteIndex.add(i);
                    }
                }
            } else if (opertator.equals(Contact.whereKeys.equal)) {
                for (int i = delFromElements.getLength() - 1; i >= 0; i--) {
                    if (delFromElements.item(i).getTextContent().compareTo(condition) == 0) { //TODO check conditions
                        delFrom.removeChild(delFromElements.item(i));
                        deleteIndex.add(i);
                    }
                }
            }

            rowsCount = rowsBeforeDelete - delFromElements.getLength();

            if (!deleteIndex.isEmpty()) {
                NodeList columns = table.getChildNodes();

                for (int i = 0; i < columns.getLength(); i++) {
                    if (columns.item(i).equals(delFrom)) {
                        continue;
                    }

                    NodeList singleColumn = columns.item(i).getChildNodes();
                    for (int j = 0; j < deleteIndex.size(); j++) {
                        Node oldNode = singleColumn.item(deleteIndex.get(j));
                        columns.item(i).removeChild(oldNode);
                    }

                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(
                    new File(getDBFolder() + File.separator + getCreationDBName() + File.separator + getDelFrom() + ".xml"));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
