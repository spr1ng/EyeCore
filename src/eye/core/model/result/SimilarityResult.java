/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.model.result;

import eye.core.math.MathExt;
import eye.core.model.Image;

/**
 * Результат сравнения двух картинок
 * @author spr1ng
 * @version $Id$
 */
public class SimilarityResult implements Comparable{

    /** Рисунок пользователя */
    private Image image1;
    /** Рисунок с которым производилось сравнение */
    private Image image2;
    /** Похожесть по первому признаку (карта ребер) */
    private double similarity1;
    /** Похожесть по второму признаку (карта углов) */
    private double similarity2;
    private double similarity3;

    public SimilarityResult() {
    }

    public SimilarityResult(double similarity1) {
        this.similarity1 = similarity1;
    }

    public SimilarityResult(Image image1, Image image2, double similarity1) {
        this.image1 = image1;
        this.image2 = image2;
        this.similarity1 = similarity1;
    }

    public SimilarityResult(Image image1, Image image2, double similarity1, double similarity2) {
        this.image1 = image1;
        this.image2 = image2;
        this.similarity1 = similarity1;
        this.similarity2 = similarity2;
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

    public double getSimilarity1() {
        return similarity1;
    }

    public void setSimilarity1(double similarity1) {
        this.similarity1 = similarity1;
    }

    public double getSimilarity2() {
        return similarity2;
    }

    public void setSimilarity2(double similarity2) {
        this.similarity2 = similarity2;
    }

    public double getSimilarity3() {
        return similarity3;
    }

    public void setSimilarity3(double similarity3) {
        this.similarity3 = similarity3;
    }

    /** Возвращает общий процент похожести */
    public double getSimilarity(){//TODO: изменить конечно
        return MathExt.round((similarity1 + similarity2) / 2, 2);
    }

    public int compareTo(Object o) {
        SimilarityResult result2 = (SimilarityResult) o;
        if (getSimilarity() > result2.getSimilarity()) return -1;
        if (getSimilarity() < result2.getSimilarity()) return 1;
        return 0;
    }
    
}
