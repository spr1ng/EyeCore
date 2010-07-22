package eye.core.model.ext;

import eye.core.math.MathExt;
import eye.core.model.Edge;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import eye.core.model.Point;
import eye.core.model.Image;
import eye.core.model.exception.IIException;
import java.util.HashMap;
import java.util.Iterator;
import static eye.core.util.InetUtils.*;

/**
 *
 * @author gpdribbler, spr1ng
 * @version $Id: ImageFactory.java 54 2010-07-08 02:23:12Z stream $
 */
public class ImageFactory {

    private BufferedImage bimg;
    private ArrayList<Point> points;

    public void load(File file) throws IOException {
        bimg = ImageIO.read(file);
    }

    public void load(URL url) throws IOException {
        //Зададим-ка мы проверочку на таймаут
        if (isReachable(url.toString(), 3000)) {
            bimg = ImageIO.read(url);
        } else {
            throw new IIException("Connection timeout");
        }
    }

    /**
     * Создает BufferedImage из потока
     * @param in входящий поток
     * @throws IOException
     */
    public void load(InputStream in) throws IOException {
        bimg = ImageIO.read(in);
    }

    public void saveToFile(BufferedImage bimg, String fileName) throws IOException {
        ImageIO.write(bimg, getExtension(fileName), new File(fileName));
    }

    public void saveToFile(String fileName) throws IOException {
        saveToFile(bimg, fileName);
    }

    /** 
     * Saves to output stream
     * @param imgExtension
     * @param out
     * @throws IOException
     */
    public void saveToOutputStream(BufferedImage bimg, String imgExtension, OutputStream out) throws IOException {
        ImageIO.write(bimg, imgExtension, out);
    }

    /** 
     * Saves to output stream
     * @param imgExtension
     * @param out
     * @throws IOException
     */
    public void saveToOutputStream(String imgExtension, OutputStream out) throws IOException {
        saveToOutputStream(bimg, imgExtension, out);
    }

    /**
     * Рисует области вокруг точек белым цветом
     * @param points
     */
    public void drawPoints(List<Point> points) {
        drawPoints(bimg, points, Color.WHITE);
    }

    /**
     * Рисует области вокруг точек выбранным цветом
     * @param points
     */
    public void drawPoints(List<Point> points, Color color) {
        drawPoints(bimg, points, color);
    }

    /**
     * Рисует области вокруг точек выбранным цветом
     * @param points
     * @param color
     */
    public void drawPoints(BufferedImage bimg, List<Point> points, Color color) {
        int d = 12;
        Graphics2D graphics = bimg.createGraphics();
        for (Point point : points) {
            graphics.setColor(color);
            graphics.drawOval(point.getX() - (d / 2), point.getY() - (d / 2), d, d);//PENDING:
        }
    }

    public void drawAndSaveRealImage(String fileName) throws IOException { //DEBUG
        drawAndSaveRealImage(bimg, fileName);
    }

    public void drawAndSaveRealImage(BufferedImage bimg, String fileName) throws IOException { //DEBUG
        drawAndSaveRealImage(bimg, seekPoints(), fileName, Color.BLACK);
    }

    public void drawAndSaveRealImage(List<Point> points, String fileName) throws IOException { //DEBUG
        drawAndSaveRealImage(bimg, points, fileName, Color.BLACK);
    }

    public void drawAndSaveRealImage(List<Point> points, String fileName, Color color) throws IOException { //DEBUG
        drawAndSaveRealImage(bimg, points, fileName, color);
    }

    public void drawAndSaveRealImage(BufferedImage bimg, List<Point> points, String fileName, Color color) throws IOException { //DEBUG
        this.bimg = bimg;
        drawPoints(points, color);
        saveToFile(System.getProperty("user.home") + "/" + fileName);
    }

    /**
     *
     * @param edges
     * @param fileName
     * @param width
     * @param height
     * @throws IOException
     */
    public void drawAndSaveMetaImage(List<Edge> edges, String fileName, int width, int height) throws IOException { //DEBUG
        BufferedImage bimg2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2 = bimg2.createGraphics();
//        graphics2.setColor(Color.WHITE);

        int d = 4;
        for (Edge edge : edges) {
            Point p1 = edge.getPoint1();
            Point p2 = edge.getPoint2();

            int x1 = (int) (10 * MathExt.round(p1.getRelativeX(), 1));
            int y1 = (int) (10 * MathExt.round(p1.getRelativeY(), 1));
            int x2 = (int) (10 * MathExt.round(p2.getRelativeX(), 1));
            int y2 = (int) (10 * MathExt.round(p2.getRelativeY(), 1));
//            System.out.println("(" + x1 + "; " + y1 + ") --> (" + x2 + "; " + y2 + ")"); //DELME: 

            graphics2.setColor(Color.RED);
            graphics2.drawOval(x1 - (d / 2), y1 - (d / 2), d, d);
            graphics2.fillOval(x1 - (d / 2), y1 - (d / 2), d, d);
            graphics2.drawOval(x2 - (d / 2), y2 - (d / 2), d, d);
            graphics2.fillOval(x2 - (d / 2), y2 - (d / 2), d, d);
            graphics2.setColor(Color.GREEN);
            graphics2.drawLine(x1, y1, x2, y2);
        }

        String filePath = System.getProperty("user.home") + "/" + fileName;
        ImageIO.write(bimg2, getExtension(fileName), new File(filePath));
    }

    public void drawAndSaveMetaImage(List<Edge> edges, String fileName) throws IOException { //DEBUG
        drawAndSaveMetaImage(edges, fileName, 1600, 1200);
    }

    public ArrayList<Point> seekPoints(InputStream in) throws IOException {
        load(in);
        return seekPoints();
    }

    public ArrayList<Point> seekPoints() {
        try {
            Kernel kernel = new Kernel(3, 3, new float[]{1f / 9f, 1f / 9f, 1f / 9f,
                        1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f});
            BufferedImageOp op = new ConvolveOp(kernel);
            bimg = op.filter(bimg, null);
        } catch (Exception e) {
        }

        points = new ArrayList<Point>();
        System.out.print("---> Seeking points ");

        //TODO: проверить значение декремента
        int w = bimg.getWidth() - 4;
        int h = bimg.getHeight() - 4;
        for (int i = 2; i < w; i++) {
            for (int j = 2; j < h; j++) {
                Point point = new Point(i, j);
                if (isBorder(point, bimg, Image.DEPTH)) {
                    point.computeRelativeXY(w, h);//PENDING: добавляем относительные координаты
                    points.add(point);
                }
            }//for
        }
        System.out.print("--> " + points.size());
        //TODO: увеличить эффективность за счет просмотра только ближайших точек
        for (int i = 0; i < points.size(); i++) {
            Point point1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point point2 = points.get(j);
                if (point1.getAbsoluteDistanceTo(point2) < Image.DISPERSION) {//PENDING:
                    points.remove(j);
                    j--;
                }
            }
        }

        //Убираем точки, не принадлежащие скоплениям
        int idx = 0;
        for (Iterator<Point> it = points.iterator(); it.hasNext();) {
            it.next();
            if (!isValidPoint(idx, points)) {
                it.remove();
            } else {
                idx++;
            }
        }

        System.out.println(" -> " + points.size());

        return points;

    }//seekPoints()

    public boolean isValidPoint(int pointIdx, List<Point> points) {
        //Убираем точки, не принадлежащие скоплениям
        int neighbourPoints = 0;
        Point point1 = points.get(pointIdx);
        for (int j = 0; j < points.size(); j++) {
            Point point2 = points.get(j);
            if (!point1.equals(point2)) {
                if (point1.getAbsoluteDistanceTo(point2) < Point.MAX_DISTANCE) {
                    neighbourPoints++;
                }
            }
        }
        if (neighbourPoints < Point.MIN_NEIGHBOURS) {
            return false;
        }
        return true;
    }

    public List<Edge> seekEdges(List<Point> points) throws IIException{
        if (points.size() > 10000) throw new IIException("Too many points (" + points.size() + ")");
        List<Edge> edges = new ArrayList<Edge>();
        System.out.print("---> Seeking edges ");
        for (int i = 0; i < points.size(); i++) {
            Point point1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point point2 = points.get(j);
                //Находим расстояние от одной точки до другой (ищем длину ребра)
                double edgeSize = point1.getRelativeDistanceTo(point2);
                //Если это ребро подходящей длины
                if (edgeSize > 0 && edgeSize <= Edge.MAX_SIZE) {
                    //Добавляем его в список ребер
                    edges.add(new Edge(point1, point2));
                }
            }
        }
        System.out.println("--> " + edges.size());

        return edges;
    }

    public List<Edge> seekEdges() throws IIException {
        return seekEdges(points);
    }

    /**
     * HashMap (edgeSize, edgeQuantity)
     * @param edges
     * @return HashMap
     */
    public HashMap<Double, Integer> seekEdgeMap(List<Edge> edges) {
        HashMap<Double, Integer> edgeMap = new HashMap<Double, Integer>();
        for (Edge edge : edges) {
            double edgeSize = edge.size();
            Integer edgeQty = edgeMap.get(edgeSize);
            if (edgeQty == null) edgeQty = 0;
            edgeMap.put(edgeSize, ++edgeQty);
        }
        return edgeMap;
    }
    
    /**
     * HashMap (angleSize, angleQuantity)
     * @param edges
     * @return
     */
    public HashMap<Double, Integer> seekAngleMap(List<Edge> edges) {
        HashMap<Double, Integer> angleMap = new HashMap<Double, Integer>();
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                if (edges.get(i).isConnected(edges.get(j))) {
                    double angle = edges.get(i).getAngle(edges.get(j));
//                    angle = Math.abs(angle);
                    if (Math.abs(angle) > Edge.ANGLE_MIN){
                        Integer angleQty = angleMap.get(angle);
                        if (angleQty == null) angleQty = 0;
                        angleMap.put(angle, ++angleQty);
                    }
                }
            }
        }
        return angleMap;
    }

    public void produceMetaImage(Image image) throws IOException {
        load(new URL(image.getUrl()));
        image.setResolution(bimg.getWidth(), bimg.getHeight());
        if (image.hasValidSize()) {
            List<Edge> edges = seekEdges(seekPoints());
            System.out.print("---> Seeking maps ");
            HashMap<Double, Integer> edgeMap = seekEdgeMap(edges);
            image.setEdgeMap(edgeMap);
            System.out.print("--> edge map [" + edgeMap.size() + "] ");
            HashMap<Double, Integer> angleMap = seekAngleMap(edges);
            image.setAngleMap(angleMap);
            System.out.println("--> angle map [" + angleMap.size() + "] ");
        } else {
            throw new IIException("Invalid image size.");
        }
    }

    /**
     * Gets file extension
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        return fileName.replaceFirst(".*\\.", "");
    }

    public int getRGBDifference(int rgb1, int rgb2) {
        int red1 = (rgb1 & 0x00ff0000) >> 16;
        int green1 = (rgb1 & 0x0000ff00) >> 8;
        int blue1 = (rgb1 & 0x000000ff);

        int red2 = (rgb2 & 0x00ff0000) >> 16;
        int green2 = (rgb2 & 0x0000ff00) >> 8;
        int blue2 = (rgb2 & 0x000000ff);

        return (int) Math.sqrt((red1 - red2) * (red1 - red2) + (green1 - green2)
                * (green1 - green2) + (blue1 - blue2) * (blue1 - blue2));
    }//getDifference

    public boolean isBorder(Point point, BufferedImage image, int limit) {
        int diff = 0;
        int rgb1 = image.getRGB(point.getX(), point.getY());
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int rgb2 = image.getRGB(point.getX() + i, point.getY() + j);
                diff += getRGBDifference(rgb1, rgb2);
            }
        }
        diff /= 8;
        if (diff > limit) {
            return true;
        } else {
            return false;
        }
    }//isBorderPixel


    public static void main(String[] args) throws IOException {
        /*List<Edge> edges = new ArrayList<Edge>();
        Point p1 = new Point(0, 0); p1.computeRelativeXY(200, 200);
        Point p2 = new Point(1, 0); p2.computeRelativeXY(200, 200);
        Point p3 = new Point(2, -1); p3.computeRelativeXY(200, 200);
        edges.add(new Edge(p1,p2));
        edges.add(new Edge(p2,p3));

        HashMap<Double, Integer> angleMap = new ImageFactory().seekAngleMap(edges);
        for (double angle : angleMap.keySet()) {
            System.out.print("angle: " + angle + "; ");
            System.out.println("qty: " + angleMap.get(angle));
        }*/

        String in = "/var/www/points10.png";
        String out1 = "out1." + getExtension(in);
        String out2 = "out2." + getExtension(in);

        ImageFactory f = new ImageFactory();
        f.load(new File(in));
        List<Point> points = f.seekPoints();
        f.drawAndSaveRealImage(points, out1);
        List<Edge> edges = f.seekEdges(points);
        f.drawAndSaveMetaImage(edges, out2);
        HashMap<Double, Integer> angleMap = f.seekAngleMap(edges);
        System.out.println(angleMap.size());
        int totalAngles = 0;
        for (double angle : angleMap.keySet()) {
            totalAngles += angleMap.get(angle);
            System.out.print("angle: " + angle + "; ");
            System.out.println("qty: " + angleMap.get(angle));
        }
        System.out.println("Total angles: " + totalAngles);
    }
}
