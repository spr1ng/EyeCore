/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.matcher;

import eye.core.model.Image;
import eye.core.model.result.SearchResult;
import eye.core.model.result.SimilarityResult;
import java.util.List;

/**
 *
 * @author spr1ng
 * @version $Id$
 */
public interface Matcher {
    /** Минимальный процент похожести, при котором картинки можно считать схожими */
    public static double SIMILARITY_MIN = 60.0;
    /** Кол-во знаков после запятой в значении похожести рисунков */
    public static int SIMILARITY_ROUNDING = 4;

    /**
     * 
     * @param image1
     * @param image2
     * @param oneInAnother искать ли один рисунок внутри другого?
     * @return
     */
    SimilarityResult getSimilarityResult(Image image1, Image image2, boolean oneInAnother);
    
    /**
     * 
     * @param image1
     * @param images list of images to check
     * @param oneInAnother искать ли один рисунок внутри другого?
     * @return
     */
    SearchResult getSearchResult(Image image1, List<Image> images, boolean oneInAnother);

}
