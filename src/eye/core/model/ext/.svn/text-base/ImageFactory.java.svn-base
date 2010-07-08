package eye.core.model.ext;

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
import static eye.core.util.InetUtils.*;

/**
 *
 * @author gpdribbler, spr1ng
 * @version $Id: ImageFactory.java 54 2010-07-08 02:23:12Z stream $
 */
public class ImageFactory {

    private final static int DEPTH = 25;
    //TODO: сделать дисперсию долевой от размеров картинки
    private final static int DISPERSION = 15;//20
    private final static int MIN_WIDTH = 128;
    private final static int MIN_HEIGHT = 128;
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

    public void saveToFile(String fileName) throws IOException {
        ImageIO.write(bimg, getExtension(fileName), new File(fileName));
    }

    /** 
     * Saves to output stream
     * @param imgExtension
     * @param out
     * @throws IOException
     */
    public void saveToOutputStream(String imgExtension, OutputStream out) throws IOException {
        ImageIO.write(bimg, imgExtension, out);
    }

    /**
     * Рисует области вокруг точек белым цветом
     * @param points
     */
    public void draw(List<Point> points) {
        draw(points, Color.WHITE);
    }

    /**
     * Рисует области вокруг точек выбранным цветом
     * @param points
     * @param color
     */
    public void draw(List<Point> points, Color color) {
        int d = 14;
        Graphics2D graphics = bimg.createGraphics();
        for (Point point : points) {
            graphics.setColor(color);
            graphics.drawOval(point.getX() - (d / 2), point.getY() - (d / 2), d, d);//PENDING:
        }
    }

    public List<Point> seekPoints(InputStream in) throws IOException {
        load(in);
        return seekPoints();
    }

    public List<Point> seekPoints() {
        try {
            Kernel kernel = new Kernel(3, 3, new float[]{1f / 9f, 1f / 9f, 1f / 9f,
                        1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f});
            BufferedImageOp op = new ConvolveOp(kernel);
            bimg = op.filter(bimg, null);
        } catch (Exception e) {
        }

        points = new ArrayList<Point>();

        //TODO: проверить значение декремента
        int w = bimg.getWidth() - 4;
        int h = bimg.getHeight() - 4;
        for (int i = 2; i < w; i++) {
            for (int j = 2; j < h; j++) {
                Point point = new Point(i, j);
                if (isBorder(point, bimg, DEPTH)) {
                    point.computeRelativeXY(bimg.getWidth(), bimg.getHeight());//PENDING: добавляем относительные координаты
                    points.add(point);
                }
            }//for
        }
        //TODO: увеличить эффективность за счет просмотра только ближайших точек
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (points.get(i).getDistanceTo(points.get(j)) < DISPERSION) {
                    points.remove(j);
                    j--;
                }
            }
        }

        return points;

    }//seekPoints()

    private boolean hasValidSize() {
        if ((bimg.getWidth() >= MIN_WIDTH) && (bimg.getHeight() >= MIN_HEIGHT)) {
            return true;
        } else {
            return false;
        }
    }//checkMeasurement

    public void produceMetaImage(Image image) throws IOException {
        load(new URL(image.getUrl()));

        if (hasValidSize()) {
            image.setPoints(seekPoints());
            System.out.println(image);
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
}
