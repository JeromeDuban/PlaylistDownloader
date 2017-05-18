package fr.jeromeduban.playlistdownloader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by PASS03911 on 05/05/2017.
 */

public class DownloadTask extends AsyncTask<String, Integer, Boolean> {

    private Context context;
    private ProgressBar mProgressBar;
    private PowerManager.WakeLock mWakeLock;

    public DownloadTask(Context context, View view) {
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String id = params[0];
        String name = params[1];

        try {
            LogHelper.i(id +">Downloading file");

            int retry = 3;
            URL url = new URL("http://www.youtubeinmp3.com/fetch/?video=https://www.youtube.com/watch?v=" + id); //TODO Extract base url

            int fileLength = -1;
            while (fileLength == -1 && retry >= 0){
                LogHelper.i(id +">Connecting, Retry=" + String.valueOf(retry));
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5*1000);
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    LogHelper.e(id +">Code :" + connection.getResponseCode());
                    return false;
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                fileLength = connection.getContentLength();
                LogHelper.i(id +">File length :" + String.valueOf(fileLength));
                retry --;
            }
            if (fileLength == -1 || retry < 0){
                return false;
            }

            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/PlaylistDownloader", name + ".mp3");
            boolean result = f.getParentFile().mkdirs(); //TODO Use result
            LogHelper.i(id +">File Created");

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(f);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
            LogHelper.i(id +">File downloaded");

        } catch (Exception e) {
            LogHelper.e(e.getMessage(), e);
            return false;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mWakeLock.release();

        if (!result) {
            mProgressBar.setIndeterminate(false);
            mProgressBar.setMax(100);
            mProgressBar.setProgress(100);
            mProgressBar.setBackgroundColor(ContextCompat.getColor(context,R.color.md_red_500));
            Toast.makeText(context, "Download error: ", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
        }

    }
}