package fr.jeromeduban.playlistdownloader;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import fr.jeromeduban.playlistdownloader.objects.PlayList;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private String KEY = "AIzaSyCAsga_OKjW0350A0msLolXm6-B0769unc";
    private String playlistID = "PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP";
    private OkHttpClient client;
    private int maxResults = 6;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                ArrayList<PlayList> list = new ArrayList<>();
                getPlaylistItems(generateUrl(playlistID, maxResults), list);

                // TODO to be used
                String test = "https://www.youtube.com/watch?v=0TFmncOtzcE&index=1&list=PLTMG0ZyH_DfDsK6j40SUmHGgpv7qTa-QA";
                String id = test.split("list=")[1].split("&")[0];
                if (id.length() != 34) {
                    Log.d(TAG, "id might be wrong");
                }
                Log.d(TAG, id);
            }
        });
    }

    private void playlistCallback(ArrayList<PlayList> list){
        button.setText("Done");
        button.setEnabled(false);

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        for (PlayList pl : list){
            LayoutInflater in = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View card = in.inflate(R.layout.card,container, false);

            TextView title = (TextView) card.findViewById(R.id.fileName);
            title.setText(String.valueOf(pl.items.size()) + " items");

            container.addView(card);
        }

    }

    private String generateUrl(String playlistID, int maxResults){
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults="+Integer.toString(maxResults)+"&playlistId="+playlistID+"&key="+KEY;
    }
    private String generateUrl(String playlistID, int maxResults, String nextPageToken){
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults="+Integer.toString(maxResults)+"&playlistId="+playlistID+"&key="+KEY+"&pageToken="+nextPageToken;
    }

    private void getPlaylistItems(String url, final ArrayList<PlayList> list){

        System.out.println("URL : "+ url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    if (response.code() == 404)
                        Log.d(TAG, "Error 404");
                    else
                        throw new IOException("Unexpected code " + response);

                PlayList pl = parse(response.body().string());

                if (pl != null){

                    list.add(pl);

                    if (pl.nextPageToken != null){ // If there are another page
                        getPlaylistItems(generateUrl(playlistID, maxResults ,pl.nextPageToken), list);
                    }
                    else{
                        // Display elements on UI thread
                        mainHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                playlistCallback(list);
                            }
                        });
                    }
                }
                else
                    Log.d(TAG, "PL null");
            }
        });

    }

    /**
     * Parse Json from Youtube API
     * @param json json from youtube API
     * @return Playlist as an object
     */
    private PlayList parse(String json){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PlayList> jsonAdapter = moshi.adapter(PlayList.class);
        try {
            return jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
