package eye.core.model;

/**
 *
 * @author gpdribbler, spr1ng
 * @version $Id: Point.java 52 2010-07-07 06:10:05Z spr1ng $
 */
public class Point {

    /** Минимальное кол-во точек в скоплении */
    public static final int MIN_NEIGHBOURS = 3;
    /** Максимальное расстояние до другой точки */
    public static final int MAX_DISTANCE = 30;
    private int x;
    private int y;
    private double relativeX;
    private double relativeY;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Вычисляет относительные координаты точки */
    public void computeRelativeXY(int imageWidth, int imageHeight) {
        if (imageWidth > imageHeight) {
            relativeX = (double) 100 * x / imageWidth;
            relativeY = (double) 100 * y / imageWidth;
        } else {
            relativeX = (double) 100 * x / imageHeight;
            relativeY = (double) 100 * y / imageHeight;
        }
    }

    /**
     * @param p
     * @return
     */
    public int getAbsoluteDistanceTo(Point p) {
        int result = (x - p.getX()) * (x - p.getX());
        result += (y - p.getY()) * (y - p.getY());
        return (int) Math.sqrt(result);
    }

    public double getRelativeDistanceTo(Point p) {
        return Math.hypot(relativeX - p.getRelativeX(), relativeY - p.getRelativeY());
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

    public double getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(double relativeX) {
        this.relativeX = relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(double relativeY) {
        this.relativeY = relativeY;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point anotherPoint = (Point) obj;
            if (relativeX == anotherPoint.getRelativeX() && 
                relativeY == anotherPoint.getRelativeY()) return true;
            else return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.relativeX) ^ (Double.doubleToLongBits(this.relativeX) >>> 32));
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.relativeY) ^ (Double.doubleToLongBits(this.relativeY) >>> 32));
        return hash;
    }
}
