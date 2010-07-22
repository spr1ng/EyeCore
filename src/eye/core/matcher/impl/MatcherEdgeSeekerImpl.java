/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.matcher.impl;

import eye.core.matcher.Matcher;
import eye.core.math.MathExt;
import eye.server.manager.DBManagerBasicImpl;
import eye.core.model.Image;
import eye.core.model.result.SearchResult;
import eye.core.model.result.SimilarityResult;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    /** 
     * Возвращает процент похожести двух HashMap<Double, Integer>
     * @param map1
     * @param map2
     * @param oneInAnother
     * @return
     */
    public double getMapSimilarity(HashMap<Double, Integer> map1,
                                   HashMap<Double, Integer> map2,
                                   boolean oneInAnother){
        int same = 0, total = 0;
    //Считаем общее кол-во признаков (углов, ребер и т.п.) для каждого map'a
        int total1 = 0, total2 = 0;
        for (Double size : map1.keySet()) total1 += map1.get(size);
        for (Double size : map2.keySet()) total2 += map2.get(size);
    //Выбираем наибольшее в качестве общего кол-ва признаков
        total = (total1 > total2 || oneInAnother) ? total1 : total2;
    //Находим количество одинаковых признаков для обоих рисунков
        for (Double size : map1.keySet()) {
            Integer qty1 = map1.get(size);
            Integer qty2 = map2.get(size);
            if (qty1 != null && qty2 != null)
                same += qty1 < qty2 ? qty1 : qty2;
        }
    //Вычисляем похожесть
        double similarity = MathExt.round((double)same * 100 / total, SIMILARITY_ROUNDING);
        
        return similarity;
    }

    /**
     * Определяет схожесть одного рисунка с другим в пройентах
     * @param image1
     * @param image2
     * @param oneInAnother искать ли один рисунок внутри другого?
     * @return
     */
    public SimilarityResult getSimilarityResult(Image image1, Image image2, boolean oneInAnother) {
        /*int sameEdges = 0, totalEdges = 0;

        HashMap<Double, Integer> edgeMap1 = image1.getEdgeMap();
        HashMap<Double, Integer> edgeMap2 = image2.getEdgeMap();
        int totalEdges1 = 0;
        for (Double edgeSize : edgeMap1.keySet()) {
            totalEdges1 += edgeMap1.get(edgeSize);
        }
        int totalEdges2 = 0;
        for (Double edgeSize : edgeMap2.keySet()) {
            totalEdges2 += edgeMap2.get(edgeSize);
        }

        totalEdges = (totalEdges1 > totalEdges2 || oneInAnother) ? totalEdges1 : totalEdges2;

        System.out.println("Total edges1: " + totalEdges1 + " [" + image1.getUrl() + "]");
        System.out.println("Total edges2: " + totalEdges2 + " [" + image2.getUrl() + "]");

        //Находим количество ребер разных длин, одинаковых для обоих рисунков
        for (Double edgeSize : edgeMap1.keySet()) {
            Integer edgeQty1 = edgeMap1.get(edgeSize);
            Integer edgeQty2 = edgeMap2.get(edgeSize);
            if (edgeQty1 != null && edgeQty2 != null){
                sameEdges += edgeQty1 < edgeQty2 ? edgeQty1 : edgeQty2;
            }
        }

        System.out.println("total edges: " + totalEdges);
        System.out.println("same edges: " + sameEdges);

        double similarity = MathExt.round((double)sameEdges * 100 / totalEdges, Edge.LENGTH_ROUNDING);
        System.out.println("-----> Matches: " + similarity + "%");*/

    //Ищем похожесть ребер
        double edgeSimilarity = getMapSimilarity(image1.getEdgeMap(), image2.getEdgeMap(), oneInAnother);
        System.out.println("edge similarity: " + edgeSimilarity + "%");
    //Ищем похожесть углов    
        double angleSimilarity = getMapSimilarity(image1.getAngleMap(), image2.getAngleMap(), oneInAnother);
        System.out.println("angle similarity: " + angleSimilarity + "%");

        return new SimilarityResult(image1, image2, edgeSimilarity, angleSimilarity);//new SimilarityResult(image1, image2, similarity, totalEdges1, totalEdges2, totalEdges);
    }

    public SearchResult getSearchResult(Image image1, List<Image> images, boolean oneInAnother) {
        List<SimilarityResult> results = new ArrayList<SimilarityResult>();
        for (Image image2 : images) {
            SimilarityResult similarity = getSimilarityResult(image1, image2, oneInAnother);
            if (similarity.getSimilarity1() >= SIMILARITY_MIN &&
                similarity.getSimilarity2() >= SIMILARITY_MIN + 10)
                if (!image1.equals(image2)) results.add(similarity);
        }
        
        return new SearchResult(image1, results);
    }
    
    /*private static void printPoints(Image image){
        List<Edge> edges = image.getEdges();
        System.out.println("Points ----------------------------------");
        for (Edge edge : edges) {
            Point p1 = edge.getPoint1(); Point p2 = edge.getPoint2();
            System.out.print("(" + p1.getX() + "; " + p1.getY() + ") ");
            System.out.print("(" + p2.getX() + "; " + p2.getY() + ") ");
        }
        System.out.println();
    }*/

    private static void printEdges(Image image){
        HashMap<Double, Integer> edges = image.getEdgeMap();
        Set<Double> keys = edges.keySet();
        System.out.println("Edges -----------------------------------");
        for (Double edgeSize : keys) {
            Integer edgeQty = edges.get(edgeSize);
            System.out.println("s: " + edgeSize + "; q: " + edgeQty);
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        DBManagerBasicImpl dbm = new DBManagerBasicImpl();
//        ObjectContainer db = dbm.getContainer();
        List<Image> images = dbm.getValidImages();/*db.query(new Predicate<Image>() {
            @Override
            public boolean match(Image image) {
                return image.getEdgeMap() != null;
            }
        });*/
       /* Query query = db.query();
        query.constrain(Image.class);
        query.descend("url").orderAscending();*/
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

//        List<Image> images = query.execute();//new ArrayList<Image>();
//        images.add(img1); images.add(img2);
        System.out.println("Images: " + images.size());
        if (images.size() > 1){
            
            for (Image image : images) {
                System.out.println("-----------------------------------------");
                System.out.println("         " + image.getUrl());
//                printPoints(image);
                printEdges(image);
            }
            
            Matcher m = new MatcherEdgeSeekerImpl();
            /*Image image1 = images.get(0);
            Image image2 = images.get(1);
            if (image1.hasMetaData() && image2.hasMetaData())
                System.out.println("isSimilar: " + m.isSimilar(image1, image2));
            else System.out.println("No metadata found. Run Seeker first.");*/
            Image image1 = images.get(0);
            
            SearchResult searchResult = m.getSearchResult(image1, images, false);
            System.out.println("------------> " + image1.getUrl());
            System.out.println("------------> Simlar images total: " + searchResult.totalImages());
            for (Image image : searchResult.getImages()) {
                System.out.println("-----> " + image.getUrl());
            }
        }
        
    }

}
