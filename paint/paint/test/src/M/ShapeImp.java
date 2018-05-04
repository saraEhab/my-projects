package M;

import java.awt.*;
import java.util.Map;

/**
 * Created by TOSIBA-PC on 24/10/2017.
 */
public class ShapeImp implements Shape  {
    private Point mouseClickPostion =new Point();
    private Map <String, Double> shapeProperties ;


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

    }

    @Override
    public Map<String, Double> getProperties() {
        return null;
    }

    @Override
    public void setColor(Color color) {

    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setFillColor(Color color) {

    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public void draw(Graphics canvas) {

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }
}
