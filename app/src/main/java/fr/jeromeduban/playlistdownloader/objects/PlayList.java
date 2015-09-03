package fr.jeromeduban.playlistdownloader.objects;

import java.util.List;

/**
 * Created by jduban on 18/06/2015.
 */

public final class PlayList {

    public final String kind;
    public final String etag;
    public final String nextPageToken;
    public final PageInfo pageInfo;
    public final List<Item> items;


    public PlayList(String kind, String etag, String nextPageToken, PageInfo pageInfo, List<Item> items) {
        this.kind = kind;
        this.etag = etag;
        this.nextPageToken = nextPageToken;
        this.pageInfo = pageInfo;
        this.items = items;
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", nextPageToken='" + nextPageToken + '\'' +
                ", pageInfo=" + pageInfo +
                ", items=" + items +
                '}';
    }
}