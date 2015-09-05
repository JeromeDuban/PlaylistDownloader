package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by jduban on 18/06/2015.
 */
public class Item {

    public final String kind;
    public final String etag;
    public final String id;
    public final ContentDetails contentDetails;
    public final Snippet snippet;

    public Item(String kind, String etag, String id, ContentDetails contentDetails, Snippet snippet) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.contentDetails = contentDetails;
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "Item{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", contentDetails=" + contentDetails +
                ", snippet=" + snippet +
                '}';
    }
}
