package fr.jeromeduban.playlistdownloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO Add a way to share playlist from youtube app to this app

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.checkPermission(this);



        //TODO Export in method
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            //Format = "listName : http://playlistUrl"
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            String[] list = sharedText.split(":",2);
            if (list.length == 2 && list[1].toLowerCase().contains("list=")){
                openNewPlaylistDialog(list[1].trim(),list[0].trim());
            }else{
                Toast.makeText(this, "Error while getting playlist", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.fab)
    void fabAction(View view) {
        openNewPlaylistDialog(null,null);
    }

    private void openNewPlaylistDialog(@Nullable String url, @Nullable String name) {
        View v = new MaterialDialog.Builder(this)
                .title("Nouvelle liste")
                .customView(R.layout.new_playlist, false)
                .positiveText("Valider")
                .show()
                .getCustomView();

        if (v != null) {

            EditText urlET = (EditText) v.findViewById(R.id.playlist_url);
            EditText nameET = (EditText) v.findViewById(R.id.playlist_name);

            if(url !=null){
                urlET.setText(url);
            }

            if(name !=null){
                nameET.setText(name);
                nameET.setEnabled(true);
            }

            final ImageView icon = (ImageView) v.findViewById(R.id.icon);

            urlET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(final CharSequence charSequence, int i, final int count, int i2) {

                    HomeActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            if (count > 5) {
                                icon.setVisibility(View.VISIBLE);

                                String[] splits = charSequence.toString().split("list=");

                                if (splits.length > 1){
                                    String id = charSequence.toString().split("list=")[1].split("&")[0];
                                    LogHelper.d(id);

                                    if (id.length() != 34) { //TODO Check if id length is always 34
                                        icon.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.mipmap.ic_cancel));
                                    } else {
                                        icon.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.mipmap.ic_checked));
                                    }
                                }

                            } else {
                                icon.setVisibility(View.GONE);
                            }

                        }
                    });
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    @OnClick(R.id.temp)
    void startDownload() {
        Intent i = new Intent(this, DownloadActivity.class);
        i.putExtra("url", getResources().getString(R.string.sample_playlist));
        startActivity(i);
    }

}