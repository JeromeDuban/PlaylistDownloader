package fr.jeromeduban.playlistdownloader;

import android.os.AsyncTask;
import android.widget.TextView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Jerome on 11/06/2015 .
 */

public class DownloadFilesTask extends AsyncTask<String, Integer, String> {

    private final OkHttpClient client;
    private TextView tv;

    public DownloadFilesTask(TextView tv, OkHttpClient client) {
        this.tv = tv;
        this.client = client;
    }

    @Override
    protected String doInBackground(String... params) {
        Request request = new Request.Builder()
                .url(params[0])
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<PlayList> jsonAdapter = moshi.adapter(PlayList.class);

            PlayList PL = jsonAdapter.fromJson(testFull);
            return PL.toString();
//            System.out.println(PL);
//
//
//
//            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {

        if (result !=null)
            tv.setText(result);
    }


    private String testFull ="{\n" +
            " \"kind\": \"youtube#playlistItemListResponse\",\n" +
            " \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/KfOaFEkrMTTguVW3194yIECQiBU\\\"\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 19,\n" +
            "  \"resultsPerPage\": 50\n" +
            " },\n" +
            " \"items\": [\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/Q_WsG6ZQOy7915ewmgLuFRUp-LQ\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf4MARC4qVe6L_xTt-DTj2zc\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"UtF6Jej8yb4\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/rrvfBuYLLpr26_BKzpA4mOmhQyI\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfycV5XlaL3TRMECPbHqC63c\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"RmVxsoz8-Oc\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/UG0MvZru0ddV6jVwkzNn9ewx0SM\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf3bOcSebU_bJVH4EKI-tl9E\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"Xa5juvNCnA0\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/cPuPmtqt2OaGGCqK6U5cD18GZcY\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfz4u7hy2mrAyc4fY27e8B44\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"Mo4cmTaEDIk\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/gSrHzmkJtqu5iSWgXa3_zz1O3h0\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfyz48qTRBAWYzKP7iKV4yV4\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"mNNfZuIA1GQ\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/uFU_nV59U3b3a21x0bKBMPvN2Vs\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfxc_Nft3kk3hPVNXR-dzE7g\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"oh2LWWORoiM\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/JLhu4sMvieroU3NV10d1HvxANHM\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf0nXF-8lFOcy8XLFV6Vh8ss\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"0FWRT9C9XMQ\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/_jCEjIEs30i-B8kQMx6-glnCTg8\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf9BbgJg8f3EJneeactPn6x0\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"szj59j0hz_4\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/-uilWP2V76lGsNQXFl8ZXyRWGbg\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf2a3RIiHFMVKX-O7OF8dJbY\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"85ftfVUTzM4\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/4LB183sAhYy3Bz229nwTp6toR94\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf97deUb-5pxutL4UHq02CGQ\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"qn-X5A0gbMA\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/pZKlEbpFC54l1MLKnmYf_ifSbQQ\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf-O-c4x1gnNPVBnqRDQyQRs\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"Q_p-XBJWgoU\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/iawSBtT9SGx7E5xmXug5d4F06s8\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfz_yTEeG2f3sPHXfi4fz0dU\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"hqQY9UkGC_A\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/CMQc-tF2wjwnxUHpkutv0Jo5jq4\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf1btsaklVrIFMvdiVVzDfz8\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"LpKyzSxVhk4\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/ZhlDLvf2lb88_9Gii52Dy__kIGI\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf5gO0k9_bvTEa6kXJJ4vzpU\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"YqeW9_5kURI\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/NfEjx3amYRrAYgPaeD91bgr6TDA\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf45BdobTk894dH1okqft8cQ\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"g_uoH6hJilc\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/mcqiYxKEJCvkofEOsJBzp_L6OGs\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfxpcNxFLFlko8K9InqmfeHI\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"7zjp1Z5x_2k\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/gnyhgct2myCJQW-x0oL3NWOXRBc\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosfwNdNTi2ewNKZtEwFE2CY5o\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"nWyhUoxAbYI\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/ryG7_mwPOR3Ud5uwFDvxC8102pk\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf-aR4wT1Hdccu91YbM7VO3g\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"oAeotgCHL3E\"\n" +
            "   }\n" +
            "  },\n" +
            "  {\n" +
            "   \"kind\": \"youtube#playlistItem\",\n" +
            "   \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/zZnT4uJC_Gpysrx-JvwWYo7VT8A\\\"\",\n" +
            "   \"id\": \"PL4cVdv18O0HSdjkDZsvosf3Ez4vqjbolaErqWoNqQ96k\",\n" +
            "   \"contentDetails\": {\n" +
            "    \"videoId\": \"-0oZNWif_jk\"\n" +
            "   }\n" +
            "  }\n" +
            " ]\n" +
            "}";

    private String test1 = "{\n" +
            " \"kind\": \"youtube#playlistItemListResponse\",\n" +
            " \"etag\": \"\\\"eYE31WLho912TfxEBDDRSwEQ5Ms/KfOaFEkrMTTguVW3194yIECQiBU\\\"\",\n" +
            " \"pageInfo\": {\n" +
            "  \"totalResults\": 19,\n" +
            "  \"resultsPerPage\": 50\n" +
            " },\n" +
            " \"items\": [\n" +
            "  \n" +
            " ]\n" +
            "}";
}