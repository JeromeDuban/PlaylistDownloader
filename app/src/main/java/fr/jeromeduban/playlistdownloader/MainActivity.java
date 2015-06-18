package fr.jeromeduban.playlistdownloader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;


public class MainActivity extends ActionBarActivity {

    private String KEY = "AIzaSyCAsga_OKjW0350A0msLolXm6-B0769unc";
    private String playList = "PLTMG0ZyH_DfDs5w40xw2LM0FvMBFtYvqP";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView) findViewById(R.id.hello_world);

        client = new OkHttpClient();
        DownloadFilesTask task= new DownloadFilesTask(t,client);
        task.execute(generateUrl(playList,50));

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
}
