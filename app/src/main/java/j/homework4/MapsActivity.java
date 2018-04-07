package j.homework4;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
   // private double lat, lng;
    String apiKey = "key=AIzaSyBwrD7phhhbyduItSqfqjSyV3VaC2oCgSA";
    String baseURL = "https://maps.googleapis.com/maps/api/geocode/json?";
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();//start of my code
        String message = intent.getStringExtra(MainActivity.key);
        address = message;



        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sendRequest();
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public static void startRequest(String message){

    }

    private void sendRequest(){


        final TextView city = (TextView) findViewById(R.id.city);
        final TextView coord = (TextView) findViewById(R.id.coord);
        String request = baseURL + address + "&" + apiKey;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("jae", response);
                            JSONObject jsonData = new JSONObject(response);
                            String cityName = jsonData.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                            Double lat = jsonData.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            Double lng = jsonData.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            LatLng newLoc = new LatLng(lat,lng);
                            city.setText(cityName);
                            coord.setText(Double.toString(lat)+"\n"+Double.toString(lng));
                            mMap.clear();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));
                            mMap.addMarker(new MarkerOptions().position(newLoc).title("New location"));
                        }
                        catch(Exception e){
                            Log.d("error", "JSONerror");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "Error!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);



    }
}
