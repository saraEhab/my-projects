package M;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by TOSIBA-PC on 24/10/2017.
 */
public class Triangle implements Shape {

    private Point mouseClickPostion1 =new Point();
    private Point mouseClickPostion2 =new Point();
    private Point mouseClickPostion3 =new Point();
    private Map<String, Double> shapeProperties ;
    private Color borderColour;
    private Color fillColour;

    public Triangle (){
        shapeProperties=new HashMap<>();
        shapeProperties.put("Length1", 0.0);
        shapeProperties.put("Length2", 0.0);
        shapeProperties.put("Length3", 0.0);
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
        int x[]={(int) mouseClickPostion1.getX(), shapeProperties.get("x").intValue(), shapeProperties.get("x2").intValue()};
        int y[]={(int) mouseClickPostion1.getY(), shapeProperties.get("y").intValue(), shapeProperties.get("y2").intValue()};
        ((Graphics2D) canvas).setColor(getFillColor());
        ((Graphics2D) canvas).fillPolygon(x,y,3);
        ((Graphics2D) canvas).setStroke(new BasicStroke(2));
        ((Graphics2D) canvas).setColor(getColor());
        ((Graphics2D) canvas).drawPolygon(x,y,3);

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Shape newShape = new Triangle();
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
