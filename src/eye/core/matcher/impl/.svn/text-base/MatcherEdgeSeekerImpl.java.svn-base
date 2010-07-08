/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.matcher.impl;

import eye.core.matcher.Matcher;
import com.db4o.ObjectContainer;
import com.db4o.query.Query;
import eye.server.manager.impl.DBManagerBasicImpl;
import eye.core.model.Image;
import eye.core.model.Point;
import eye.core.model.result.SearchResult;
import eye.core.model.result.SimilarityResult;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author spr1ng
 * @version $Id$
 */
public class MatcherEdgeSeekerImpl implements Matcher{
    /**
     * Коэффициент похожести (минимальный порог в процентах,
     * при котором два рисунка можно считать похожими)
     */
    private static final int SIMILARITY_MIN = 60; // %

    /**
     * Определяет схожесть одного рисунка с другим в пройентах
     * @param image1
     * @param image2
     * @param oneInAnother искать ли один рисунок внутри другого?
     * @return
     */
    public SimilarityResult getSimilarityResult(Image image1, Image image2, boolean oneInAnother) {
        int sameEdges = 0, totalEdges = 0;

        HashMap<Integer, Integer> edgeMap1 = image1.getEdgeMap();
        HashMap<Integer, Integer> edgeMap2 = image2.getEdgeMap();
        int totalEdges1 = 0;
        for (Integer edgeSize : edgeMap1.keySet()) {
            totalEdges1 += edgeMap1.get(edgeSize);
        }
        int totalEdges2 = 0;
        for (Integer edgeSize : edgeMap2.keySet()) {
            totalEdges2 += edgeMap2.get(edgeSize);
        }

        if (oneInAnother)//Если ищем одну картинку в другой..
            totalEdges = (totalEdges1 + totalEdges2) / 2;
        else
            totalEdges = totalEdges1 > totalEdges2 ? totalEdges1 : totalEdges2;

        System.out.println("Total edges1: " + totalEdges1);
        System.out.println("Total edges2: " + totalEdges2);

        //Находим количество ребер разных длин, одинаковых для обоих рисунков
        for (Integer edgeSize : edgeMap1.keySet()) {
            Integer edgeQty1 = edgeMap1.get(edgeSize);
            Integer edgeQty2 = edgeMap2.get(edgeSize);
            if (edgeQty1 != null && edgeQty2 != null){
                //Большее число ребер заданной длины
                int maxQty = edgeQty1 > edgeQty2 ? edgeQty1 : edgeQty2;
                sameEdges += maxQty - Math.abs(edgeQty1 - edgeQty2);
            }
        }

        System.out.println("total edges: " + totalEdges);
        System.out.println("same edges: " + sameEdges);

        int result = sameEdges * 100 / totalEdges;
        System.out.println("-----> Matches: " + result + "%");

        return new SimilarityResult(image1, image2, result, totalEdges1, totalEdges2, totalEdges);
    }

    public SearchResult getSearchResult(Image image1, List<Image> images, boolean oneInAnother) {
        Set<SimilarityResult> results = new HashSet<SimilarityResult>();
        for (Image image2 : images) {
            SimilarityResult similarity = getSimilarityResult(image1, image2, oneInAnother);
            if (similarity.getSimilarity() >= SIMILARITY_MIN)
                if (!image1.equals(image2)) results.add(similarity);
        }
        
        return new SearchResult(image1, results);
    }
    
    private static void printPoints(Image image){
        List<Point> points = image.getPoints();
        System.out.println("Points ----------------------------------");
        for (Point point : points) {
            System.out.print("(" + point.getX() + "; " + point.getY() + ") ");
        }
        System.out.println();
    }

    private static void printEdges(Image image){
        HashMap<Integer, Integer> edges = image.getEdgeMap();
        Set<Integer> keys = edges.keySet();
        System.out.println("Edges -----------------------------------");
        for (Integer edgeSize : keys) {
            Integer edgeQty = edges.get(edgeSize);
            System.out.println("s: " + edgeSize + "; q: " + edgeQty);
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        DBManagerBasicImpl dbm = new DBManagerBasicImpl();
        ObjectContainer db = dbm.getContainer();
        Query query = db.query();
        query.constrain(Image.class);
        query.descend("url").orderAscending();
        /*Point p1 = new Point(15, 17); p1.computeRelativeXY(200, 200);
        Point p2 = new Point(15, 20); p2.computeRelativeXY(200, 200);
        Point p3 = new Point(18, 17); p3.computeRelativeXY(200, 200);
        List<Point> pts1 = new ArrayList<Point>();
        pts1.add(p1); pts1.add(p2); pts1.add(p3);
        Image img1 = new Image(null, pts1);
        
        Point p21 = new Point(8, 7); p21.computeRelativeXY(200, 200);
        Point p22 = new Point(8, 10); p22.computeRelativeXY(200, 200);
        Point p23 = new Point(5, 10); p23.computeRelativeXY(200, 200);
        List<Point> pts2 = new ArrayList<Point>();
        pts2.add(p21); pts2.add(p22); pts2.add(p23);
        Image img2 = new Image(null, pts2);*/

        List<Image> images = query.execute();//new ArrayList<Image>();
//        images.add(img1); images.add(img2);
        System.out.println("Images: " + images.size());
        if (images.size() > 1){
            
            for (Image image : images) {
                System.out.println("-----------------------------------------");
                System.out.println("         " + image.getUrl());
                printPoints(image);
                printEdges(image);
            }
            
            Matcher m = new MatcherEdgeSeekerImpl();
            /*Image image1 = images.get(0);
            Image image2 = images.get(1);
            if (image1.hasMetaData() && image2.hasMetaData())
                System.out.println("isSimilar: " + m.isSimilar(image1, image2));
            else System.out.println("No metadata found. Run Seeker first.");*/
            Image image1 = images.get(8);
            
            SearchResult searchResult = m.getSearchResult(image1, images, false);
            System.out.println("------------> " + image1.getUrl());
            System.out.println("------------> Simlar images total: " + searchResult.totalImages());
            for (Image image : searchResult.getImages()) {
                System.out.println("-----> " + image.getUrl());
            }
        }
        
    }

}
