package M;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TOSIBA-PC on 24/10/2017.
 */
public class Circle implements Shape {

    private Point mouseClickPostion =new Point();
    private Map<String, Double> shapeProperties ;
    private Color borderColour;
    private Color fillColour;

    public Circle(){
        shapeProperties=new HashMap<>();
        shapeProperties.put("raduis", 0.0);
    }

    @Override
    public void setPosition(Point position) {
        this.mouseClickPostion=position;
    }

    @Override
    public Point getPosition() {
        return this.mouseClickPostion;
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
        ((Graphics2D) canvas).fillOval((int) this.mouseClickPostion.getX(),
                (int) this.mouseClickPostion.getY(),(int) this.shapeProperties.get("raduis").intValue(),
                (int) this.shapeProperties.get("raduis").intValue());


        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
        ((Graphics2D) canvas).setColor(getColor());
        ((Graphics2D) canvas).drawOval((int) this.mouseClickPostion.getX(),
                (int) this.mouseClickPostion.getY(),
                (int) this.shapeProperties.get("raduis").intValue(),
                (int) this.shapeProperties.get("raduis").intValue());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newShape = new Circle();
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
