package C;

import M.*;
import M.Shape;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller {

    public VBox drawing;
    public Canvas updateCanvas;
    private M.Shape shape;
    private String type;
    @FXML
    private Canvas canvas = new Canvas(600, 360);

    @FXML
    protected Button Line;

    @FXML
    protected Button Circle;

    @FXML
    protected Button Triangle;

    @FXML
    protected Button Rectangle;

    @FXML
    protected Button Ellipse;

    @FXML
    protected Button Square;

    @FXML
    protected TextField Height;

    @FXML
    protected TextField Width;

    @FXML
    protected TextField Radius;

    @FXML
    protected TextField Length;

    @FXML
    protected Button Save;

    @FXML
    protected Button Refresh;

    @FXML
    protected Button Load;

    @FXML
    protected Button Redo;

    @FXML
    protected Button Undo;

    @FXML
    protected Button Copy;

    @FXML
    protected Button AddShape;

    @FXML
    protected Button RemoveShape;

    @FXML
    protected Button UpdateShape;
    @FXML
    private TextField saveText;

    @FXML
    private TextField loadText;
    @FXML
    private ColorPicker bColor;

    @FXML
    private ColorPicker fColor;

    @FXML
    private ComboBox<String> listOfUserShapes = new ComboBox<>();

    @FXML
    private Button loadPlugins;

    @FXML
    private TextField pathOfJar;

    @FXML
    private Button newPlugIn;

    @FXML
    private TextField updatendex;

    @FXML
    private Canvas tmpCanvas;


    @FXML
    private Pane updatePane;

    @FXML
    private Button copy;

    @FXML
    private Button move;

    @FXML
    private Button resize;


    private int[] shapeIndex = new int[7];/*  rectangle  ,ellipse ,circle ,square , triangle ,line ,  new shape*/
    private String classPlugInName = new String();
    private String classSuffix = ".class";
    private M.Shape[] sh;
    private int counterIndex = 0;
    private MouseEvent mouseEvent;
    private ActionEvent event;
    private javafx.scene.paint.Color border;
    private javafx.scene.paint.Color fill;
    private DrawingEngine engine = new DrawingEngineImp();
    private Point secondPosition = new Point();
    private Point mouseCoordinates = new Point();
    private Point thirdPosition = new Point();
    private int no = 0, click = 0;
    private String name = new String();
    private ShapeFactory factory = new ShapeFactory();
    private String pathToJar = new String();
    private boolean flag = true, triFlag = false;
    private int length1 = 0, length2 = 0;
    private LinkedList<Integer> indexOfselectedShape = new LinkedList();
    private LinkedList<ToggleButton> buttons = new LinkedList();
    private MouseEvent event1;
    private boolean moveFlag = false;
    private boolean resizeFlag = false;
    private boolean plugFlag = false;
    private int moveIndex;

    private void clickCounter() {


        if (type.equals("Line")) {

            click = 0;
            no = 1;

        } else if (type.equals("Triangle")) {

            click = 0;
            no = 2;

        } else if (type.equals("Square")) {

            click = 0;
            no = 0;

        } else if (type.equals("Circle")) {

            click = 0;
            no = 0;

        } else if (type.equals("Ellipse")) {

            click = 0;
            no = 0;

        } else if (type.equals("Rectangle")) {

            click = 0;
            no = 0;
        } else if (type.equals("newPlugIn")) {

            click = 0;
            no = 0;
        }

    }


    public void searchForClass(JarFile jarFilePath)
            throws SecurityException, ClassNotFoundException {
        Enumeration<JarEntry> entryEnum = jarFilePath.entries();
        while (entryEnum.hasMoreElements()) {
            doSearchClassName(entryEnum.nextElement());
        }
    }

    private void doSearchClassName(JarEntry entry) throws SecurityException, ClassNotFoundException {
        name = entry.getName();
        if (name.endsWith(classSuffix)) {
            /**
             * Populate the class name
             */
            this.classPlugInName = name.replaceAll("/", ".").substring(0, name.lastIndexOf("."));
            /*check the name to add a new button*/
            if (name != "Circle" && name != "Triangle" && name != "Square" &&
                    name != "Rectangle" && name != "Line" && name != "Ellipse") {
                newPlugIn.setText(name);
                newPlugIn.setVisible(true);

            }
        }
    }


    @FXML
    void operationButtons(ActionEvent event) { //undo & redo & update & newplugin the handling method

        if (event.getSource() == this.Undo) {
            engine.undo();
        } else if (event.getSource() == this.Redo) {
            engine.redo();
        } else if (event.getSource() == this.UpdateShape) {

            mouseCoordinates = new Point();
            secondPosition = new Point();
            boolean update = false;
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).isSelected()) {
                    engine.historyGetter().removeLast();
                    engine.listOFShapesGetter().removeLast();
                    engine.updateShape(engine.getShapes()[i], shape);
                    break;
                }
            }
        } else if (event.getSource() == this.RemoveShape) {
            for (int i = 0; i < buttons.size(); i++) {
                if (buttons.get(i).isSelected()) {
                    engine.removeShape(engine.getShapes()[i]);
                    drawing.getChildren().remove(i);
                    buttons.remove(i);
                }
            }
            updatingViewList();
        } else if (event.getSource() == this.Refresh) {
            engine.refresh(new Drawer(canvas.getGraphicsContext2D()));


        } else if (event.getSource() == this.loadPlugins) {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open File");
            pathToJar = chooser.showOpenDialog(new Stage()).getPath();

//             pathToJar = pathOfJar.getText();
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(pathToJar);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = new URL[0];
            try {
                urls = new URL[]{new URL("jar:file:" + pathToJar + "!/")};
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            boolean flag = true;
            while (e.hasMoreElements() && flag) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                flag = false;
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                this.classPlugInName = className.substring(2);
                try {
                    Class c = cl.loadClass(className);
                    engine.installPlugIn(c);

                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }


            }
            newPlugIn.setText(this.classPlugInName);
            newPlugIn.setVisible(true);

        }
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        engine.refresh(new Drawer(canvas.getGraphicsContext2D()));
        updatingViewList();

    }

    public java.util.List<Class<? extends Shape>> getList() { // getting the supported shapes
        return engine.getSupportedShapes();
    }

    @FXML
    void getMouseDraddedPosition(MouseEvent event) { //the drag event to draw in the canvas
        if (!plugFlag) {
            shape = factory.createAShape(type);
        }

        if (flag || moveFlag) {
            mouseCoordinates.x = (int) event.getSceneX();
            mouseCoordinates.y = (int) event.getSceneY();
            flag = false;
            if (moveFlag) {
                shape.setPosition(mouseCoordinates);
                if (((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name")==0.0){
                    Map<String,Double> tmp =new HashMap<>();
                    double distanceX = mouseCoordinates.x-((Shape) engine.getShapes()[moveIndex]).getPosition().x;
                    double distanceY = mouseCoordinates.y-((Shape) engine.getShapes()[moveIndex]).getPosition().y;
                    tmp.put("Name",((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name"));
                    tmp.put("x",((Shape) engine.getShapes()[moveIndex]).getProperties().get("x")+distanceX);
                    tmp.put("y",((Shape) engine.getShapes()[moveIndex]).getProperties().get("y")+distanceY);
                    shape.setProperties(tmp);
                }else if (((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name")==1.0){
                    Map<String,Double> tmp =new HashMap<>();
                    double distanceX = mouseCoordinates.x-((Shape) engine.getShapes()[moveIndex]).getPosition().x;
                    double distanceY = mouseCoordinates.y-((Shape) engine.getShapes()[moveIndex]).getPosition().y;
                    tmp.put("Name",((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name"));
                    tmp.put("x",((Shape) engine.getShapes()[moveIndex]).getProperties().get("x")+distanceX);
                    tmp.put("x2",((Shape) engine.getShapes()[moveIndex]).getProperties().get("x2")+distanceX);
                    tmp.put("y",((Shape) engine.getShapes()[moveIndex]).getProperties().get("y")+distanceY);
                    tmp.put("y2",((Shape) engine.getShapes()[moveIndex]).getProperties().get("y2")+distanceY);
                    shape.setProperties(tmp);
                }
                else {
                    shape.setProperties(((Shape) engine.getShapes()[moveIndex]).getProperties());
                }
                shape.setColor(((Shape) engine.getShapes()[moveIndex]).getColor());
                shape.setFillColor(((Shape) engine.getShapes()[moveIndex]).getFillColor());
                tmpCanvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                shape.draw(new Drawer(tmpCanvas.getGraphicsContext2D()));
            }
        } else {
            if (triFlag) {
                thirdPosition.x = Integer.valueOf(Height.getText());
                thirdPosition.y = Integer.valueOf(Width.getText());
            }
            Map properities = new HashMap<String, Double>();
            secondPosition.x = (int) event.getSceneX();
            secondPosition.y = (int) event.getSceneY();
            if (resizeFlag) {
                shape.setPosition(((Shape) engine.getShapes()[moveIndex]).getPosition());
            } else {
                shape.setPosition(mouseCoordinates);
            }
            length1 = Math.abs(mouseCoordinates.x - secondPosition.x);
            length2 = Math.abs(mouseCoordinates.y - secondPosition.y);
            SettingProperities(properities);
            shape.setProperties(properities);
            click = 0;
            tmpCanvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            shape.draw(new Drawer(tmpCanvas.getGraphicsContext2D()));
        }
   }


    @FXML
    void getMouseReleasedPosition(MouseEvent event) { // releasing event to draw in the canvas
        plugFlag = false;
        if (moveFlag) {
            tmpCanvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            engine.updateShape(engine.getShapes()[moveIndex], shape);
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            updatingViewList();
            engine.refresh(new Drawer(canvas.getGraphicsContext2D()));
            moveFlag = false;
            flag=true;
        } else {
            if (length1 != 0) {
                tmpCanvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                Map properities = new HashMap<String, Double>();
                secondPosition.x = (int) event.getSceneX();
                secondPosition.y = (int) event.getSceneY();
                length1 = Math.abs(mouseCoordinates.x - secondPosition.x);
                length2 = Math.abs(mouseCoordinates.y - secondPosition.y);
                SettingProperities(properities);
                shape.setProperties(properities);
                click = 0;
                if (resizeFlag) {
                    tmpCanvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    engine.updateShape(engine.getShapes()[moveIndex], shape);
                    resizeFlag = false;
                    flag = true;
                    canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    updatingViewList();
                    engine.refresh(new Drawer(canvas.getGraphicsContext2D()));
                } else {
                    engine.addShape(shape);
                }
                updatingViewList();
                shape.draw(new Drawer(canvas.getGraphicsContext2D()));
                mouseCoordinates = new Point();
                secondPosition = new Point();
                thirdPosition = new Point();
                flag = true;
                length2 = 0;
                length1 = 0;
            }
        }
    }

    private void updatingViewList() { //update the toggle buttons of the list view
        engine.findShapeName();
        drawing.getChildren().clear();
        int i = 0;
        buttons.clear();
        for (String name : engine.nameOfShapeGetter()) {
            ToggleButton addingShape = new ToggleButton();
            addingShape.setOnAction(event1 -> {
                if (addingShape.isSelected()) {
                    //add to list
                    int index = addingShape.getText().indexOf(' ');
                    indexOfselectedShape.add(Integer.valueOf(addingShape.getText().substring(0, index)));
                } else {
                    //remove
                    int index = addingShape.getText().indexOf(' ');
                    indexOfselectedShape.remove(Integer.valueOf(addingShape.getText().substring(0, index)));
                }
            });
            buttons.add(addingShape);
            addingShape.setText(String.valueOf(i) + " )" + name);
            addingShape.setPrefSize(150, 25);
            drawing.getChildren().add(addingShape);
            i++;
        }
    }

    private void SettingProperities(Map<String, Double> properities) { //setting the shapes properities
        if (type.equals("Line")) {
            properities.put("Name", 0.0);
            properities.put("x", (double) secondPosition.x);
            properities.put("y", (double) secondPosition.y);
            colourShapes(this.event);
        } else if (type.equals("Triangle")) {
            properities.put("Name", 1.0);
            properities.put("x", (double) secondPosition.x);
            properities.put("y", (double) secondPosition.y);
            properities.put("x2", (double) thirdPosition.x);
            properities.put("y2", (double) thirdPosition.y);
            colourShapes(this.event);
        } else if (type.equals("Square")) {
            properities.put("Length", (double) length1);
            properities.put("Name", 2.0);
            colourShapes(this.event);
        } else if (type.equals("Circle")) {
            properities.put("raduis", (double) length1);
            properities.put("Name", 3.0);
            colourShapes(this.event);
        } else if (type.equals("Ellipse")) {
            properities.put("Width", (double) length1);
            properities.put("Height", (double) length2);
            properities.put("Name", 4.0);
            colourShapes(this.event);
        } else if (type.equals("Rectangle")) {
            properities.put("Width", (double) length1);
            properities.put("Length", (double) length2);
            properities.put("Name", 5.0);
            colourShapes(this.event);
        } else if (type.equals("newPlugIn")) {
            properities.put("raduis", (double) length1);
            properities.put("Width", (double) length1);
            properities.put("Length", (double) length2);
            properities.put("Height", (double) length2);
            properities.put("Name", 6.0);
            colourShapes(this.event);
        }
    }

    @FXML
    public void CreateShape(ActionEvent event) { //setting the shapes type to create an object
        factory = new ShapeFactory();
        if (event.getSource() == this.Line) {
            shape = factory.createAShape("Line");
            type = "Line";
            clickCounter();
            shapeIndex[0]++;
            listOfUserShapes.getItems().add(counterIndex++ + "Line" + shapeIndex[0]);
        } else if (event.getSource() == this.Triangle) {
            shape = factory.createAShape("Triangle");
            type = "Triangle";
            triFlag = true;
            clickCounter();
            this.Width.setVisible(true);
            this.Height.setVisible(true);
            shapeIndex[1]++;
            listOfUserShapes.getItems().add(counterIndex++ + "Triangle" + shapeIndex[1]);
        } else if (event.getSource() == this.Square) {
            shape = factory.createAShape("Square");
            type = "Square";
            clickCounter();
            shapeIndex[2]++;
            listOfUserShapes.getItems().add(counterIndex++ + "Square" + shapeIndex[2]);
        } else if (event.getSource() == this.Circle) {
            type = "Circle";
            clickCounter();
            shapeIndex[3]++;
            listOfUserShapes.getItems().add(counterIndex++ + " Circle" + shapeIndex[3]);
        } else if (event.getSource() == this.Ellipse) {
            shape = factory.createAShape("Ellipse");
            type = "Ellipse";
            clickCounter();
            shapeIndex[4]++;
            listOfUserShapes.getItems().add(counterIndex++ + "Ellipse" + shapeIndex[4]);
        } else if (event.getSource() == this.Rectangle) {
            shape = factory.createAShape("Rectangle");
            type = "Rectangle";
            clickCounter();
            shapeIndex[5]++;
            listOfUserShapes.getItems().add(counterIndex++ + "Rectangle" + shapeIndex[5]);
        } else if (event.getSource() == newPlugIn) {
            plugFlag = true;
            try {
                shape = engine.getSupportedShapes().get(engine.getSupportedShapes().size() - 1).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            type = "newPlugIn";
            this.Width.setVisible(true);
            this.Height.setVisible(true);
            this.Radius.setVisible(true);
            this.Length.setVisible(true);
            shapeIndex[6]++;
            listOfUserShapes.getItems().add(counterIndex++ + name + shapeIndex[6]);

        }

    }

    @FXML
    private void colourShapes(ActionEvent event) {
        shape.setColor(colorConveter(border));
        shape.setFillColor(colorConveter(fill));
    }

    @FXML
    void textFieldActions(ActionEvent event) {


    }

    @FXML
    void loadingShapes(ActionEvent event) { //the load handler method
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        engine.load(chooser.showOpenDialog(new Stage()).getPath());
//        engine.load(loadText.getText());
        engine.refresh(new Drawer(canvas.getGraphicsContext2D()));
        updatingViewList();
    }

    @FXML
    void savingShapes(ActionEvent event) { //the saving handler method
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        engine.save(chooser.showSaveDialog(new Stage()).getPath());
    }

    private java.awt.Color colorConveter(javafx.scene.paint.Color color) { //converter method to handle the colors
        java.awt.Color rgb = new java.awt.Color(
                (float) color.getRed(),
                (float) color.getGreen(),
                (float) color.getBlue(),
                (float) color.getOpacity()
        );
        return rgb;
    }

    @FXML
    void borderColor(ActionEvent event) {
        border = bColor.getValue();
    }

    @FXML
    void fillColor(ActionEvent event) {
        fill = fColor.getValue();
    }

    @FXML
    void copyShape(ActionEvent event) throws CloneNotSupportedException { //copy handler method
        int i = 0;
        Shape copy;
        for (ToggleButton button : buttons) {
            if (button.isSelected()) {
                engine.addShape((Shape) engine.getShapes()[i].clone());

            }
            i++;
        }
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        engine.refresh(new Drawer(canvas.getGraphicsContext2D()));
        updatingViewList();
    }

    private String getName(double c) { //getting the shapes type by the representing type
        String type = null;
        if (c == 0.0) {
            type = "Line";
        } else if (c == 1.0) {
            type = "Triangle";
        } else if (c == 2.0) {
            type = "Square";
        } else if (c == 3.0) {
            type = "Circle";
        } else if (c == 4.0) {
            type = "Ellipse";
        } else if (c == 5.0) {
            type = "Rectangle";
        }
        return type;
    }

    @FXML
    void movingShapes(ActionEvent event) { //moving handler event
        for (moveIndex = 0; moveIndex < buttons.size(); moveIndex++) {
            if (buttons.get(moveIndex).isSelected()) {
                break;
            }
        }

        if (((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name") == 6.0) {
            try {
                shape = (Shape) ((Shape) engine.getShapes()[moveIndex]).clone();
                plugFlag = true;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            type = getName(((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name"));
        }
        moveFlag = true;

    }

    @FXML
    void resizing(ActionEvent event) { //resizing handler event
        for (moveIndex = 0; moveIndex < buttons.size(); moveIndex++) {
            if (buttons.get(moveIndex).isSelected()) {
                break;
            }
        }
        if (((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name") == 6.0) {
            try {
                shape = (Shape) ((Shape) engine.getShapes()[moveIndex]).clone();
                plugFlag = true;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } else {
            type = getName(((Shape) engine.getShapes()[moveIndex]).getProperties().get("Name"));
            plugFlag = false;
        }
        resizeFlag = true;
        flag = false;
    }

}




