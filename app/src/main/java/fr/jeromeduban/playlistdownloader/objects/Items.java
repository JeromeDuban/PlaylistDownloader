package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by jduban on 18/06/2015.
 */
public class Items {

    public final String kind;
    public final String etag;
    public final String id;
    public final ContentDetails contentDetails;

    public Items(String kind, String etag, String id, ContentDetails contentDetails) {
        this.kind = kind;
        this.etag = etag;
        this.id = id;
        this.contentDetails = contentDetails;
    }

    @Override
    public String toString() {
        return "Items{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", contentDetails=" + contentDetails +
                '}';
    }
}
