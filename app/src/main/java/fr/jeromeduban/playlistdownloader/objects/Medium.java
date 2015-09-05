package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by Jérôme on 05/09/2015 .
 */
public class Medium {
    public String url;

    public Medium(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Medium{" +
                "url='" + url + '\'' +
                '}';
    }
}