package daniel.mmu.org.flashsaleplatform;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout scrollLayout;
    GridLayout scrollGrid;

    Bundle mapBundle;
    Bundle saleBundle;
    Sale intentSale;

    ArrayList<Sale> salesList;

    HashMap<Button, Integer> categoriesHash;
    ArrayList<Category> categories;

    HashMap<Button, Integer> salesHash;
    int salesCount;

    double latitude;
    double longitude;

    int height;
    int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //locationInfo = new LocationInfo(MainActivity.this);
        //locationInfo.getLocation();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    "android.permission.ACCESS_FINE_LOCATION"}, 1);
        } else {
            //Creates a new LocationManager and constantly updates the latitude and longitude
            //variables with the user's current position.
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                }

                @Override
                public void onProviderEnabled(String s) {
                }

                @Override
                public void onProviderDisabled(String s) {
                }
            });
        }

        scrollGrid = findViewById(R.id.scrollGrid);
        scrollLayout = findViewById(R.id.scrollLayout);

        mapBundle = new Bundle();
        saleBundle = new Bundle();

        salesList = new ArrayList<>();
        salesHash = new HashMap<>();

        categories = new ArrayList<>();
        categoriesHash = new HashMap<>();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        initialiseCategories();
        initialiseSales("https://flashsaleplatform.appspot.com/utils/getAllSales");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMapsClick(View v) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("LATITUDE", latitude);
        intent.putExtra("LONGITUDE", longitude);
        mapBundle.putSerializable("SALES", salesList);
        intent.putExtra("MAP_BUNDLE", mapBundle);
        startActivity(intent);
    }

    private void initialiseCategories() {
        try {
            categories = new HttpGetCategories(MainActivity.this).execute("https://flashsaleplatform.appspot.com/utils/getAllCategories").get();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        Button returnButton = new Button(this);
        returnButton.setText(R.string.all_sales);
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                initialiseSales("https://flashsaleplatform.appspot.com/utils/getAllSales");
            }
        });
        scrollLayout.addView(returnButton);

        Button[] buttons = new Button[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            buttons[i] = new Button(this);
            buttons[i].setText(categories.get(i).getCategoryName());
            categoriesHash.put(buttons[i], i);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Category category = categories.get(categoriesHash.get(v));
                    initialiseSales("https://flashsaleplatform.appspot.com/utils/getSalesByCategory?saleCategory=" + Integer.toString(category.getCategoryId()));
                }
            });
            scrollLayout.addView(buttons[i]);
        }
    }

    private void initialiseSales(String url) {
        scrollGrid.removeAllViews();
        salesList = getSales(url);
        Button[] buttons = new Button[salesList.size()];
        for (int i = 0; i < salesList.size(); i++) {
            intentSale = salesList.get(i);
            salesCount = i;
            buttons[i] = new Button(this);
            buttons[i].setText(intentSale.getSaleTitle());
            buttons[i].setGravity(Gravity.BOTTOM);
            buttons[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            GridLayout.LayoutParams buttonParams = new GridLayout.LayoutParams();
            buttonParams.setMargins(0, 0, 0, 50);
            buttonParams.setGravity(Gravity.CENTER_HORIZONTAL);
            buttons[i].setLayoutParams(buttonParams);

            if (intentSale.getSaleImage() == "no-image") {
                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sale);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height / 4, true);
                BitmapDrawable saleImage = new BitmapDrawable(getResources(), scaledBitmap);
                buttons[i].setHeight(height / 4);
                buttons[i].setWidth(width);
                buttons[i].setBackground(saleImage);
            }
            else {
                try {
                    Bitmap image = new HttpGetImage(MainActivity.this).execute(intentSale.getSaleImage()).get();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, image.getWidth(), height / 4, true);
                    BitmapDrawable saleImage = new BitmapDrawable(getResources(), scaledBitmap);
                    buttons[i].setHeight(height / 4);
                    if (image.getWidth() < width / 2) {
                        buttons[i].setWidth(width / 2);
                    }
                    else {
                        buttons[i].setWidth(image.getWidth());
                    }
                    buttons[i].setBackground(saleImage);
                }
                catch (ExecutionException ee) {
                    ee.printStackTrace();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }

            salesHash.put(buttons[i], i);
            buttons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SaleActivity.class);
                    intentSale = salesList.get(salesHash.get(v));
                    saleBundle.putSerializable("SALE", intentSale);
                    intent.putExtra("SALE_BUNDLE", saleBundle);
                    startActivity(intent);
                }
            });
            scrollGrid.addView(buttons[i]);
        }
    }

    private ArrayList<Sale> getSales(String url) {
        ArrayList<Sale> salesList = new ArrayList<>();
        try {
            salesList = new HttpGetAllSales(MainActivity.this).execute(url).get();
        } catch (ExecutionException ee) {
            ee.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        return salesList;
    }
}

