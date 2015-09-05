package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by Jérôme on 05/09/2015 .
 */
public class Snippet {

    public String title;

    public Snippet(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "title='" + title + '\'' +
                '}';
    }
}
