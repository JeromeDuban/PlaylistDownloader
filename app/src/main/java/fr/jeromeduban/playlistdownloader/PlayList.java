package fr.jeromeduban.playlistdownloader;

import java.util.List;

/**
 * Created by jduban on 18/06/2015.
 */

public final class PlayList {

    public final String kind;
    public final String etag;
    public final PageInfo pageInfo;
    public final List<Items> items;

    public PlayList(String kind, String etag, PageInfo pageInfo, List<Items> items) {
        this.kind = kind;
        this.etag = etag;
        this.pageInfo = pageInfo;
        this.items = items;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", pageInfo=" + pageInfo +
                ", items=" + items +
                '}';
    }
}