package fr.jeromeduban.playlistdownloader;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by PJDN05921 on 05/05/2017.
 */

public class Utils {

    static void ToastOnUIThread(final Activity a, final String message){
        a.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
