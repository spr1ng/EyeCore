/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eye.core.model;

import eye.core.math.MathExt;

/**
 *
 * @author spr1ng
 * @version $Id: Edge.java 122 2010-07-21 04:30:55Z spr1ng $
 */
public class Edge {

    /** Кол-во знаков после запятой, домустимое в длине ребра */
    public static final int EDGE_ROUNDING = 1;
    /** Кол-во знаков после запятой, домустимое в угле между ребрами */
    public static final int ANGLE_ROUNDING = 1;
    /** Минимальный значимый угол между двумя ребрами */
    public static final int ANGLE_MIN = 12;
    public static final int MAX_SIZE = 6;//PENDING:
    private Point point1;
    private Point point2;

    public Edge() {
    }

    public Edge(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public Point getPoint1() {
        return point1;
    }

    public void setPoint1(Point point1) {
        this.point1 = point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public void setPoint2(Point point2) {
        this.point2 = point2;
    }

    public double size() {
        return MathExt.round(Math.hypot(point1.getRelativeX() - point2.getRelativeX(),
                point1.getRelativeY() - point2.getRelativeY()), EDGE_ROUNDING);
    }

    public boolean isConnected(Edge edge) {
        if (edge.getPoint1().equals(point1) || edge.getPoint1().equals(point2)
                || edge.getPoint2().equals(point1) || edge.getPoint2().equals(point2))
             return true;
        else return false;
    }

    public Point getSharedPoint(Edge anotherEdge) {
        if (anotherEdge.getPoint1().equals(point1) || anotherEdge.getPoint2().equals(point1))
             return point1;
        if (anotherEdge.getPoint1().equals(point2) || anotherEdge.getPoint2().equals(point2))
             return point2;
        else return null;
    }

    public double getAngle(Edge anotherEdge) {
        Point sharedPoint = getSharedPoint(anotherEdge);
        if (sharedPoint == null) return 0.0;

        Point p1 = !point1.equals(sharedPoint) ? point1 : point2;
        Point p2 = !anotherEdge.getPoint1().equals(sharedPoint) ? anotherEdge.getPoint1() : anotherEdge.getPoint2();

        double dx1 = p1.getRelativeX() - sharedPoint.getRelativeX();
        double dy1 = p1.getRelativeY() - sharedPoint.getRelativeY();
        double dx2 = p2.getRelativeX() - sharedPoint.getRelativeX();
        double dy2 = p2.getRelativeY() - sharedPoint.getRelativeY();

        double a = dx1 * dy2 - dy1 * dx2;
        double b = dx1 * dx2 + dy1 * dy2;

        double θ = Math.atan(a / b);
        return MathExt.round(Math.toDegrees(θ), ANGLE_ROUNDING);
    }
}
