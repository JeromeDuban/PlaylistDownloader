package fr.jeromeduban.playlistdownloader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import fr.jeromeduban.playlistdownloader.objects.PlayList;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private String KEY = "AIzaSyCAsga_OKjW0350A0msLolXm6-B0769unc";
    private String playlistID = "PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        getPlaylistItems(generateUrl(playlistID, 50));

        String test = "https://www.youtube.com/watch?v=0TFmncOtzcE&index=1&list=PLTMG0ZyH_DfDsK6j40SUmHGgpv7qTa-QA";

        String id = test.split("list=")[1].split("&")[0];
        if (id.length() != 34){
            Log.d(TAG,"id might be wrong");
        }
        Log.d(TAG,id);

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

    private String generateUrl(String playlistID, int maxResults){
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults="+Integer.toString(maxResults)+"&playlistId="+playlistID+"&key="+KEY;
    }

    private void getPlaylistItems(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        //TODO Add multiple API calls if there is more than 50 songs in the playlist
        // Try with : https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=18&playlistId=PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP&key=AIzaSyCAsga_OKjW0350A0msLolXm6-B0769unc

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful())
                    if (response.code() == 404)
                        Log.d(TAG,"Error 404");
                    else
                        throw new IOException("Unexpected code " + response);

                PlayList pl = parse(response.body().string());

                if (pl != null)
                    Log.d(TAG,pl.toString());
                else
                    Log.d(TAG,"PL null");

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
}
