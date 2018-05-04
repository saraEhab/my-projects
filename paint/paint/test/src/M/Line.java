package M;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TOSIBA-PC on 24/10/2017.
 */
public class Line implements Shape {

    private Point mouseClickPostion1 =new Point();
    private Map<String, Double> shapeProperties ;
    private Color borderColour;
    private Color fillColour;

    public Line(){
        shapeProperties=new HashMap<>();
//        shapeProperties.put("x", 0.0);
//        shapeProperties.put("y", 0.0);
//        this.mouseClickPostion2=mouseClickPostion2;
    }

    @Override
    public void setPosition(Point position) {
        this.mouseClickPostion1=position;
    }

    @Override
    public Point getPosition() {
        return this.mouseClickPostion1;
    }

    @Override
    public void setProperties(Map<String, Double> properties) {
        this.shapeProperties=properties;

    }

    @Override
    public Map<String, Double> getProperties() {
        return this.shapeProperties;
    }

    @Override
    public void setColor(Color color) {
        this.borderColour=color;
    }

    @Override
    public Color getColor() {
        return this.borderColour;
    }

    @Override
    public void setFillColor(Color color) {
        this.fillColour=color;
    }

    @Override
    public Color getFillColor() {
        return this.fillColour;
    }

    @Override
    public void draw(Graphics canvas) {
        ((Graphics2D) canvas).setColor(this.getFillColor());
     //   ((Graphics2D) canvas).fillArc((int) this.mouseClickPostion1.getX(),
       //         (int) this.mouseClickPostion1.getY(),0,
         //       (int) this.shapeProperties.get("Length").intValue(),
           //     180,180);
        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
        ((Graphics2D) canvas).setColor(getColor());


        ((Graphics2D) canvas).drawLine((int) this.mouseClickPostion1.getX(),
                (int) this.mouseClickPostion1.getY(),
                this.shapeProperties.get("x").intValue(),
                this.shapeProperties.get("y").intValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newShape = new Line();
        newShape.setPosition(this.getPosition());
        newShape.setColor(this.getColor());
        newShape.setFillColor(this.getFillColor());
        Map newShapeProperities = new HashMap<>();

        for (Map.Entry s: shapeProperties.entrySet()){
            newShapeProperities.put(s.getKey(), s.getValue());
        }
        newShape.setProperties(newShapeProperities);
        return newShape;
    }

}
