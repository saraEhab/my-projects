package M;

public class ShapeFactory {

    public Shape createAShape(String shapeType){
        Shape shape;
        if (shapeType.equals("Line")) {
            shape = new Line();
            return shape;
        } else if (shapeType.equals("Triangle")) {
            shape = new Triangle();
            return shape;

        } else if (shapeType.equals("Square")) {
            shape = new Square();
            return shape;

        } else if (shapeType.equals("Circle")) {
            shape = new Circle();
            return shape;

        } else if (shapeType.equals("Ellipse")) {
            shape = new Ellipse();
            return shape;

        } else if (shapeType.equals("Rectangle")) {
            shape = new Rectangle();
            return shape;
        }

        return null;
    }
}
