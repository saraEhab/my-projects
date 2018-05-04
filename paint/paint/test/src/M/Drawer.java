package M;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

import java.awt.*;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class Drawer extends Graphics2D {
    private GraphicsContext canvas ;
    double [] x,y;
    Color color;

    public Drawer(GraphicsContext canvas){
        this.canvas=canvas;
    }

    @Override
    public void draw(java.awt.Shape s) {

    }

    @Override
    public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
        return false;
    }

    @Override
    public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {

    }

    @Override
    public void drawRenderedImage(RenderedImage img, AffineTransform xform) {

    }

    @Override
    public void drawRenderableImage(RenderableImage img, AffineTransform xform) {

    }

    @Override
    public void drawString(String str, int x, int y) {

    }

    @Override
    public void drawString(String str, float x, float y) {

    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {

    }

    @Override
    public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        return false;
    }

    @Override
    public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {

    }

    @Override
    public void drawGlyphVector(GlyphVector g, float x, float y) {

    }

    @Override
    public void fill(java.awt.Shape s) {

    }

    @Override
    public boolean hit(Rectangle rect, java.awt.Shape s, boolean onStroke) {
        return false;
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        return null;
    }

    @Override
    public void setComposite(Composite comp) {

    }

    @Override
    public void setPaint(Paint paint) {

    }

    @Override
    public void setStroke(Stroke s) {

    }

    @Override
    public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {

    }

    @Override
    public Object getRenderingHint(RenderingHints.Key hintKey) {
        return null;
    }

    @Override
    public void setRenderingHints(Map<?, ?> hints) {

    }

    @Override
    public void addRenderingHints(Map<?, ?> hints) {

    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    @Override
    public Graphics create() {
        return null;
    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color c) {
        this.color = c;
    }

    private javafx.scene.paint.Color colorConveter(java.awt.Color color) {
        return javafx.scene.paint.Color.rgb (color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (color.getAlpha()/255.0));
    }

    @Override
    public void setPaintMode() {

    }

    @Override
    public void setXORMode(Color c1) {

    }

    @Override
    public Font getFont() {
        return null;
    }

    @Override
    public void setFont(Font font) {

    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        return null;
    }

    @Override
    public Rectangle getClipBounds() {
        return null;
    }

    @Override
    public void clipRect(int x, int y, int width, int height) {

    }

    @Override
    public void setClip(int x, int y, int width, int height) {

    }

    @Override
    public java.awt.Shape getClip() {
        return null;
    }

    @Override
    public void setClip(java.awt.Shape clip) {

    }

    @Override
    public void copyArea(int x, int y, int width, int height, int dx, int dy) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        canvas.setStroke(colorConveter(color));
        canvas.strokeLine((double) x1,(double) y1, (double) x2, (double) y2);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        canvas.setFill(colorConveter(color));
        canvas.fillRect((double) x ,(double) y,(double) width,(double) height);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {

    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {

    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        canvas.setStroke(colorConveter(color));
        canvas.strokeOval((double) x ,(double) y,(double) width,(double) height);
    }

    @Override
    public void fillOval(int x, int y, int width, int height) {
        canvas.setFill(colorConveter(color));
        canvas.fillOval((double) x ,(double) y,(double) width,(double) height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {

    }

    @Override
    public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {

    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
//        x= new double[xPoints.length];
//        y= new double[yPoints.length];
//        for (int i = 0 ; i < xPoints.length ; i++){
//            x[i]= (double) xPoints[i];
//            y[i]= (double) yPoints[i];
//        }
        canvas.setStroke(colorConveter(color));
        canvas.strokePolygon(x,y ,nPoints );
    }

    @Override
    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        x= new double[xPoints.length];
        y= new double[yPoints.length];
        for (int i = 0 ; i < xPoints.length ; i++){
            x[i]= (double) xPoints[i];
            y[i]= (double) yPoints[i];
        }
        canvas.setFill(colorConveter(color));
        canvas.fillPolygon(x,y,nPoints);
    }

    @Override
    public void translate(double tx, double ty) {

    }

    @Override
    public void rotate(double theta) {

    }

    @Override
    public void rotate(double theta, double x, double y) {

    }

    @Override
    public void scale(double sx, double sy) {

    }

    @Override
    public void shear(double shx, double shy) {

    }

    @Override
    public void transform(AffineTransform Tx) {

    }

    @Override
    public void setTransform(AffineTransform Tx) {

    }

    @Override
    public AffineTransform getTransform() {
        return null;
    }

    @Override
    public Paint getPaint() {
        return null;
    }

    @Override
    public Composite getComposite() {
        return null;
    }

    @Override
    public void setBackground(Color color) {

    }

    @Override
    public Color getBackground() {
        return null;
    }

    @Override
    public Stroke getStroke() {
        return null;
    }

    @Override
    public void clip(Shape s) {

    }

    @Override
    public FontRenderContext getFontRenderContext() {
        return null;
    }
}

