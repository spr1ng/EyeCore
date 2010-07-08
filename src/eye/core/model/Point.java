package eye.core.model;

/**
 *
 * @author gpdribbler, spr1ng
 * @version $Id: Point.java 52 2010-07-07 06:10:05Z spr1ng $
 */
public class Point {

    private int x;
    private int y;   
    
    public Point() {
    }
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Делает координаты точки относительными */
    public void computeRelativeXY(int imageWidth, int imageHeight){
        x = x * 100 / imageWidth;
        y = y * 100 / imageHeight;
    }

    public int getDistanceTo(Point p) {
        int result =  (x - p.getX()) * (x - p.getX());
        result += (y - p.getY()) * (y - p.getY());
        return (int) Math.sqrt(result);
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
}
