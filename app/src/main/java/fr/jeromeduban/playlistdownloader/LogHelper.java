package fr.jeromeduban.playlistdownloader;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

/**
 * Cette classe permet de faciliter la gestion des logs de l'application. Un tag automatique propre
 * au SDK est ajouté aux logs si jamais l'utilisateur ne souhaite pas en indiquer lui même.
 */
public class LogHelper {

    private static final int MAX_SIZE = 23;
    private static TextView view;

    private LogHelper() {

    }

    static String sExtension = ".java";
    static final String TAG = "PlaylistDownloader";

    private static boolean isDebuggable() {
        return true;
    }

    /**
     * Vérifie si le tag utilisé est correct (<23 caractères)
     *
     * @param tag Tag à vérifier
     * @return Tag valide
     */
    private static String checkTag(String tag) {

        if (tag.length() > MAX_SIZE) {
            return tag.substring(0, MAX_SIZE - 1);
        }
        return tag;
    }

    /**
     * Mets en forme les logs en indiquant le nom de la classe, de la méthode et le numéro de ligne
     *
     * @param message Message a formater
     * @return Message formaté
     */
    private static String checkMessage(String message) {
        if (message == null) {
            return "null";
        }

        StackTraceElement[] sElements = new Throwable().getStackTrace();

        return "[" + sElements[2].getFileName().split(sExtension)[0] + "." + sElements[2].getMethodName() + ":" + sElements[2].getLineNumber() + "] " + message;

        /*return String.format("[%s.%s:%d] %s",
                sElements[2].getFileName().split(sExtension)[0],
                sElements[2].getMethodName(),
                sElements[2].getLineNumber(),
                message);*/
    }

    /**
     * Prints the current method name in the console.
     */
    public static void method(){
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        Log.d(TAG,"[METHOD>" + sElements[1].getFileName().split(sExtension)[0] + "] " + sElements[1].getMethodName());
    }

    public static void parentMethod(){
        StackTraceElement[] sElements = new Throwable().getStackTrace();
        Log.d(TAG,"[" + sElements[1].getFileName().split(sExtension)[0] + "." + sElements[1].getMethodName() + ":" + sElements[1].getLineNumber() + "] Called from : " + sElements[2].getFileName().split(sExtension)[0] + "." + sElements[2].getMethodName() + ":" + sElements[2].getLineNumber());
    }


    /**
     * Affiche un log info
     *
     * @param tagp    Tag a afficher
     * @param message Message a afficher
     */
    public static void i(String tagp, String message) {
        String tag = checkTag(tagp);
        Log.i(tag, checkMessage(message));
        displayLogs(checkMessage(message),"#388E3C");
    }

    /**
     * Affiche un log info
     *
     * @param message Message a afficher
     */
    public static void i(String message) {
        Log.i(TAG, checkMessage(message));
        displayLogs(checkMessage(message),"#388E3C");
    }

    /**
     * Affiche un log erreur
     *
     * @param tagp    Tag a afficher
     * @param message Message a afficher
     */
    public static void e(String tagp, String message) {
        String tag = checkTag(tagp);
        Log.e(tag, checkMessage(message));
        displayLogs(checkMessage(message),"#D32F2F");
    }

    /**
     * Affiche un log erreur
     *
     * @param message Message a afficher
     */
    public static void e(String message) {
        Log.e(TAG, checkMessage(message));
        displayLogs(checkMessage(message),"#D32F2F");
    }

    /**
     * Affiche un log erreur
     *
     * @param tagp    Tag a afficher
     * @param message Message a afficher
     * @param trow    Erreur renvoyée
     */
    public static void e(String tagp, String message, Throwable trow) {
        String tag = checkTag(tagp);
        Log.e(tag, checkMessage(message), trow);
        displayLogs(checkMessage(message),"#D32F2F");
    }

    /**
     * Affiche un log erreur
     *
     * @param message Message a afficher
     * @param trow    Erreur renvoyée
     */
    public static void e(String message, Throwable trow) {
        Log.e(TAG, checkMessage(message), trow);
        displayLogs(checkMessage(message),"#D32F2F");
    }

    /**
     * Affiche un log warning
     *
     * @param tagp    Tag a afficher
     * @param message Message a afficher
     */
    public static void w(String tagp, String message) {
        String tag = checkTag(tagp);
        Log.w(tag, checkMessage(message));
        displayLogs(checkMessage(message),"#D32F2F");
    }

    /**
     * Affiche un log warning
     *
     * @param message Message a afficher
     */
    public static void w(String message) {
        Log.w(TAG, checkMessage(message));
        displayLogs(checkMessage(message),"#D32F2F");
    }

    /**
     * Affiche un log verbose
     *
     * @param tagp    Tag a afficher
     * @param message Message a afficher
     */
    public static void v(String tagp, String message) {
        String tag = checkTag(tagp);
        if (isDebuggable()) {
            Log.v(tag, checkMessage(message));
        }
    }

    /**
     * Affiche un log verbose
     *
     * @param message Message a afficher
     */
    public static void v(String message) {
        Log.v(TAG, checkMessage(message));
    }

    /**
     * Affiche un log debug
     *
     * @param tagp    Tag a afficher
     * @param message Message a afficher
     */
    public static void d(String tagp, String message) {
        String tag = checkTag(tagp);
        if (isDebuggable()) {
            Log.d(tag, checkMessage(message));
            displayLogs(checkMessage(message),"#1976D2");
        }
    }

    /**
     * Affiche un log debug
     *
     * @param message Message a afficher
     */
    public static void d(String message) {
        Log.d(TAG, checkMessage(message));
        displayLogs(checkMessage(message),"#1976D2");
    }

    public static void setView(TextView v){
        view = v;
    }

    public static void displayLogs(final String message, final String color){
        if (view != null){
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    view.append(Html.fromHtml("<font color=\""+color+"\">" + message + "</font><br/>"));
                }
            });
        }


    }
}
