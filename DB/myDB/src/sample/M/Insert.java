package sample.M;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Insert {
    private WriteInFile writeInFile = new WriteInFile();
    //INSERT INTO table_name VALUES (value1, value2, value3, ...);
    public void insertAtAll(String name, String tableName, String query, String str
            , LinkedList<String> splittedQuery, DocumentBuilder docBuilder
            , DocumentBuilderFactory dbf, Set<Integer> noOfUpdatedRows
            , Map<String, Map<String, DBMap>> db, File xmlFile
            , LinkedList<String> valuesToInsert) throws SQLException {
        int i = 4;
        for (String key : db.get(name).get(tableName).keySet()) {
            str = key;
            String[] splittedKey = key.split("\\s+");
            noOfUpdatedRows.add(db.get(name).get(tableName).get(key).size());
            if (splittedKey[2].equals("INT") && splittedQuery.get(i).charAt(0) == '\'') {
                throw new SQLException(query);
            }
            db.get(name).get(tableName).get(key).add(splittedQuery.get(i));
            i++;
        }
        try {
            docBuilder = dbf.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // DOM tree structure object,Dom usedd to read xml file
        try {
            InputStream inputStream;
            Reader reader = null;
            try {
                inputStream = new FileInputStream(xmlFile);
                try {
                    reader = new InputStreamReader(inputStream, "ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            InputSource is = new InputSource(reader);
            is.setEncoding("ISO-8859-1");
            Document readDocument = docBuilder.parse(is);
            NodeList elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");
            int valueIt = 0;
            for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
                String content = elements.item(elementsIndex).getTextContent();
                content += valuesToInsert.get(elementsIndex);
                content += " ";
                valueIt++;
                elements.item(elementsIndex).setTextContent(content);

                // write from temp memory to a file
                writeInFile.write(readDocument, xmlFile);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String insertIntoColumns(String name, String tableName, String query, String str
            , LinkedList<String> splittedQuery, DocumentBuilder docBuilder
            , DocumentBuilderFactory dbf, Set<Integer> noOfUpdatedRows
            , Map<String, Map<String, DBMap>> db, File xmlFile
            , LinkedList<String> valuesToInsert
            ,LinkedList<String> nameOfColsToInsertIn) throws SQLException {
        int i = 3, j = splittedQuery.indexOf("VALUES") + 1;
        while (i != splittedQuery.indexOf("VALUES")) {
            for (String key : db.get(name).get(tableName).keySet()) {
                String[] splittedKey = key.split("\\s+");
                str = key;
                if (splittedKey[1].equals(splittedQuery.get(i))) {
                    if (splittedKey[2].equals("INT") && splittedQuery.get(j).charAt(0) == '\'') {
                        throw new SQLException(query);
                    }
                    db.get(name).get(tableName).get(key).add(splittedQuery.get(j));
                }
            }
            i++;
            j++;
        }
        try {
            docBuilder = dbf.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        // DOM tree structure object,Dom usedd to read xml file
        try {
            InputStream inputStream;
            Reader reader = null;
            try {
                inputStream = new FileInputStream(xmlFile);
                try {
                    reader = new InputStreamReader(inputStream, "ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            InputSource is = new InputSource(reader);
            is.setEncoding("ISO-8859-1");
            Document readDocument = docBuilder.parse(is);
            NodeList elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");
            int valueIt = 0;
            //Set<Integer> noOfRows = new HashSet<>();

            for (int listIndex = 0; listIndex < nameOfColsToInsertIn.size(); listIndex++) {
                for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
                    if (elements.item(elementsIndex).getAttributes().getNamedItem("column_id").getNodeValue()
                            .equals(nameOfColsToInsertIn.get(listIndex))) {

                        String content = elements.item(elementsIndex).getTextContent();
                        content += valuesToInsert.get(valueIt);
                        content += " ";
                        valueIt++;

                        elements.item(elementsIndex).setTextContent(content);
                        // write from temp memory to a file
                        writeInFile.write(readDocument,xmlFile);
                        break;
                    }
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
