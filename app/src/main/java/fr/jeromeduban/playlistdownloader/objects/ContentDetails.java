package fr.jeromeduban.playlistdownloader.objects;

/**
 * Created by jduban on 18/06/2015.
 */
public class ContentDetails {

    public final String videoId;

    public ContentDetails(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "ContentDetails{" +
                "videoId='" + videoId + '\'' +
                '}';
    }
}
