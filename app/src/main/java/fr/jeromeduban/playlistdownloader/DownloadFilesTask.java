package fr.jeromeduban.playlistdownloader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Date;

import fr.jeromeduban.playlistdownloader.objects.PlayList;

/**
 * Created by Jerome on 11/06/2015 .
 */

public class DownloadFilesTask extends AsyncTask<String, String, PlayList> {

    private final OkHttpClient client;
    private TextView tv;
    private long start;
    private final String TAG ="DL Task";

    public DownloadFilesTask(TextView tv, OkHttpClient client) {
        this.tv = tv;
        this.client = client;
    }

    @Override
    protected PlayList doInBackground(String... params) {

        start = new Date().getTime();

        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            publishProgress("Download");

            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<PlayList> jsonAdapter = moshi.adapter(PlayList.class);

            return jsonAdapter.fromJson(response.body().string());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(PlayList result) {
        publishProgress("Parsing");
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        long current = new Date().getTime();
        Log.i(TAG, values[0] + " done in " + (current - start) + " ms");
        start = current;
    }


}