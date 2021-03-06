package com.example.cgaima.squaa.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cgaima.squaa.Models.Event;
import com.example.cgaima.squaa.R;
import com.example.cgaima.squaa.adapters.EventAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.mapView)
    MapView gMapView;

    GoogleMap mMap;
    List<Event> events;
    EventAdapter mAdapter;
    LatLng ewc = new LatLng(37.3903,-122.0945);
    Marker marker;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int MY_LOCATION_REQUEST_CODE = 2;
    private static final String TAG = MapFragment.class.getSimpleName();

    public MapFragment() { // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            events = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView tv = toolbar.findViewById(R.id.toolbar_title);
        tv.setText("map");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        //create the map bundle
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        gMapView.onCreate(mapViewBundle);
        gMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new EventAdapter(events); //new instance of the events adapter

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

    public void getPosts() throws ParseException {
        final Event.Query query = new Event.Query();

        query.getTop(); //queries the top 25 posts in parse

        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        events.add(objects.get(i));
                        mAdapter.notifyDataSetChanged();
                        ParseGeoPoint parseGeoPoint = events.get(i).getGeoPoint();
                        LatLng mylatlng = new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude()); //"convert" the parse geopoint object to a google latlng object

                        marker = mMap.addMarker(new MarkerOptions()
                                .position(mylatlng)
                                .title(events.get(i).getEventName()) //displays event name on first click
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        );
//                        events.get(i).getEventImage().getDataInBackground(new GetDataCallback() {
//                            @Override
//                            public void done(byte[] data, ParseException e) {
//                                if (e == null) {
//                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
//                                    Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, 80, 80, false);
//                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizedBmp));
//                                }
//                            }
//                        });
                    }
                } else {
                    e.printStackTrace();
                    Log.e("MapFragement", "Sorry! Looks like we couldn't get your events :/");
                }
            }
        });
    }




    @Override
    public void onStart() {
        super.onStart();
        gMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        gMapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        gMapView.onStop();
    }
    @Override
    public void onPause() {
        gMapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
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
         mMap.moveCamera(CameraUpdateFactory.newLatLng(ewc));

         mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
             @Override
             public void onInfoWindowClick(Marker marker) {
                Log.d("MapFragment:", "The click works!");
                 for (int i = 0; i < events.size(); i++) {
                     Log.d("events null?", events.get(i).getEventName());
                     if (marker.getTitle().equals(events.get(i).getEventName())) {
                         //Intent intent = new Intent(getContext(), HomeActivity.class);
                         //startActivity(intent);


                       Fragment eventDetailActivity = EventDetailFragment.newInstance(events.get(i), null);
                       FragmentTransaction fragmentTransaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
                       fragmentTransaction.replace(R.id.fragment_container, eventDetailActivity).commit();
                     } else {
                         //Toast.makeText(getContext(), "There may be something wrong with this event. Try again later.", Toast.LENGTH_LONG).show();
                     }
                 }
             }
         });

         boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
         if (!success) {
             Log.e(TAG, "Style parsing failed.");
         }

         UiSettings uiSettings = mMap.getUiSettings();
         uiSettings.setZoomControlsEnabled(true);

         try {
             getPosts();
         } catch (ParseException e) {
             e.printStackTrace();
         }
         if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE); //request permissions
            return;
         } else {
            mMap.setMyLocationEnabled(true);
         }
         mMap.setMinZoomPreference(6);

     }

}
