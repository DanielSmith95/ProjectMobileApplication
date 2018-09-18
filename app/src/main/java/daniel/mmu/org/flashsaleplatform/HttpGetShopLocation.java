package daniel.mmu.org.flashsaleplatform;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpGetShopLocation extends AsyncTask<String, Integer, String> {

    private Context context;

    public HttpGetShopLocation(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        String line = "";

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    "android.permission.ACCESS_NETWORK_STATE"}, 1);
        } else {
            try {
                URL myURL = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) myURL.openConnection();
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                line = br.readLine();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }
}
