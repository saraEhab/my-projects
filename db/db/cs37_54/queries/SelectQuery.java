package eg.edu.alexu.csd.oop.db.cs37_54.queries;

import com.sun.security.auth.NTDomainPrincipal;
import eg.edu.alexu.csd.oop.db.cs37_54.Contact;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by M.Sharaf on 21/11/2017.
 */
public class SelectQuery extends Query {

    private String SelectionTableName;
    private ArrayList<String> selectionArray = new ArrayList<>();
    private String whereCondition;
    private boolean selectAll;
    private String columnName;
    private String opertator;
    private String condition;
    private Object[][] listOfSelected;
    private LinkedList<Integer> listIndex = new LinkedList<>();
    private String selectionColumnType;

    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

    public SelectQuery() throws ParserConfigurationException {
        super(Contact.selectKeys.SELECT);
    }

    public void setSelectionTableName(String t) {
        this.SelectionTableName = t;
    }

    public String getSelectionTableName() {
        return this.SelectionTableName;
    }

    public void setSelectionArray(ArrayList<String> m) {
        this.selectionArray = m;
    }

    public ArrayList<String> getselectionArray() {
        return this.selectionArray;
    }

    public void setWhere(String x) {
        this.whereCondition = x;
    }


    public void setSelectAll(boolean x) {
        this.selectAll = x;
    }

    public boolean getSelectAll() {
        return this.selectAll;
    }

    public String getWhere() {
        return this.whereCondition;
    }

    //TODO refactor
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
        return 9;
    }

    @Override
    public void execute() throws SQLException {
        Document doc = null;

        InputStream inputStream;
        Reader reader = null;
        try {
            inputStream = new FileInputStream(getDBFolder() + File.separator + getCreationDBName() + File.separator + getSelectionTableName() + ".xml");
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
        try {
            doc = dBuilder.parse(is);
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        Node table = doc.getFirstChild();

        String columnType = null;

        if (getWhere() == null) {

            NodeList columns = table.getChildNodes();
            //TODO check the condition
            if (selectionArray.isEmpty() || selectAll) { // SELECT * FROM Customers; (if not) SELECT column1, column2, ... FROM table_name;
                for (int i = 0; i < columns.getLength(); i++) {
                    selectionArray.add(columns.item(i).getNodeName());
                }
            }

            int rowsSize = columns.item(0).getChildNodes().getLength();
            int columnSize = selectionArray.size();
            listOfSelected = new Object[rowsSize][columnSize];


            for (int j = 0; j < columnSize; j++) {
                Node singleColumn = doc.getElementsByTagName(selectionArray.get(j)).item(0);
                NodeList singleColumnElements = singleColumn.getChildNodes();
                columnType = singleColumn.getAttributes().getNamedItem("type").toString();
                columnType = columnType.substring(6, columnType.length() - 1);
                for (int i = 0; i < rowsSize; i++) {
                    listOfSelected[i][j] = singleColumnElements.item(i).getTextContent();
                    if (columnType.equals("int")) {
                        listOfSelected[i][j] = Integer.valueOf(singleColumnElements.item(i).getTextContent());
                    } else { // varchar
                    }
                }
            }
        } else {
            splitCondition();
            Node selFrom = doc.getElementsByTagName(columnName).item(0);
            NodeList selFromElements = selFrom.getChildNodes();
            NodeList columns = table.getChildNodes();

            if (selectionArray.isEmpty() || selectAll) {
                for (int i = 0; i < columns.getLength(); i++) {
                    selectionArray.add(columns.item(i).getNodeName());
                }
            }

            int rowsSize = columns.item(0).getChildNodes().getLength();
            int columnSize = selectionArray.size();
            listOfSelected = new Object[rowsSize][columnSize];

            columnType = selFrom.getAttributes().getNamedItem("type").toString();
            columnType = columnType.substring(6, columnType.length() - 1);

            for (int j = 0; j < selFromElements.getLength(); j++) {

                if (columnType.equals("int")) {

                    if (opertator.equals(Contact.whereKeys.greater)) {
                        if (Integer.valueOf(selFromElements.item(j).getTextContent()) > Integer.valueOf(condition)) {
                            setConditionalCell(doc, j);
                        }
                    } else if (opertator.equals(Contact.whereKeys.less)) {
                        if (Integer.valueOf(selFromElements.item(j).getTextContent()) < Integer.valueOf(condition)) {
                            setConditionalCell(doc, j);
                        }
                    } else if (opertator.equals(Contact.whereKeys.equal)) {
                        if (Integer.valueOf(selFromElements.item(j).getTextContent()) == Integer.valueOf(condition)) {
                            setConditionalCell(doc, j);
                        }
                    }
                } else {
                    if (opertator.equals(Contact.whereKeys.greater)) {
                        if (selFromElements.item(j).getTextContent().compareTo(condition) > 0) {
                            setConditionalCell(doc, j);
                        }
                    } else if (opertator.equals(Contact.whereKeys.less)) {
                        if (selFromElements.item(j).getTextContent().compareTo(condition) < 0) {
                            setConditionalCell(doc, j);
                        }
                    } else if (opertator.equals(Contact.whereKeys.equal)) {
                        if (selFromElements.item(j).getTextContent().compareTo(condition) == 0) {
                            setConditionalCell(doc, j);
                        }
                    }
                }
            }
            cleanList();
        }
        super.setSelected(listOfSelected);
    }

    private void setConditionalCell(Document doc, int j) {
        String columnType = null;
        listIndex.add(j);
        for (int k = 0; k < selectionArray.size(); k++) {
            Node node = doc.getElementsByTagName(selectionArray.get(k)).item(0);
            columnType = node.getAttributes().getNamedItem("type").toString();
            columnType = columnType.substring(6, columnType.length() - 1);
            NodeList column = node.getChildNodes();
            if (columnType.equals("int")) {
                listOfSelected[j][k] = Integer.valueOf(column.item(j).getTextContent());
            } else { // varchar
                listOfSelected[j][k] = column.item(j).getTextContent();
            }
        }
    }

    private void cleanList() {
        int rows = listIndex.size();
        int columns = listOfSelected[0].length;

        Object[][] temp = new Object[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                temp[i][j] = listOfSelected[listIndex.get(i)][j];
            }
        }

        listOfSelected = temp;

    }

}
