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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

import fr.jeromeduban.playlistdownloader.objects.Item;
import fr.jeromeduban.playlistdownloader.objects.Playlist;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private String KEY = "AIzaSyCAsga_OKjW0350A0msLolXm6-B0769unc";
    private String playlistID = "PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP";
    private OkHttpClient client;
    private int maxResults = 6;
    private Button button;
    private ImageLoaderConfiguration.Builder config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Wakelock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new OkHttpClient();
                ArrayList<Playlist> list = new ArrayList<>();

                // TODO to be used : text from a TextView
                String test = "https://www.youtube.com/watch?v=0TFmncOtzcE&index=1&list=PLTMG0ZyH_DfDsK6j40SUmHGgpv7qTa-QA";
                String id = test.split("list=")[1].split("&")[0];
                if (id.length() != 34) {
                    Log.d(TAG, "id might be wrong");
                }
                Log.d(TAG, id);


                getPlaylistItems(generateUrl(playlistID, maxResults), list);
            }
        });

        config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }

    private void playlistCallback(ArrayList<Playlist> list) {

        for (Playlist playList : list) {
            for (final Item item : playList.items) {
                final String call = generateUrlVideoID(item.contentDetails.videoId);

                Request request = new Request.Builder()
                        .url(call)
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    Handler mainHandler = new Handler(Looper.getMainLooper());


                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        final Playlist playlist = parsePlaylist(response.body().string());

                        if (playlist != null){
                            mainHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    displayCards(playlist.items.get(0).snippet.title,
                                            playlist.items.get(0).snippet.thumbnails.medium.url );

                                }
                            });
                        }
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void displayCards(String title, String url) {

        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        ImageLoader imageLoader = ImageLoader.getInstance();
        LayoutInflater in = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = in.inflate(R.layout.card, container, false);

        TextView tv = (TextView) card.findViewById(R.id.fileName);
        tv.setText(title);

        ImageView iv = (ImageView) card.findViewById(R.id.thumbnail);
        imageLoader.displayImage(url, iv);

        container.addView(card);
    }

    private String generateUrl(String playlistID, int maxResults) {
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=" + Integer.toString(maxResults) + "&playlistId=" + playlistID + "&key=" + KEY;
    }

    private String generateUrl(String playlistID, int maxResults, String nextPageToken) {
        return "https://www.googleapis.com/youtube/v3/playlistItems?part=contentDetails&maxResults=" + Integer.toString(maxResults) + "&playlistId=" + playlistID + "&key=" + KEY + "&pageToken=" + nextPageToken;
    }

    private String generateUrlVideoID(String videoID) {
        return "https://www.googleapis.com/youtube/v3/videos?id=" + videoID + "&part=snippet" + "&key=" + KEY;
    }

    /**
     * Get all playlist videos information
     *
     * @param url  URL of the API call
     * @param list Will be filled with the API response
     */
    private void getPlaylistItems(String url, final ArrayList<Playlist> list) {

        System.out.println("URL : " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {

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

                Playlist pl = parsePlaylist(response.body().string());

                if (pl != null) {

                    list.add(pl);

                    if (pl.nextPageToken != null) { // If there are another page
                        getPlaylistItems(generateUrl(playlistID, maxResults, pl.nextPageToken), list);
                    } else {
                        playlistCallback(list);
                    }
                } else
                    Log.d(TAG, "PL null");
            }
        });

    }

    /**
     * Parse Json from Youtube API
     *
     * @param json json from youtube API
     * @return Playlist as an object
     */
    private Playlist parsePlaylist(String json) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Playlist> jsonAdapter = moshi.adapter(Playlist.class);
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
