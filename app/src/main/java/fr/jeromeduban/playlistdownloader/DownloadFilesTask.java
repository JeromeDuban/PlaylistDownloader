package fr.jeromeduban.playlistdownloader;

import android.os.AsyncTask;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Jérôme on 11/06/2015 .
 */

public class DownloadFilesTask extends AsyncTask<String, Integer, String> {

    private final OkHttpClient client;
    private TextView tv;

    public DownloadFilesTask(TextView tv, OkHttpClient client) {
        this.tv = tv;
        this.client = client;
    }

    @Override
    protected String doInBackground(String... params) {
        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            LoganSquare.parse(response.body().byteStream());


            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {

        if (result !=null)
            tv.setText(result);
    }
}