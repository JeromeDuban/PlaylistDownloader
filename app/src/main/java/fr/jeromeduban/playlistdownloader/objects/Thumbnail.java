package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by Jérôme on 05/09/2015 .
 */
public class Thumbnail {
    public Medium medium;

    public Thumbnail(Medium medium) {
        this.medium = medium;
    }

    @Override
    public String toString() {
        return "Thumbnail{" +
                "medium=" + medium +
                '}';
    }
}
