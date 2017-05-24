package fr.jeromeduban.playlistdownloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_container)
    LinearLayout homeContainer;

    public static Map<String, String> playlistsMap;

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
            String[] list = sharedText.split(":", 2);
            if (list.length == 2 && list[1].toLowerCase().contains("list=")) {
                openNewPlaylistDialog(list[1].trim(), list[0].trim());
            } else {
                Toast.makeText(this, "Error while getting playlist", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlaylists();
        refreshUI();
    }

    @OnClick(R.id.fab)
    void fabAction(View view) {
        openNewPlaylistDialog(null, null);
    }

    private void openNewPlaylistDialog(@Nullable String url, @Nullable String name) {
        View v = new MaterialDialog.Builder(this)
                .title("Nouvelle liste")
                .customView(R.layout.new_playlist, false)
                .positiveText("Valider")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (addPlaylist(dialog)) {
                            dialog.dismiss();
                        } else {
                            Utils.ToastOnUIThread(HomeActivity.this, "La playlist éxiste déjà. ");
                        }
                    }
                })
                .show()
                .getCustomView();

        if (v != null) {

            EditText urlET = (EditText) v.findViewById(R.id.playlist_url);
            EditText nameET = (EditText) v.findViewById(R.id.playlist_name);

            if (url != null) {
                urlET.setText(url);
            }

            if (name != null) {
                nameET.setText(name);
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
                            if (charSequence.length() > 5) {
                                icon.setVisibility(View.VISIBLE);

                                String[] splits = charSequence.toString().split("list=");

                                if (splits.length > 1) {
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

    private boolean addPlaylist(MaterialDialog dialog) {
        View v = dialog.getCustomView();
        if (v != null && playlistsMap != null) {
            EditText urlET = (EditText) v.findViewById(R.id.playlist_url);
            EditText nameET = (EditText) v.findViewById(R.id.playlist_name);

            for (Map.Entry<String, String> entry : playlistsMap.entrySet()) {
                if (entry.getKey().equals(urlET.getText().toString().trim())) return false;
            }
            playlistsMap.put(urlET.getText().toString().trim(), nameET.getText().toString().trim());
            savePlaylists();
            refreshUI();
            return true;
        }

        return false;
    }

    private void refreshUI() {
        //TODO
        homeContainer.removeAllViews();

        for (final Map.Entry<String, String> entry : playlistsMap.entrySet()) {

            Button btn = new Button(this);
            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setText(entry.getValue());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HomeActivity.this, DownloadActivity.class);
                    i.putExtra("url", entry.getKey());
                    startActivity(i);
                }
            });
            homeContainer.addView(btn);
        }

    }

//    @OnClick(R.id.temp)
//    void startDownload() {
//        Intent i = new Intent(this, DownloadActivity.class);
//        i.putExtra("url", getResources().getString(R.string.sample_playlist));
//        startActivity(i);
//    }

    public boolean savePlaylists() {
        //TODO
        return true;
    }

    public Map<String, String> getPlaylists() {
        //TODO

        playlistsMap = new HashMap<>();
        playlistsMap.put("https://www.youtube.com/watch?v=CDrCGZVe5zM&list=RDCDrCGZVe5zM", "Mix chill");
        playlistsMap.put("https://www.youtube.com/playlist?list=PLTMG0ZyH_DfBFPJA0dLWijvzbONcg9Tkg", "Nazes old");

        return playlistsMap;
    }

}
