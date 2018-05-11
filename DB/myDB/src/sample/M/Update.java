package sample.M;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Update {
    private Split mysplit = new Split();
    private WriteInFile writeInFile = new WriteInFile();

    public void conditionalUpdate(String name, String tableName, String query
            , LinkedList<String> splittedQuery
            , Set<Integer> noOfUpdatedRows
            , Map<String, Map<String, DBMap>> db, File xmlFile) throws SQLException {
        String[] columnsToSet = splittedQuery.subList(splittedQuery.indexOf("SET") + 1
                , splittedQuery.indexOf("WHERE")).toArray(new String[0]);
        String[] condition = splittedQuery.subList(splittedQuery.size() - 3, splittedQuery.size()).toArray(new String[0]);
        String selectedKey = null;
        for (String key : db.get(name).get(tableName).keySet()) {
            if (key.contains(condition[0])) {
                selectedKey = key;
                break;
            }
        }
        if (db.get(name).get(tableName).get(selectedKey).size() == 0
                || !db.get(name).get(tableName).containsKey(selectedKey)
                || !db.get(name).containsKey(tableName)) {
            throw new SQLException(query);
        }
        // convert xml to dom object tree
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // provide access to xml file
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

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
        Document readDocument = null;
        try {
            readDocument = docBuilder.parse(is);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        NodeList elements = readDocument.getDocumentElement().getElementsByTagName("tableColumn");
        for (int i = 0; i < db.get(name).get(tableName).get(selectedKey).size(); i++) {

            if (conditionReview(selectedKey, condition, i, name
                    ,tableName, db)) {
                for (int listUpdatedIndex = 0;
                     listUpdatedIndex * 3 < columnsToSet.length; listUpdatedIndex++) {
                    for (String key : db.get(name).get(tableName).keySet()) {
                        if (key.contains(columnsToSet[listUpdatedIndex * 3])) {
                            selectedKey = key;
                            break;
                        }
                    }
                    String[] splittedKey = selectedKey.split("\\s+");
                    if (splittedKey[2].equals("INT") && columnsToSet[listUpdatedIndex * 3 + 2].charAt(0) == '\'') {
                        throw new SQLException(query);
                    }

                    db.get(name).get(tableName).get(selectedKey).set(i, columnsToSet[listUpdatedIndex * 3 + 2]);
                    noOfUpdatedRows.add(i);
                    for (int fileIndex = 0; fileIndex < elements.getLength(); fileIndex++) {
                        if (elements.item(fileIndex).getAttributes().getNamedItem("column_id")
                                .getNodeValue().equals(columnsToSet[(listUpdatedIndex * 3)])) {
                            String updatedContent = elements.item(fileIndex).getTextContent();
                            LinkedList<String> splittedUpdatedContent = mysplit.split(updatedContent);
                            splittedUpdatedContent.set(i, columnsToSet[(listUpdatedIndex * 3 + 2)]);
                            String upcontent = "";
                            for (int index = 0; index < splittedUpdatedContent.size(); index++) {
                                upcontent += splittedUpdatedContent.get(index);
                                upcontent += " ";
                            }

                            elements.item(fileIndex).setTextContent(upcontent);
                            // write from temp memory to a file
                            // write from temp memory to a file
                            writeInFile.write(readDocument, xmlFile);

                        }
                    }
                }
            }

        }
    }
    public boolean conditionReview(String selectedKey, String[] condition, int i ,String name
            ,String tableName,Map<String, Map<String, DBMap>> db) {
        switch (condition[1]) {
            case "=":
                if (db.get(name).get(tableName).get(selectedKey).get(i).equals(condition[2])) {
                    return true;
                }
                break;
            case ">":
                if (Integer.valueOf(db.get(name).get(tableName).get(selectedKey).get(i).toString())
                        > Integer.valueOf(condition[2])) {
                    return true;
                }
                break;
            case "<":
                if (Integer.valueOf(db.get(name).get(tableName).get(selectedKey).get(i).toString())
                        < Integer.valueOf(condition[2])) {
                    return true;
                }
                break;
            case ">=":
                if (Integer.valueOf(db.get(name).get(tableName).get(selectedKey).get(i).toString())
                        >= Integer.valueOf(condition[2])) {
                    return true;
                }
                break;
            case "<=":
                if (Integer.valueOf(db.get(name).get(tableName).get(selectedKey).get(i).toString())
                        <= Integer.valueOf(condition[2])) {
                    return true;
                }
                break;
        }
        return false;
    }


}
