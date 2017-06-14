package fr.jeromeduban.playlistdownloader;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.jeromeduban.playlistdownloader.objects.Playlist;

/**
 * Created by PJDN05921 on 05/05/2017.
 */

class Utils {

    static final int WRITE_PERMISSION = 100;

    static final String[] COLOR_LIST = new String[]{"#d96c6c", "#e55c00", "#a66c29", "#403300", "#4c5916", "#008033", "#264d4a", "#00294d", "#003d99", "#534d66", "#602080", "#b35995", "#591631", "#664d50", "#ff2200", "#f2ceb6", "#736556", "#b2a159", "#19bf00", "#134d2a", "#39dae6", "#307cbf", "#7b6cd9", "#6100f2", "#352040", "#ff0088"};


    static void ToastOnUIThread(final Activity a, final String message){
        a.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static String generateUrl(String playlistID) {
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=" + Integer.toString(DownloadActivity.maxResults) + "&playlistId=" + playlistID + "&key=" + DownloadActivity.KEY;
    }

    static String generateUrl(String playlistID, String nextPageToken) {
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=" + Integer.toString(DownloadActivity.maxResults) + "&playlistId=" + playlistID + "&key=" + DownloadActivity.KEY + "&pageToken=" + nextPageToken;
    }

    static String generateUrlVideoID(String videoID) {
        return "https://www.googleapis.com/youtube/v3/videos?id=" + videoID + "&part=snippet" + "&key=" + DownloadActivity.KEY;
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

    protected static boolean checkPermission(final Activity a) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(a,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(a,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new MaterialDialog.Builder(a)
                        .title(R.string.app_name)
                        .content(R.string.permission_explanation)
                        .positiveText("Accepter")
                        .negativeText("Refuser")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                ActivityCompat.requestPermissions(a,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        WRITE_PERMISSION);
                            }
                        })
                        .show();



            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            return true;
        }
        return false;
    }

    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static int getPositionInAlphabet(String string) {

        string = string.toLowerCase();

        int temp = (int) string.charAt(0);
        if (temp <= 122 & temp >= 97) {
            return temp - 96;
        }
        return -1;
    }
}
