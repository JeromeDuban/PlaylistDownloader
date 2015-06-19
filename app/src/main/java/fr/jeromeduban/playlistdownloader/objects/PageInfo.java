package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by jduban on 18/06/2015.
 */
public class PageInfo {

    public final int totalResults;
    public final int resultsPerPage;


    public PageInfo(int totalResults, int resultsPerPage) {
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "totalResults=" + totalResults +
                ", resultsPerPage=" + resultsPerPage +
                '}';
    }
}
