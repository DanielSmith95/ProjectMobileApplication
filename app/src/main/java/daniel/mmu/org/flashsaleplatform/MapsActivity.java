package daniel.mmu.org.flashsaleplatform;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private HashMap<Marker, Integer> mHashMap = new HashMap<>();

    Bundle saleBundle;
    Sale sale;

    Bundle mapBundle;
    ArrayList<Sale> salesList;
    double latitude;
    double longitude;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        saleBundle = new Bundle();

        mapBundle = getIntent().getBundleExtra("MAP_BUNDLE");
        salesList = (ArrayList<Sale>) mapBundle.getSerializable("SALES");
        latitude = getIntent().getExtras().getDouble("LATITUDE");
        longitude = getIntent().getExtras().getDouble("LONGITUDE");

        geocoder = new Geocoder(MapsActivity.this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Sale intentSale = salesList.get(mHashMap.get(marker));
                Intent intent = new Intent(MapsActivity.this, SaleActivity.class);
                saleBundle.putSerializable("SALE", intentSale);
                intent.putExtra("SALE_BUNDLE", saleBundle);
                startActivity(intent);
            }
        });

        LatLng currentLoc = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLoc).title("Current Location"));

        for (int i = 0; i < salesList.size(); i++) {
            sale = salesList.get(i);
            int shopId = sale.getShopId();
            try {
                String shopLocation = new HttpGetShopLocation(MapsActivity.this).execute("https://flashsaleplatform.appspot.com/utils/getShopLocation?shopId=" + Integer.toString(shopId)).get();
                List<Address> addresses = geocoder.getFromLocationName(shopLocation, 1);
                if (!addresses.isEmpty()) {
                    double saleLatitude = addresses.get(0).getLatitude();
                    double saleLongitude = addresses.get(0).getLongitude();
                    LatLng saleLoc = new LatLng(saleLatitude, saleLongitude);
                    Marker myMarker = mMap.addMarker(new MarkerOptions().position(saleLoc).title(sale.getSaleTitle()));
                    mHashMap.put(myMarker, i);
                }
            } catch (ExecutionException ee) {
                ee.printStackTrace();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        CameraUpdate setCamera = CameraUpdateFactory.newLatLngZoom(currentLoc, 15);
        mMap.animateCamera(setCamera);
    }
}
