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

public class HttpGetCategories extends AsyncTask<String, Integer, ArrayList<Category>> {

    private Context context;

    public HttpGetCategories(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Category> doInBackground(String... strings) {

        ArrayList<Category> categories = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{
                    "android.permission.ACCESS_NETWORK_STATE"}, 1);
        } else {
            try {
                URL myURL = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) myURL.openConnection();
                InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader br = new BufferedReader(isr);

                String line;
                while ((line = br.readLine()) != null) {
                    JSONArray jsonArray = new JSONArray(line);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Category category = new Category();
                        category.setCategoryId(jsonObject.getInt("categoryId"));
                        category.setCategoryName(jsonObject.getString("categoryName"));
                        categories.add(category);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }
}
