package sample.M;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.*;

public class FinalDBSM implements Database {

    Map<String, Map<String, DBMap>> db = new HashMap<>();
    private Compilor mycompilor = new Compilor();
    private Split mysplit = new Split();
    private WriteInFile writeInFile = new WriteInFile();
    private Dtd dtd = new Dtd();
    private Insert insert = new Insert();
    private Update update = new Update();
    private File theDir;
    private String dataBaseName = null;
    private String tableName = "";
    private String name;

    private boolean createMainFolder(String name) {
        File sample = new File(name);
        boolean flag = false;
        if (!sample.exists()) {
            sample.mkdirs();
            flag = true;
        }
        return flag;
    }

    @Override
    public String createDatabase(String databaseName, boolean dropIfExists) {
        if (databaseName.charAt(0) == System.getProperty("file.separator").charAt(0)) {
            databaseName = databaseName.substring(1, databaseName.length());
        }
        databaseName = databaseName.toUpperCase();
        this.dataBaseName = databaseName;
        int x = -1;
        for (int i = 0; i < databaseName.length(); i++) {
            if (databaseName.charAt(i) == System.getProperty("file.separator").charAt(0)) {
                x = i;
            }
        }
        name = databaseName.substring(x + 1, databaseName.length());
        if (!dropIfExists) {
            if (!db.containsKey(name)) {
                try {
                    executeStructureQuery("CREATE DATABASE " + name);
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        } else {
            try {
                File fileExist = new File(name);
                if (!fileExist.exists()) {
                    throw new RuntimeException();
                }
                executeStructureQuery("Drop DATABASE " + name);
                executeStructureQuery("CREATE DATABASE " + name);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return theDir.getAbsolutePath();
    }

    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
        LinkedList<String> splittedQury = mysplit.split(query);
        boolean successFlag = false;
        if (mycompilor.executeStructureQueryValidy(query)) {
            LinkedList<String> splittedQuery = mysplit.split(query);
            if (splittedQuery.get(1).equals("DATABASE")) {
                if (splittedQuery.get(0).equals("CREATE")) {
                    db.put(splittedQuery.get(2), new HashMap<String, DBMap>());
                    dataBaseName = splittedQuery.get(2);
                    theDir = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                            + this.dataBaseName);
                    name = dataBaseName;
                    // path=theDir.getAbsoluteFile().toString();
                    successFlag = true;
                    // if the directory does not exist, create it
                    if (!theDir.exists()) {
                        theDir.mkdirs();
                    }
                } else if (splittedQuery.get(0).equals("DROP")) {
                    this.dataBaseName = this.mysplit.split(query).get(2);
                    db.remove(this.dataBaseName);
                    // delete the files inside the data base first the delete the folder
                    try {
                        deleteDirectory(System.getProperty("user.dir") + System.getProperty("file.separator") + this.dataBaseName);
                        successFlag = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } else if (splittedQuery.get(1).equals("TABLE")) {
                if (splittedQuery.get(0).equals("CREATE")) {
                    if (dataBaseName == null) {
                        throw new SQLException(query);
                    }
                    this.tableName = this.mysplit.split(query).get(2);
                    dtd.createDtdFile(tableName, dataBaseName);
                    db.get(name).put(tableName, new DBMap());
                    int j = 1;
                    for (int i = 3; i < splittedQuery.size(); i++) {
                        db.get(name).get(tableName).put(j + " " + splittedQuery.get(i) + " " + splittedQuery.get(++i), new ArrayList<Object>());
                        j++;
                    }
                    if (this.dataBaseName.equals(null)) {
                        throw new RuntimeException();
                    }
                    this.tableName = this.mysplit.split(query).get(2);

                    // checkIntegerVarchar(query);
                    // don't forget to call this method

                    // check 3la esm l table mn 3nd belal lw rg3t true hdefo fl list
                    // tableNames.add(tableName);

                    // createDtdFile(tableName);

                    String[] columnIds = new String[this.mysplit.split(query).size() - 3];
                    for (int index = 3; index < this.mysplit.split(query).size(); index++) {
                        columnIds[index - 3] = this.mysplit.split(query).get(index);
                    }

                    // convert xml to dom object tree
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    try {
                        // provide access to xml file
                        DocumentBuilder docBuilder = dbf.newDocumentBuilder();

                        // DOM tree structure object,Dom used to read xml file
                        Document document = docBuilder.newDocument();

                        Element root = document.createElement("table");
                        for (int index = 0; index < columnIds.length; index++) {
                            Element tableColumn = document.createElement("tableColumn");

                            if (2 * index < columnIds.length) {
                                tableColumn.setAttribute("column_id", columnIds[2 * index]);
                            }

                            if (2 * index < columnIds.length) {
                                tableColumn.setAttribute("type", columnIds[2 * index + 1]);

                                root.appendChild(tableColumn);
                            }

                        }
                        document.appendChild(root);

                        // write from temp memory to a file
                        String path = createDatabase(this.dataBaseName, false) + System.getProperty("file.separator")
                                + this.tableName + ".xml";
                        File xmlFile = new File(path);
                        if (xmlFile.exists()) {
                            return false;
                        }
                        DOMSource domSource = new DOMSource(document);
                        Result result = new StreamResult(xmlFile);
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        try {
                            Transformer transformer = transformerFactory.newTransformer();
                            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "No");
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

                            try {
                                transformer.transform(domSource, result);

                                successFlag = true;

                            } catch (TransformerException e) {
                                e.printStackTrace();
                            }
                        } catch (TransformerConfigurationException e) {
                            e.printStackTrace();
                        }
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                } else {
                    this.tableName = this.mysplit.split(query).get(2);
                    dtd.deleteDtdFile(tableName, dataBaseName);
                    if (dataBaseName == null) {
                        throw new SQLException(query);
                    }
                    String path = createDatabase(this.dataBaseName, false) + System.getProperty("file.separator")
                            + this.tableName + ".xml";
                    File file = new File(path);
                    db.get(name).remove(tableName);
                    file.delete();
                    successFlag = true;
                }
            }
        } else {
            throw new SQLException(query);
        }
        return successFlag;
    }

    private void deleteDirectory(String directoryFilePath) throws IOException {
        Path directory = Paths.get(directoryFilePath);
        if (Files.exists(directory)) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes)
                        throws IOException {
                    Files.delete(path);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException {
                    Files.delete(directory);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    @Override
    public Object[][] executeQuery(String query) throws SQLException {
        if (!mycompilor.executeQueryValidation(query)) {
            throw new SQLException(query);
        }
        LinkedList<LinkedList<Object>> selected = new LinkedList<>();
        LinkedList<String> splittedQuery = mysplit.split(query);
        this.tableName = splittedQuery.get(splittedQuery.indexOf("FROM") + 1);
        File xmlFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + this.dataBaseName + System.getProperty("file.separator") + tableName + ".xml");
        if (xmlFile.exists() && !db.get(name).containsKey(this.tableName)) {
            db.get(name).put(tableName, new DBMap());
            readingIntoDbMMap(db);
        }
        if (!xmlFile.exists()) {
            return new Object[0][];
        }
        if (splittedQuery.contains("WHERE")) {
            String[] condition = splittedQuery.subList(splittedQuery.size() - 3, splittedQuery.size()).toArray(new String[0]);
            if (splittedQuery.get(1).equals("*")) {
                String selectedKey = null;
                for (String key : db.get(name).get(tableName).keySet()) {
                    if (key.contains(condition[0])) {
                        selectedKey = key;
                        break;
                    }
                }
                for (int i = 0; i < db.get(name).get(tableName).size(); i++) {
                    selected.add(new LinkedList<Object>());
                }
                for (int i = 0; i < db.get(name).get(tableName).get(selectedKey).size(); i++) {
                    int k = 0;
                    if (conditionReview(selectedKey, condition, i)) {
                        for (String key : db.get(name).get(tableName).keySet()) {
                            if (key.charAt(key.length() - 1) == 'T') {
                                selected.get(k).add(Integer.valueOf(db.get(name).get(tableName).get(key).get(i).toString()));
                            } else {
                                selected.get(k).add(db.get(name).get(tableName).get(key).get(i));
                            }
                            k++;
                        }
                    }
                }
            } else {
                String[] columns = splittedQuery.subList(1, splittedQuery.indexOf("FROM")).toArray(new String[0]);
                for (int i = 0; i < columns.length; i++) {
                    selected.add(new LinkedList<Object>());
                }
                String selectedKey = null;
                for (String key : db.get(name).get(tableName).keySet()) {
                    if (key.contains(condition[0])) {
                        selectedKey = key;
                        break;
                    }
                }
                for (int i = 0; i < db.get(name).get(tableName).get(selectedKey).size(); i++) {
                    String selectedColumn = null;
                    if (conditionReview(selectedKey, condition, i)) {
                        for (int j = 0; j < columns.length; j++) {
                            for (String key : db.get(name).get(tableName).keySet()) {
                                if (key.contains(columns[j])) {
                                    selectedColumn = key;
                                    break;
                                }
                            }
                            if (selectedColumn.charAt(selectedColumn.length() - 1) == 'T') {
                                selected.get(j).add(Integer.valueOf(db.get(name).get(tableName).get(selectedColumn).get(i).toString()));
                            } else {
                                selected.get(j).add(db.get(name).get(tableName).get(selectedColumn).get(i));
                            }
                        }

                    }
                }

            }
        } else if (splittedQuery.get(1).equals("*")) {
            for (int i = 0; i < db.get(name).get(tableName).size(); i++) {
                selected.add(new LinkedList<Object>());
            }
            int k = 0;
            for (String key : db.get(name).get(tableName).keySet()) {
                for (int i = 0; i < db.get(name).get(tableName).get(key).size(); i++) {
                    if (key.charAt(key.length() - 1) == 'T') {
                        selected.get(k).add(Integer.valueOf(db.get(name).get(tableName).get(key).get(i).toString()));
                    } else {
                        selected.get(k).add(db.get(name).get(tableName).get(key).get(i));
                    }
                }
                k++;
            }
        } else {
            String[] columns = splittedQuery.subList(1, splittedQuery.indexOf("FROM")).toArray(new String[0]);
            for (int i = 0; i < columns.length; i++) {
                selected.add(new LinkedList<Object>());
            }
            String selectedColumn = null;
            for (int j = 0; j < columns.length; j++) {
                for (String key : db.get(name).get(tableName).keySet()) {
                    if (key.contains(columns[j])) {
                        selectedColumn = key;
                        break;
                    }
                }
                for (int i = 0; i < db.get(name).get(tableName).get(selectedColumn).size(); i++) {
                    if (selectedColumn.charAt(selectedColumn.length() - 1) == 'T') {
                        selected.get(j).add(Integer.valueOf(db.get(name).get(tableName).get(selectedColumn).get(i).toString()));
                    } else {
                        selected.get(j).add(db.get(name).get(tableName).get(selectedColumn).get(i));
                    }
                }

            }

        }
        int columnSize = selected.size();
        int rowSize = selected.get(0).size();
        Object[][] ans = new Object[rowSize][columnSize];
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (!(selected.get(j).get(i).toString().charAt(0) == '\'')) {
                    ans[i][j] = (Integer) selected.get(j).get(i);
                } else {
                    ans[i][j] = selected.get(j).get(i);
                }
            }
        }
        return ans;
    }

    public boolean conditionReview(String selectedKey, String[] condition, int i) {
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

    @Override
    public int executeUpdateQuery(String query) throws SQLException {

        Set<Integer> noOfUpdatedRows = new HashSet<>();
        if (!mycompilor.executeUpdateQueryValidation(query)) {
            throw new SQLException(query);
        }
        LinkedList<String> splittedQuery = mysplit.split(query);
        if (splittedQuery.contains("UPDATE")) {
            this.tableName = splittedQuery.get(1);
        } else {
            this.tableName = splittedQuery.get(2);
        }
        File xmlFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + this.dataBaseName + System.getProperty("file.separator") + tableName + ".xml");
        if (xmlFile.exists() && !db.get(name).containsKey(this.tableName)) {
            db.get(name).put(tableName, new DBMap());
            readingIntoDbMMap(db);
        }
        if (!xmlFile.exists()) {
            return 0;
        }
        if (splittedQuery.get(0).equals("INSERT")) {

            mysplit.findTheElementsToInsert(query);
            LinkedList<String> valuesToInsert = mysplit.getvaluesToInsert();
            LinkedList<String> nameOfColsToInsertIn = mysplit.getnameOfColsToInsertIn();

            // checkIntegerVarchar(query);

            // convert xml to dom object tree
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // provide access to xml file
            DocumentBuilder docBuilder = null;
            String str = null;
            if (splittedQuery.get(3).equals("VALUES")) {
                insert.insertAtAll(name, tableName, query, str
                        , splittedQuery, docBuilder
                        , dbf, noOfUpdatedRows
                        , db, xmlFile
                        , valuesToInsert);
            } else {
                str = insert.insertIntoColumns(name, tableName, query, str
                        , splittedQuery, docBuilder
                        , dbf, noOfUpdatedRows
                        , db, xmlFile
                        , valuesToInsert
                        , nameOfColsToInsertIn);
            }
//            noOfUpdatedRows.add(db.get(name).get(tableName).get(str).size() - 1);
        } else if (splittedQuery.get(0).equals("UPDATE")) {
            if (!db.get(name).containsKey(splittedQuery.get(1))) {
                throw new SQLException(query);
            }
            if (splittedQuery.contains("WHERE")) {
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

                    if (conditionReview(selectedKey, condition, i)) {
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
                                    DOMSource domSource = new DOMSource(readDocument);

                                    // StreamResult(the file to save in)
                                    // StreamResult it writes in the file
                                    Result result = new StreamResult(xmlFile);
                                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                                    try {
                                        Transformer transformer = transformerFactory.newTransformer();
                                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                                        transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

                                        try {
                                            transformer.transform(domSource, result);
                                        } catch (TransformerException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (TransformerConfigurationException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }

                }
            } else {
                String[] columnsToSet = splittedQuery.subList(splittedQuery.indexOf("SET") + 1
                        , splittedQuery.size()).toArray(new String[0]);
                String selectedKey = null;
                String[] column = db.get(name).get(tableName).keySet();

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
                for (int i = 0; i < db.get(name).get(tableName).get(db.get(name).get(tableName).keySet()[0]).size(); i++) {
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
                                DOMSource domSource = new DOMSource(readDocument);

                                // StreamResult(the file to save in)
                                // StreamResult it writes in the file
                                Result result = new StreamResult(xmlFile);
                                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                                try {
                                    Transformer transformer = transformerFactory.newTransformer();
                                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                                    transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

                                    try {
                                        transformer.transform(domSource, result);
                                    } catch (TransformerException e) {
                                        e.printStackTrace();
                                    }
                                } catch (TransformerConfigurationException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }


                }
            }
        } else if (splittedQuery.get(0).equals("DELETE")) {
            if (splittedQuery.contains("WHERE")) {
                String selectedKey = null;
                String[] condition = splittedQuery.subList(splittedQuery.size() - 3, splittedQuery.size()).toArray(new String[0]);
                for (String key : db.get(name).get(tableName).keySet()) {
                    if (key.contains(condition[0])) {
                        selectedKey = key;
                        break;
                    }
                }
//                if (db.get(name).get(tableName).get(selectedKey).size() == 0
//                        || !db.get(name).get(tableName).containsKey(selectedKey)
//                        || !db.get(name).containsKey(tableName)) {
//                    throw new SQLException(query);
//                }
                int j = 0;
                for (int i = 0; i < db.get(name).get(tableName).get(selectedKey).size(); i++) {
                    if (conditionReview(selectedKey, condition, i)) {
                        for (String key : db.get(name).get(tableName).keySet()) {
                            db.get(name).get(tableName).get(key).remove(i);
                            noOfUpdatedRows.add(j);
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
                            for (int fileIndex = 0; fileIndex < elements.getLength(); fileIndex++) {
                                String deletedContent = elements.item(fileIndex).getTextContent();
                                LinkedList<String> splitteddeletedContent = mysplit.split(deletedContent);
                                // put the array in alinked list to remove
                                LinkedList<String> deletedItems = new LinkedList<>();
                                for (int index = 0; index < splitteddeletedContent.size(); index++) {
                                    deletedItems.add(splitteddeletedContent.get(index));
                                }
                                deletedItems.remove(i);
                                deletedContent = "";
                                for (int index = 0; index < deletedItems.size(); index++) {
                                    deletedContent += deletedItems.get(index) + " ";
                                }
                                // push the string into the file
                                elements.item(fileIndex).setTextContent(deletedContent);
                                // write from temp memory to a file
                                writeInFile.write(readDocument, xmlFile);

                            }
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // DOM tree structure object,Dom usedd to read xml file
                        i--;
                    }
                    j++;
                }

            } else {
                String[] columns = db.get(name).get(tableName).keySet();
                if (!db.get(name).containsKey(tableName)) {
                    return 0;
                }
                for (int row = 0; row < db.get(name).get(tableName).get(columns[0]).size(); row++) {
                    for (int i = 0; i < db.get(name).get(tableName).size(); i++) {
                        noOfUpdatedRows.add(i);
                        // convert xml to dom object tree
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        // provide access to xml file
                        DocumentBuilder docBuilder = null;
                        try {
                            docBuilder = dbf.newDocumentBuilder();
                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        }
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
                            String deletedContent = elements.item(i).getTextContent();
                            LinkedList<String> splitteddeletedContent = mysplit.split(deletedContent);
                            // put the array in alinked list to remove
                            LinkedList<String> deletedItems = new LinkedList<>();
                            for (int index = 0; index < splitteddeletedContent.size(); index++) {
                                deletedItems.add(splitteddeletedContent.get(index));
                            }
                            deletedItems.remove(row);
                            deletedContent = "";
                            // push the string into the file
                            elements.item(i).setTextContent(deletedContent);
                            // write from temp memory to a file
                            writeInFile.write(readDocument, xmlFile);
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // DOM tree structure object,Dom usedd to read xml file
                        db.get(name).get(tableName).get(db.get(name).get(tableName).keySet()[row]).clear();
                    }
                }
            }
        }
        return noOfUpdatedRows.size();
    }

    public void readingIntoDbMMap(Map<String, Map<String, DBMap>> db) {
        LinkedList<Node> fileContent = loadFile(this.dataBaseName, tableName);
        for (int i = 0; i < fileContent.size(); i++) {
            db.get(name).get(tableName).put(String.valueOf(i + 1) + " " + fileContent.get(i).columnName + " " + fileContent.get(i).columnType
                    , linkedToArr(fileContent.get(i).content));
        }
    }

    public ArrayList<Object> linkedToArr(LinkedList<String> content) {
        ArrayList<Object> contentArr = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            contentArr.add(content.get(i));
        }
        return contentArr;
    }

    public LinkedList<Node> loadFile(String dataBaseName, String tableName) {
        LinkedList<Node> fileContent = new LinkedList<>();
        File xmlFile = new File(System.getProperty("user.dir") + System.getProperty("file.separator")
                + dataBaseName + System.getProperty("file.separator") + tableName + ".xml");
        // convert xml to dom object tree
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // provide access to xml file
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbf.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        // DOM tree structure object,Dom usedd to read xml file
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
        for (int elementsIndex = 0; elementsIndex < elements.getLength(); elementsIndex++) {
            Node node = new Node();
            if (elements.item(elementsIndex).getTextContent().length() > 0) {
                node.content = mysplit.split(elements.item(elementsIndex).getTextContent());
            } else {
                node.content = new LinkedList<String>();
            }
            node.columnName = elements.item(elementsIndex).getAttributes().item(0).getTextContent();
            node.columnType = elements.item(elementsIndex).getAttributes().item(1).getTextContent();
            fileContent.add(elementsIndex, node);
        }
        return fileContent;
    }

    public static void main(String[] args) {
        FinalDBSM b = new FinalDBSM();
//        System.out.println(b.createDatabase(System.getProperty("file.separator")+"usr"+System.getProperty("file.separator")+"share"+System.getProperty("file.separator")+"tomcat7"+System.getProperty("file.separator")+"webapps"+System.getProperty("file.separator")+"ROOT"+System.getProperty("file.separator")+"temp"+System.getProperty("file.separator")+"k_mohamed"+System.getProperty("file.separator")+"oop"+System.getProperty("file.separator")+"1512239766810"+System.getProperty("file.separator")+"ThirdTermFirstAssignment"+System.getProperty("file.separator")+"Third term assignmets"+System.getProperty("file.separator")+"src"+System.getProperty("file.separator")+"sample"+System.getProperty("file.separator")+"9632.17170098759", false));
        b.createDatabase("sample" + System.getProperty("file.separator") + "SAra", false);
//        try {
//            b.executeStructureQuery("DROP DATABASE TestDB");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        try {
//            b.executeUpdateQuery("INSERT INTO * table_name2(column_NAME1, COLUMN_name3, column_name2) VALUES ('value1', 'value3', 4)");
//            b.executeStructureQuery("Create TABLE table_name1(column_name1 varchar, column_name2 int, column_name3 varchar)");
//            b.executeUpdateQuery("INSERT INTO table_name1 VALUES ('value1',4, 'value3')");
            b.executeUpdateQuery("INSERt INTO table_name1 VALUES ('value1',4,'value3')");
            b.executeUpdateQuery("INSERT INTO table_name1(column_name1, COLUMN_NAME3, column_NAME2) VAlUES ('value2', 'value4', 5)");
//            b.executeUpdateQuery("DELETE FROM table_name1 where column_NAME2=5");
//            System.out.println(b.executeUpdateQuery("UPDATE table_name1 SET column_NAME1 = 'value1', column_name2 = 2 WHERE column_name2>4"));
//            b.executeQuery("SELECT * FROM table_name1");
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try {
//            b.executeStructureQuery("DROP TABLE table_name5");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
