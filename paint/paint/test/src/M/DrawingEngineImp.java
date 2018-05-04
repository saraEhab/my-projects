package M;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.xml.sax.*;
import org.w3c.dom.*;


/**
 * Created by TOSIBA-PC on 30/10/2017.
 */
public class DrawingEngineImp implements DrawingEngine {
    private LinkedList<Shape> listOfShapes = new LinkedList<>();
    private LinkedList<Shape> undoShapes = new LinkedList<>();
    private LinkedList<HistoryNode> history = new LinkedList<>();
    private LinkedList<String> nameOfShape = new LinkedList<>();
    private Shape[] shapes;
    private boolean jsonFlag=false,xmlFlag=false;

    private int hitoryIndex;
    private List<Class<? extends Shape>> plugInShapes ;
    public DrawingEngineImp(){
        plugInShapes = new LinkedList<>();
        plugInShapes.add(Circle.class);
        plugInShapes.add(Ellipse.class);
        plugInShapes.add(Line.class);
        plugInShapes.add(Rectangle.class);
        plugInShapes.add(Square.class);
        plugInShapes.add(Triangle.class);

    }


    @Override
    public LinkedList<String> nameOfShapeGetter(){
        return this.nameOfShape;
    } //a linked list with shapes names

    @Override
    public LinkedList<HistoryNode> historyGetter() {
        return history;
    } //linked list carrys the actions

    @Override
    public LinkedList<Shape> listOFShapesGetter() { //list of the shapes
        return listOfShapes;
    }

    @Override
    public void findShapeName() { // creating the list of shapes name
        nameOfShape.clear();
        for (Shape listOfShape : listOfShapes) {
            if (listOfShape.getProperties().get("Name") == 3.0) {
                nameOfShape.add("circle");
            } else if (listOfShape.getProperties().get("Name") == 1.0) {
                nameOfShape.add("triangle");
            } else if (listOfShape.getProperties().get("Name") == 5.0) {
                nameOfShape.add("rectangle");
            } else if (listOfShape.getProperties().get("Name") == 4.0) {
                nameOfShape.add("ellipse");
            } else if (listOfShape.getProperties().get("Name") == 0.0) {
                nameOfShape.add("line");
            } else if (listOfShape.getProperties().get("Name") == 2.0) {
                nameOfShape.add("square");
            } else if (listOfShape.getProperties().get("Name") == 6.0) {
                nameOfShape.add("the new plug");
            }
        }
    }

    @Override
    public void refresh(Graphics canvas) {
        for (Shape listOfShape : this.listOfShapes) {
            listOfShape.draw(canvas);
        }
    }

    private void clearingHistory() {
        while (history.size() > hitoryIndex + 1 && history.size() > 0) {
            history.removeLast();
        }
    }

    @Override
    public void addShape(Shape shape) { //drawing the shape and adding it to the list os shapes
        clearingHistory();
        listOfShapes.add(shape);
        HistoryNode o = new HistoryNode();
        o.shape1 = shape;
        o.s = "add";
        o.shape2 = null;
        if (history.size() < 20) {
            history.add(o);
        } else {
            history.removeFirst();
            history.add(o);
        }
        hitoryIndex = history.size() - 1;


    }

    @Override
    public void removeShape(Shape shape) { // removing a shape from the canvas and from the list of shapes
        listOfShapes.remove(shape);
        clearingHistory();
        HistoryNode o = new HistoryNode();
        o.shape1 = shape;
        o.s = "remove";
        o.shape2 = null;
        if (history.size() < 20) {
            history.add(o);
        } else {
            history.removeFirst();
            history.add(o);
        }
        hitoryIndex = history.size() - 1;

    }

    @Override
    public void updateShape(Shape oldShape, Shape newShape) { //updating the selected shape
        clearingHistory();
        listOfShapes.remove(oldShape);
        listOfShapes.add(newShape);
        HistoryNode o = new HistoryNode();
        o.shape1 = oldShape;
        o.s = "update";
        o.shape2 = newShape;
        if (history.size() < 20) {
            history.add(o);
        } else {
            history.removeFirst();
            history.add(o);
        }
        hitoryIndex = history.size() - 1;

    }

    @Override
    public Shape[] getShapes() { //getting the array of shapes
        shapes = new Shape[this.listOfShapes.size()];
        for (int index = 0; index < shapes.length; index++) {
            shapes[index] = this.listOfShapes.get(index);
        }
        return shapes;
    }

    @Override
    public List<Class<? extends Shape>> getSupportedShapes() { // the supported shapes in the program
        return this.plugInShapes;
    }
    @Override
    public void installPlugIn(Class<? extends Shape> newPlugIn) {
        this.plugInShapes.add(newPlugIn);
    }

    @Override
    public void undo() { //canceling actions
        if (hitoryIndex >= 0) {
            if (history.get(hitoryIndex).s.equals("add")) {
                listOfShapes.remove(history.get(hitoryIndex).shape1);
            } else if (history.get(hitoryIndex).s.equals("remove")) {
                listOfShapes.add(history.get(hitoryIndex).shape1);
            } else if (history.get(hitoryIndex).s.equals("update")) {
                listOfShapes.set(listOfShapes.indexOf(history.get(hitoryIndex).shape2)
                        ,history.get(hitoryIndex).shape1);
            }
            hitoryIndex--;
        }

    }

    @Override
    public void redo() { //redoing actions
        if (hitoryIndex <= history.size() - 2 && history.size() > 0) {
            hitoryIndex++;
            if (history.get(hitoryIndex).s.equals("add")) {
                listOfShapes.add(history.get(hitoryIndex).shape1);
            } else if (history.get(hitoryIndex).s.equals("remove")) {
                listOfShapes.remove(history.get(hitoryIndex).shape1);
            } else if (history.get(hitoryIndex).s.equals("update")) {
//                listOfShapes.indexOf(history.get(hitoryIndex).shape2);
                listOfShapes.set(listOfShapes.indexOf(history.get(hitoryIndex).shape1)
                        ,history.get(hitoryIndex).shape2);
            }
        }

    }

    @Override
    public void save(String path) { //saving the canvas
        if (path.substring(path.length() - 5, path.length()).equals(".json")) {
            creatingJsonFile(path);
        } else if (path.substring(path.length() - 4, path.length()).equals(".xml")) {
            saveToXML(path);
        }

    }

    private void creatingJsonFile(String path) { //creating the json file
        File file = new File(path);
        BufferedWriter bw = null;
        if (!file.exists()) {
            try {
                boolean create = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            boolean delete = file.delete();
            try {
                boolean create = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw;
        try {
            fw = new FileWriter(file);

            bw = new BufferedWriter(fw);
            bw.write("[");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < listOfShapes.size(); i++) {
            JsonObject obj = new JsonObject();
            if (listOfShapes.get(i) == null
                    || listOfShapes.get(i).getPosition() == null
                    || listOfShapes.get(i).getColor() == null
                    || listOfShapes.get(i).getFillColor() == null
                    || listOfShapes.get(i).getProperties() == null) {
                obj.put("null", null);
            } else {
                obj.put("Name", listOfShapes.get(i).getProperties().get("Name").toString());
                obj.put("Position", listOfShapes.get(i).getPosition());
                obj.put("fillColor", listOfShapes.get(i).getFillColor());
                obj.put("Color", listOfShapes.get(i).getColor());
                if (listOfShapes.get(i).getProperties().containsKey("Length")) {
                    obj.put("Length", listOfShapes.get(i).getProperties().get("Length").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("raduis")) {
                    obj.put("raduis", listOfShapes.get(i).getProperties().get("raduis").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("Width")) {
                    obj.put("Width", listOfShapes.get(i).getProperties().get("Width").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("Height")) {
                    obj.put("Height", listOfShapes.get(i).getProperties().get("Height").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("x")) {
                    obj.put("x", listOfShapes.get(i).getProperties().get("x").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("y")) {
                    obj.put("y", listOfShapes.get(i).getProperties().get("y").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("x2")) {
                    obj.put("x2", listOfShapes.get(i).getProperties().get("x2").toString());
                }
                if (listOfShapes.get(i).getProperties().containsKey("y2")) {
                    obj.put("y2", listOfShapes.get(i).getProperties().get("y2").toString());
                }

                try {
                    if (bw != null) {
                        bw.write("{");
                    }
//                fw = new FileWriter(file);
//
//                bw = new BufferedWriter(fw);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < obj.getList().size(); j++) {
                    try {
                        bw.write(String.valueOf(obj.getList().get(j).first));
                        bw.write(String.valueOf(obj.getList().get(j).second));
                        if (j != obj.getList().size() - 1) {
                            bw.write(",");
                            bw.newLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    bw.write("}");
                    if (i != listOfShapes.size() - 1) {
                        bw.write(",");
                    }
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            if (bw != null) {
                bw.write("]");
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToXML(String xml) {

        Document dom = null, domShape = null;
        Element e;
        Element shape = null;

        // instance of a DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use factory to get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // create instance of DOM
            dom = db.newDocument();
            // create the root element
            Element root = dom.createElement("shapes");

            e = dom.createElement("size");

            e.appendChild(dom.createTextNode(String.valueOf(getShapes().length)));
            root.appendChild(e);

            for (int i = 0; i < getShapes().length; i++) {
                // create data elements and place them under root
                if (listOfShapes.get(i) == null
                        || listOfShapes.get(i).getPosition() == null
                        || listOfShapes.get(i).getColor() == null
                        || listOfShapes.get(i).getFillColor() == null
                        || listOfShapes.get(i).getProperties() == null) {
                    Element rootEle = dom.createElement("Shape" + String.valueOf(i));
                    e.appendChild(dom.createTextNode(null));
                    root.appendChild(rootEle);
                } else {
                    Element rootEle = dom.createElement("Shape" + String.valueOf(i));

                    e = dom.createElement("name" + String.valueOf(i));
                    e.appendChild(dom.createTextNode(getNameOfShape(getShapes()[i].getProperties().get("Name"))));
                    rootEle.appendChild(e);

                    e = dom.createElement("position" + String.valueOf(i));
                    e.appendChild(dom.createTextNode(String.valueOf(getShapes()[i].getPosition())));
                    rootEle.appendChild(e);

                    e = dom.createElement("fillColor" + String.valueOf(i));
                    e.appendChild(dom.createTextNode(String.valueOf(getShapes()[i].getFillColor())));
                    rootEle.appendChild(e);

                    e = dom.createElement("color" + String.valueOf(i));
                    e.appendChild(dom.createTextNode(String.valueOf(getShapes()[i].getColor())));
                    rootEle.appendChild(e);
                    e = dom.createElement("size" + String.valueOf(i));
                    e.appendChild(dom.createTextNode(String.valueOf(getShapes()[i].getProperties().size() - 1)));
                    rootEle.appendChild(e);

                    for (String key : getShapes()[i].getProperties().keySet()) {
                        if (!key.equals("Name")) {
                            e = dom.createElement(key + String.valueOf(i));
                            e.appendChild(dom.createTextNode(String.valueOf(getShapes()[i].getProperties().get(key))));
                            rootEle.appendChild(e);
                        }
                    }
                    root.appendChild(rootEle);

                }
            }
            dom.appendChild(root);
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            // send DOM to file
            tr.transform(new DOMSource(dom),
                    new StreamResult(new FileOutputStream(xml)));

        } catch (TransformerException | IOException te) {
            System.out.println(te.getMessage());
        }
    }

    @Override
    public void load(String path) { //loading the files
        undoShapes.clear();
        listOfShapes.clear();
        if (path.substring(path.length() - 5, path.length()).equals(".json")) {
            jsonFlag=true;
            loadingJsonFile(path);
        } else if (path.substring(path.length() - 4, path.length()).equals(".xml")) {
            xmlFlag=true;
            readXML(path);
        }
        jsonFlag=false;
        xmlFlag=false;
    }

    private String getTextValue(String def, Element doc, String tag) {
        String value = def;
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
            value = nl.item(0).getFirstChild().getNodeValue();
        }
        return value;
    }

    private void readXML(String xml) { //loading the xml file
        String name = null, position = null, fillColor = null, color = null, shap = null,
                x = null, y = null, x2 = null, y2 = null, length = null, raduis = null, width = null, height = null, size;
        Document dom;
        // Make an  instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // use the factory to take an instance of the document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            // parse using the builder to get the DOM mapping of the
            // XML file
            dom = db.parse(xml);
            Element doc = dom.getDocumentElement();
            size = getTextValue(shap, doc, "size");
            for (int i = 0; i < Integer.valueOf(size); i++) {
                Shape loadedShape = null;
                Map<String, Double> temp = new HashMap<>();
                shap = getTextValue(shap, doc, "shap" + String.valueOf(i));
                name = getTextValue(name, doc, "name" + String.valueOf(i));
                temp.put("Name", Double.valueOf(name));
                if (name != null) {
                    if (!name.isEmpty()) {
                        loadedShape = getName(name.charAt(0));
                    }

                }
                position = getTextValue(position, doc, "position" + String.valueOf(i));
                Point point = new Point();
                if (position != null) {
                    if (!position.isEmpty()) {
                        loadedShape.setPosition(loadPosition(point, position));

                    }

                }
                fillColor = getTextValue(fillColor, doc, "fillColor" + String.valueOf(i));
                if (fillColor != null) {
                    if (!fillColor.isEmpty()) {
                        loadedShape.setFillColor(loadFillColor(fillColor));
                    }

                }
                color = getTextValue(color, doc, "color" + String.valueOf(i));
                if (color != null) {
                    if (!color.isEmpty()) {
                        loadedShape.setColor(loadColor(color));
                    }

                }
                x = getTextValue(x, doc, "x" + String.valueOf(i));
                if (x != null) {
                    if (!x.isEmpty()) {
                        temp.put("x", Double.valueOf(x));
                    }

                }
                y = getTextValue(y, doc, "y" + String.valueOf(i));
                if (y != null) {
                    if (!y.isEmpty()) {
                        temp.put("y", Double.valueOf(y));
                    }

                }
                x2 = getTextValue(x2, doc, "x2" + String.valueOf(i));
                if (x2 != null) {
                    if (!x2.isEmpty()) {
                        temp.put("x2", Double.valueOf(x2));
                    }

                }
                y2 = getTextValue(y2, doc, "y2" + String.valueOf(i));
                if (y2 != null) {
                    if (!y2.isEmpty()) {
                        temp.put("y2", Double.valueOf(y2));


                    }

                }
                length = getTextValue(length, doc, "Length" + String.valueOf(i));
                if (length != null) {
                    if (!length.isEmpty()) {
                        temp.put("Length", Double.valueOf(length));
                    }

                }
                raduis = getTextValue(raduis, doc, "raduis" + String.valueOf(i));
                if (raduis != null) {
                    if (!raduis.isEmpty()) {
                        temp.put("raduis", Double.valueOf(raduis));

                    }

                }
                width = getTextValue(width, doc, "Width" + String.valueOf(i));
                if (width != null) {
                    if (!width.isEmpty()) {
                        temp.put("Width", Double.valueOf(width));

                    }

                }
                height = getTextValue(height, doc, "Height" + String.valueOf(i));
                if (height != null) {
                    if (!height.isEmpty()) {
                        temp.put("Height", Double.valueOf(height));

                    }

                }


                loadedShape.setProperties(temp);
                addShape(loadedShape);
            }
        } catch (ParserConfigurationException | SAXException pce) {
            System.out.println(pce.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

    }

    private Shape getName(char c) { //getting the shapes from the reprsented number
        Shape shape = null;
        if (c == '0') {
            shape = new Line();
        } else if (c == '1') {
            shape = new Triangle();
        } else if (c == '2') {
            shape = new Square();
        } else if (c == '3') {
            shape = new Circle();
        } else if (c == '4') {
            shape = new Ellipse();
        } else if (c == '5') {
            shape = new Rectangle();
        }
        return shape;
    }

    private String getNameOfShape(double c) {
        String shape = null;
        if (c == 0.0) {
            shape = "0";
        } else if (c == 1.0) {
            shape = "1";
        } else if (c == 2.0) {
            shape = "2";
        } else if (c == 3.0) {
            shape = "3";
        } else if (c == 4.0) {
            shape = "4";
        } else if (c == 5.0) {
            shape = "5";
        }
        return shape;
    }

    private void loadingJsonFile(String path) { //loading the json file
        BufferedReader br = null;
        FileReader fr = null;
        try {
            br = new BufferedReader(new FileReader(path));
            fr = new FileReader(path);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int counter = 0;
        Shape shape;
        try {
            String sCurrentLine = null;
            if (br != null) {
                sCurrentLine = br.readLine();
            }
            while (sCurrentLine != null && !(sCurrentLine.equals("]"))) {
                Map<String, Double> temp ;
                temp = new HashMap<>();
                if (counter == 0) {
                    char name = sCurrentLine.charAt(10);
                    shape = getName(name);
                    temp.put("Name", (double) Character.getNumericValue(name));
                    counter++;
                } else {
                    char name = sCurrentLine.charAt(9);
                    shape = getName(name);
                    temp.put("Name", (double) Character.getNumericValue(name));
                }
                sCurrentLine = br.readLine();
                Point point = new Point();
                shape.setPosition(loadPosition(point, sCurrentLine));
                sCurrentLine = br.readLine();
                shape.setFillColor(loadFillColor(sCurrentLine));

                sCurrentLine = br.readLine();
                shape.setColor(loadColor(sCurrentLine));

                sCurrentLine = br.readLine();
                while (sCurrentLine.length() > 1) {
                    int k = 1;
                    String s1 = "", s2 = "";
                    for (int state = 0; state < 2; state++) {
                        while (sCurrentLine.charAt(k) != '"') {
                            if (state == 0) {
                                s1 += sCurrentLine.charAt(k);
                            }
                            if (state == 1) {
                                s2 += sCurrentLine.charAt(k);
                            }
                            k++;
                        }
                        k += 3;

                    }
                    temp.put(s1, Double.valueOf(s2));
                    if ((sCurrentLine.charAt(sCurrentLine.length() - 2) == '}'
                            || sCurrentLine.charAt(sCurrentLine.length() - 1) == '}')) {
                        break;
                    } else {
                        sCurrentLine = br.readLine();
                    }
                }
                shape.setProperties(temp);
                addShape(shape);
                sCurrentLine = br.readLine();
            }
        } catch (IOException e) {

            e.printStackTrace();

        }
        try {

            br.close();

            if (fr != null)
                fr.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }
    }

    private Point loadPosition(Point point, String sCurrentLine) { //loading the shapes positions
        int i = 0;
        if (xmlFlag) {
            i = 17;
        }
        else if (jsonFlag){
            i=29;
        }
        StringBuilder x = new StringBuilder();
        StringBuilder y = new StringBuilder();
        while (sCurrentLine.charAt(i) != ',') {
            x.append(sCurrentLine.charAt(i));
            i++;
        }
        i += 3;
        while (sCurrentLine.charAt(i) != ']') {
            y.append(sCurrentLine.charAt(i));
            i++;
        }
        point.x = Integer.valueOf(x.toString());
        point.y = Integer.valueOf(y.toString());
        return point;
    }

    private Color loadFillColor(String sCurrentLine) { //loading the fill color

        int i = 0;
        if (xmlFlag) {
            i = 17;
        }
        else if (jsonFlag){
            i=30;
        }
        StringBuilder value = new StringBuilder();
        int r = 0, b = 0, g = 0;
        for (int j = 0; j < 3; j++) {
            while (sCurrentLine.charAt(i) != ',' && sCurrentLine.charAt(i) != ']') {
                value.append(sCurrentLine.charAt(i));
                i++;
            }
            if (j == 0) {
                r = Integer.valueOf(value.toString());
            } else if (j == 1) {
                g = Integer.valueOf(value.toString());
            } else if (j == 2) {
                b = Integer.valueOf(value.toString());
            }
            value = new StringBuilder();
            i += 3;
        }

        return new Color(r, g, b);
    }

    private Color loadColor(String sCurrentLine) { // loading the border color
        int i = 0, r = 0, g = 0, b = 0;

        if (xmlFlag) {
            i = 17;
        }
        else if (jsonFlag){
            i=26;
        }
        String value = "";
        for (int j = 0; j < 3; j++) {
            while (sCurrentLine.charAt(i) != ',' && sCurrentLine.charAt(i) != ']') {
                value += sCurrentLine.charAt(i);
                i++;
            }
            if (j == 0) {
                r = Integer.valueOf(value);
            } else if (j == 1) {
                g = Integer.valueOf(value);
            } else if (j == 2) {
                b = Integer.valueOf(value);
            }
            value = "";
            i += 3;
        }
        return new Color(r, g, b);
    }


}
