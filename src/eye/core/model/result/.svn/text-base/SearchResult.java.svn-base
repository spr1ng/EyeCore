/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eye.core.model.result;

import eye.core.model.Image;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author spr1ng
 * @version $Id$
 */
public class SearchResult {

    private Image userImage;
    private Set<SimilarityResult> results;

    public SearchResult(Image userImage, Set<SimilarityResult> results) {
        this.userImage = userImage;
        this.results = results;
    }

    public SearchResult(Set<SimilarityResult> results) {
        this.results = results;
    }

    public Image getUserImage() {
        return userImage;
    }

    public void setUserImage(Image userImage) {
        this.userImage = userImage;
    }

    public Set<SimilarityResult> getResults() {
        return results;
    }

    public void setResults(Set<SimilarityResult> results) {
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

}
