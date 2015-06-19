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
    private String playList = "PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        run(generateUrl(playList,50));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String generateUrl(String playlistID, int maxResults){
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults="+Integer.toString(maxResults)+"&playlistId="+playlistID+"&key="+KEY;
    }

    private void run(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                PlayList pl = parse(response.body().string());

                assert pl != null;
                Log.d(TAG,pl.toString());

            }
        });
    }

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
