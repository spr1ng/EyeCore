package eye.core.model;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author gpdribbler, spr1ng, stream
 * @version $Id: Image.java 52 2010-07-07 06:10:05Z spr1ng $
 */
public class Image implements RemoteSource{
    
    private String url;
    private List<Point> points;
    

    public Image() {
    }

    public Image(String url) {
        this.url = url;
    }

    public Image(String url, List<Point> points) {
        this.url = url;
        this.points = points;
    }

    /**
     * HashMap (edgeSize, edgeQuantity)
     * @return HashMap
     */
    public HashMap<Integer, Integer> getEdgeMap(){
        final int EDGE_MAX_SIZE = 50;
        if (points == null) return null;
        HashMap<Integer, Integer> edges = new HashMap<Integer, Integer>();
        for (int i = 0; i < points.size(); i++) {
            Point point1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Point point2 = points.get(j);
                int edgeSize = point1.getDistanceTo(point2);
                if (edgeSize > 0 && edgeSize <= EDGE_MAX_SIZE) {
                    Integer edgeQty = edges.get(edgeSize);
                    if (edgeQty == null) edgeQty = 0;
                    edges.put(edgeSize, ++edgeQty);
                }
            }
        }
        return edges;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        String result = "";//url.toString();
        result += ": " + points.size() + " points";
        return result;
    }

    public boolean hasMetaData(){
        if (points == null) return false;
        return true;
    }

}
