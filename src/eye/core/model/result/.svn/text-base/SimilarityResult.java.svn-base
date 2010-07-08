/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.model.result;

import eye.core.model.Image;

/**
 * Результат сравнения двух картинок
 * @author spr1ng
 * @version $Id$
 */
public class SimilarityResult {

    /** Рисунок пользователя */
    private Image image1;
    /** Рисунок с которым производилось сравнение */
    private Image image2;
    /** Процент схожести двух картинок */
    private int similarity;
    /** Общее кол-во ребер у первой картинки */
    private int edgeQty1;
    /** Общее кол-во ребер у второй картинки */
    private int edgeQty2;
    /** Кол-во ребер, по которому считался результат схожести картинок */
    private int edgeQtyTotal;
    

    public SimilarityResult() {
    }

    public SimilarityResult(int result) {
        this.similarity = result;
    }

    public SimilarityResult(Image image1, Image image2, int result) {
        this.image1 = image1;
        this.image2 = image2;
        this.similarity = result;
    }

    public SimilarityResult(int result, int edgeQty1, int edgeQty2, int totalEdges) {
        this.similarity = result;
        this.edgeQty1 = edgeQty1;
        this.edgeQty2 = edgeQty2;
        this.edgeQtyTotal = totalEdges;
    }

    public SimilarityResult(Image image1, Image image2, int result, int edgeQty1, int edgeQty2, int totalEdges) {
        this.image1 = image1;
        this.image2 = image2;
        this.similarity = result;
        this.edgeQty1 = edgeQty1;
        this.edgeQty2 = edgeQty2;
        this.edgeQtyTotal = totalEdges;
    }

    public Image getImage1() {
        return image1;
    }

    public void setImage1(Image image1) {
        this.image1 = image1;
    }

    public Image getImage2() {
        return image2;
    }

    public void setImage2(Image image2) {
        this.image2 = image2;
    }

    /** Максимальное кол-во ребер */
    public int getMaxEdgesQty(){
        return edgeQty1 > edgeQty2 ? edgeQty1 : edgeQty2;
    };

    /** Среднее кол-во ребер */
    public int getAverageEdgesQty(){
        return (edgeQty1 + edgeQty2) / 2;
    }

    public int getEdgeQty1() {
        return edgeQty1;
    }

    public void setEdgeQty1(int edgeQty1) {
        this.edgeQty1 = edgeQty1;
    }

    public int getEdgeQty2() {
        return edgeQty2;
    }

    public void setEdgeQty2(int edgeQty2) {
        this.edgeQty2 = edgeQty2;
    }

    public int getEdgeQtyTotal() {
        return edgeQtyTotal;
    }

    public void setEdgeQtyTotal(int edgeQtyTotal) {
        this.edgeQtyTotal = edgeQtyTotal;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }
    
}
