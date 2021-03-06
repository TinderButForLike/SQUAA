package com.example.cgaima.squaa.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.fragments.CreateEventFragment;
import com.example.cgaima.squaa.fragments.MapFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

import butterknife.BindView;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.mapSet)
    Button mapSet;
    MapView gMapView;
    TextView mapSearch;
    String setLoc = "";
    LatLng searchLatLng;
    ParseGeoPoint mPGP = new ParseGeoPoint();


    private GoogleMap mMap; //new instance of a map
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int MY_LOCATION_REQUEST_CODE = 2;
    private static final String TAG = MapFragment.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //create the map bundle
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        gMapView = findViewById(R.id.gMapView); //bind the views.
        mapSearch = findViewById(R.id.mapSearch); //butterknife does not support map and autocomp.

        gMapView.onCreate(mapViewBundle);
        gMapView.getMapAsync(this);
        mapSet = findViewById(R.id.mapSet);

        //launch thr autocomplete places text box
        mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MapsActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e("MapsActivity", "Something not installed/enabled/or up to date");
                    Toast.makeText(MapsActivity.this, "Please update some of your Google play features and try again.", Toast.LENGTH_SHORT).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e("MapsActivity", "Something went wrong :(");
                    Toast.makeText(MapsActivity.this, "Google Services not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //move back to the create event fragment upon choosing the location and pass long some intents
        mapSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent passLoc = new Intent(MapsActivity.this, HomeActivity.class);
//                passLoc.putExtra("locationtext", setLoc);
//                passLoc.putExtra("geo", mPGP);
//                startActivity(passLoc);


                Intent intent = new Intent("infoIntent").putExtra("locationtext", setLoc).putExtra("geo", mPGP);
                LocalBroadcastManager.getInstance(MapsActivity.this).sendBroadcast(intent);
                finish();
            }
        });
    }

    @Override //allow for retrieval of previous state
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }
        gMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gMapView.onStop();
    }
    @Override
    protected void onPause() {
        gMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        gMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        gMapView.onLowMemory();
    }

    @Override //set a default map
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE); //request permissions
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setMyLocationEnabled(true);
        mMap.setMinZoomPreference(12);

        LatLng ny = new LatLng(40.7143528, -74.0059731);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("MapsActivity", "Place: " + place.getName());

                 mapSearch.setText(place.getName());
                 setLoc = place.getName() + ", \n" + place.getAddress();
                 searchLatLng = place.getLatLng();
                 mMap.moveCamera(CameraUpdateFactory.newLatLng(searchLatLng));
                 mMap.addMarker(new MarkerOptions().position(searchLatLng));

                 mPGP.setLatitude(searchLatLng.latitude);
                 mPGP.setLongitude(searchLatLng.longitude);

                //send the lat/long information to the create event fragment to display as the user creates the event
                Intent searchResultIntent = new Intent(MapsActivity.this, CreateEventFragment.class);
                searchResultIntent.putExtra("locationtext", mapSearch.getText());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Maps Activity", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
