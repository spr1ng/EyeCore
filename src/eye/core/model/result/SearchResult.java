/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.model.result;

import eye.core.model.Image;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author spr1ng
 * @version $Id$
 */
public class SearchResult {

    private Image userImage;
    private List<SimilarityResult> results;

    public SearchResult(Image userImage, List<SimilarityResult> results) {
        this.userImage = userImage;
        this.results = results;
    }

    public SearchResult(List<SimilarityResult> results) {
        this.results = results;
    }

    public Image getUserImage() {
        return userImage;
    }

    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

    public List<SimilarityResult> getResults() {
        return results;
    }

    public void setResults(List<SimilarityResult> results) {
        this.results = results;
    }

    public int totalImages(){
        return results.size();
    }
    /** Возвращает список всех найденных похожих картинок */
    public Set<Image> getImages(){
        Set<Image> similarImages = new HashSet<Image>();
        for (SimilarityResult result : results) {
            similarImages.add(result.getImage2());
        }
        
        return similarImages;
    }

    public void sortResults(){
        SimilarityResult[] results2 = results.toArray(new SimilarityResult[0]);
        Arrays.sort(results2);
        for (int i = 0; i < results2.length; i++) {
            SimilarityResult result = results2[i];
            results.set(i, result);
        }
    }

}
