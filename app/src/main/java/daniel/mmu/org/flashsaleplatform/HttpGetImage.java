package daniel.mmu.org.flashsaleplatform;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HttpGetImage extends AsyncTask<String, Integer, Bitmap> {

    private Context context;

    public HttpGetImage(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {

        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.drawable.sale);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    "android.permission.ACCESS_NETWORK_STATE"}, 1);
        } else {
            try {

                HttpURLConnection connection = (HttpURLConnection) new URL(strings[0]).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                image = BitmapFactory.decodeStream(input);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }
}
