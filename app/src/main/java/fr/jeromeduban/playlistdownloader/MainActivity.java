package fr.jeromeduban.playlistdownloader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
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

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.jeromeduban.playlistdownloader.objects.Item;
import fr.jeromeduban.playlistdownloader.objects.Playlist;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static fr.jeromeduban.playlistdownloader.Utils.generateUrl;
import static fr.jeromeduban.playlistdownloader.Utils.generateUrlVideoID;
import static fr.jeromeduban.playlistdownloader.Utils.parsePlaylist;

//TODO Download video
//TODO Extract mp3
//TODO Guess Artist and song name
//TODO Add TextView to chose playlist

public class MainActivity extends AppCompatActivity {

    protected static String KEY = "AIzaSyCAsga_OKjW0350A0msLolXm6-B0769unc";
    protected static int maxResults = 6;

    private String playlistID = "PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP";
    private OkHttpClient client;

    private Activity a;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.container)
    LinearLayout container;

    private ArrayList<Playlist> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        a = this;
        client = new OkHttpClient();

        // Wakelock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //FIXME

        // Initialize ImageLoader
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);

//        if (BuildConfig.DEBUG) {
//            config.writeDebugLogs();
//        }

        ImageLoader.getInstance().init(config.build());
    }

    /**
     * Will be called when the user clicks on Start
     */
    @OnClick(R.id.button)
    public void getPlaylist() {

        // FIXME to be used : text from a TextView
        // Parse playlist from youtube link
        String test = "https://www.youtube.com/watch?v=0TFmncOtzcE&index=1&list=PLTMG0ZyH_DfDsK6j40SUmHGgpv7qTa-QA";
        String id = test.split("list=")[1].split("&")[0];
        LogHelper.d(id);
        if (id.length() != 34) { //TODO Check if id length is always 34
            LogHelper.d("id might be wrong");
        }

        getPlaylistItems(generateUrl(playlistID));
    }

    /**
     * Get all playlist videos information
     *
     * @param url URL of the API call
     */
    private void getPlaylistItems(String url) {

        LogHelper.i("Getting videos from : " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                LogHelper.e(e.getMessage(), e);
                //TODO Add Retry !!!!!
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                LogHelper.d("Got response from " + call.request().url());

                if (!response.isSuccessful()) {
                    LogHelper.d(String.valueOf(response.code()));
                    Utils.ToastOnUIThread(a, "Une erreur réseau est survenue");
                    if (response.code() == 404) {
                        LogHelper.d("Error 404");
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } else {
                    Playlist pl = parsePlaylist(response.body().string());

                    if (pl != null) {

                        list.add(pl);

                        // If there are another page, get next playlist items
                        if (pl.nextPageToken != null) {
                            getPlaylistItems(generateUrl(playlistID, pl.nextPageToken));
                        } else {
                            playlistCallback(list);
                        }
                    } else {
                        LogHelper.d("PL null");
                    }
                }
            }
        });
    }

    /**
     * Get videos' data
     *
     * @param list
     */
    private void playlistCallback(ArrayList<Playlist> list) {
        final Handler mainHandler = new Handler(Looper.getMainLooper());

        for (Playlist playList : list) {
            for (final Item item : playList.items) {
                final String call = generateUrlVideoID(item.contentDetails.videoId);

                final Request request = new Request.Builder()
                        .url(call)
                        .build();

                client.newCall(request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogHelper.e(e.getMessage(), e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        final Playlist playlist = parsePlaylist(response.body().string());

                        if (playlist != null) {
                            mainHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (playlist.items !=null && playlist.items.size() > 0) {
                                        displayCard(playlist.items.get(0));
                                    }else{
                                        LogHelper.i("Vidéo supprimée");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void displayCard(Item item) {

        String title = item.snippet.title;
        String url = item.snippet.thumbnails.medium.url;

        ImageLoader imageLoader = ImageLoader.getInstance();
        LayoutInflater in = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = in.inflate(R.layout.card, container, false);

        TextView tv = (TextView) card.findViewById(R.id.fileName);
        tv.setText(title);

        ImageView iv = (ImageView) card.findViewById(R.id.thumbnail);
        imageLoader.displayImage(url, iv);

        container.addView(card);
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
