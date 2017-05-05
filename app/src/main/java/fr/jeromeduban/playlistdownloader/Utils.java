package fr.jeromeduban.playlistdownloader;

import android.app.Activity;
import android.widget.Toast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import fr.jeromeduban.playlistdownloader.objects.Playlist;

/**
 * Created by PJDN05921 on 05/05/2017.
 */

class Utils {

    static void ToastOnUIThread(final Activity a, final String message){
        a.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static String generateUrl(String playlistID) {
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=" + Integer.toString(MainActivity.maxResults) + "&playlistId=" + playlistID + "&key=" + MainActivity.KEY;
    }

    static String generateUrl(String playlistID, String nextPageToken) {
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=" + Integer.toString(MainActivity.maxResults) + "&playlistId=" + playlistID + "&key=" + MainActivity.KEY + "&pageToken=" + nextPageToken;
    }

    static String generateUrlVideoID(String videoID) {
        return "https://www.googleapis.com/youtube/v3/videos?id=" + videoID + "&part=snippet" + "&key=" + MainActivity.KEY;
    }


    /**
     * Parse Json from Youtube API
     *
     * @param json json from youtube API
     * @return Playlist as an object
     */
    static Playlist parsePlaylist(String json) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Playlist> jsonAdapter = moshi.adapter(Playlist.class);
        try {
            return jsonAdapter.fromJson(json);
        } catch (IOException e) {
            LogHelper.e(e.getMessage(), e);
        }
        return null;
    }
}
