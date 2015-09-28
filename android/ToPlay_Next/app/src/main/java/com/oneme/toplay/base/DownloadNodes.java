package com.oneme.toplay.base;

import android.content.Context;
import android.os.AsyncTask;
//import android.util.Log;

import com.oneme.toplay.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;


public class DownloadNodes extends AsyncTask<Void, Void, Void> {

    private static class JsonReader {
        private static String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }
    }

    final String[] nodeDetails = new String[7];
    final String TAG = "DHTNODEDETAILS";
    Context mcontext;

    public DownloadNodes(Context context) {
        this.mcontext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            /* Get nodes from json-formatted data */
            JSONObject json = JsonReader.readJsonFromUrl("https://dist-build.tox.im/Nodefile.json");//"https://s3.amazonaws.com/www.onemee.net/Nodefile.json");//"https://dist-build.tox.im/Nodefile.json");//"https://s3.amazonaws.com/www.onemee.net/Nodefile.json");
            if (Application.APPDEBUG) {
                //Log.d(TAG, json.toString());
            }
            JSONArray serverArray = json.getJSONArray("servers");
            for(int i = 0; i < serverArray.length(); i++) {
                JSONObject jsonObject = serverArray.getJSONObject(i);
                DHTNodes.owner.add(jsonObject.getString("owner"));
                DHTNodes.ipv6.add(jsonObject.getString("ipv6"));
                DHTNodes.key.add(jsonObject.getString("pubkey"));
                DHTNodes.ipv4.add(jsonObject.getString("ipv4"));
                DHTNodes.port.add(String.valueOf(jsonObject.getInt("port")));
            }

            if (Application.APPDEBUG) {
                //Log.d(TAG, "Nodes fetched from online");
            }

        } catch (Exception exp) {
            if (Application.APPDEBUG) {
               // Log.d(TAG, "Failed to connect to Tox CDN for nodes");
            }

            DHTNodes.ipv4.add("192.254.75.98");
            com.oneme.toplay.base.DHTNodes.ipv6.add("2607:5600:284::2");
            DHTNodes.owner.add("stqism");
            DHTNodes.port.add("33445");
            com.oneme.toplay.base.DHTNodes.key.add("951C88B7E75C867418ACDB5D273821372BB5BD652740BCDF623A4FA293E75D2F");

            com.oneme.toplay.base.DHTNodes.ipv4.add("144.76.60.215");
            com.oneme.toplay.base.DHTNodes.ipv6.add("2a01:4f8:191:64d6::1");
            DHTNodes.owner.add("sonofra");
            DHTNodes.port.add("33445");
            DHTNodes.key.add("04119E835DF3E78BACF0F84235B300546AF8B936F035185E2A8E9E0A67C8924F");

            DHTNodes.ipv4.add("37.187.46.132");
            DHTNodes.ipv6.add("2001:41d0:0052:0300::0507");
            DHTNodes.owner.add("mouseym");
            com.oneme.toplay.base.DHTNodes.port.add("33445");
            DHTNodes.key.add("A9D98212B3F972BD11DA52BEB0658C326FCCC1BFD49F347F9C2D3D8B61E1B927");

            DHTNodes.ipv4.add("37.59.102.176");
            DHTNodes.ipv6.add("2001:41d0:51:1:0:0:0:cc");
            com.oneme.toplay.base.DHTNodes.owner.add("astonex");
            com.oneme.toplay.base.DHTNodes.port.add("33445");
            com.oneme.toplay.base.DHTNodes.key.add("B98A2CEAA6C6A2FADC2C3632D284318B60FE5375CCB41EFA081AB67F500C1B0B");

            DHTNodes.ipv4.add("54.199.139.199");
            DHTNodes.ipv6.add("");
            DHTNodes.owner.add("aitjcize");
            DHTNodes.port.add("33445");
            DHTNodes.key.add("7F9C31FE850E97CEFD4C4591DF93FC757C7C12549DDD55F8EEAECC34FE76C029");
        }

        if (Application.APPDEBUG) {
            //Log.d(TAG, "DhtNode size: " + DHTNodes.ipv4.size());
        }

        return null;
    }
}
