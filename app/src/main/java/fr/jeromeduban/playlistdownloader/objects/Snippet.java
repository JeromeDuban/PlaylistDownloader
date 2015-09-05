package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by Jérôme on 05/09/2015 .
 */
public class Snippet {

    public String title;
    public Thumbnail thumbnails;

    public Snippet(String title, Thumbnail thumbnails) {
        this.title = title;
        this.thumbnails = thumbnails;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "title='" + title + '\'' +
                ", thumbnails=" + thumbnails +
                '}';
    }
}
