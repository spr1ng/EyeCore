package eye.core.model;

import java.util.HashMap;

/**
 *
 * @author gpdribbler, spr1ng, stream
 * @version $Id: Image.java 52 2010-07-07 06:10:05Z spr1ng $
 */
public class Image implements RemoteSource{

    
    public final static int MIN_WIDTH = 128;
    public final static int MIN_HEIGHT = 128;
    public final static int DEPTH = 28;
    //TODO: сделать дисперсию долевой от размеров картинки
    public final static int DISPERSION = 10;//10;
    
    private String url;
    private int width;
    private int height;
    private HashMap<Double, Integer> edgeMap;
    private HashMap<Double, Integer> angleMap;
    
    public Image() {
    }

    public Image(String url) {
        this.url = url;
    }

    public Image(String url, HashMap<Double, Integer> edgeMap) {
        this.url = url;
        this.edgeMap = edgeMap;
    }
    
    public Image(String url, HashMap<Double, Integer> edgeMap, HashMap<Double, Integer> angleMap) {
        this.url = url;
        this.edgeMap = edgeMap;
        this.angleMap = angleMap;
    }

    public HashMap<Double, Integer> getEdgeMap() {
        return edgeMap;
    }

    public void setEdgeMap(HashMap<Double, Integer> edgeMap) {
        this.edgeMap = edgeMap;
    }

    public HashMap<Double, Integer> getAngleMap() {
        return angleMap;
    }

    public void setAngleMap(HashMap<Double, Integer> angleMap) {
        this.angleMap = angleMap;
    }
    
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setResolution(int width, int height){
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return (url == null   ? "null" : url) + " : " +
               (edgeMap == null ? "null" : edgeMap.size()) + " points";
    }

    public boolean hasMetaData(){
        if (edgeMap == null || angleMap == null) return false;
        return true;
    }
    
    public boolean hasValidSize() {
        if ((width >= MIN_WIDTH) && (height >= MIN_HEIGHT)) {
            return true;
        } else {
            return false;
        }
    }

}
