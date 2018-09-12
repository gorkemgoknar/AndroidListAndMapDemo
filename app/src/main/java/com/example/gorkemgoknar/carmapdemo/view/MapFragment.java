package com.example.gorkemgoknar.carmapdemo.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gorkemgoknar.carmapdemo.R;
import com.example.gorkemgoknar.carmapdemo.model.Persistence;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.example.gorkemgoknar.carmapdemo.presenter.ListPresenter;
import com.example.gorkemgoknar.carmapdemo.presenter.MapPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/*
   Handles showing placemarks in Google Mapview
 */
public class MapFragment extends Fragment implements MapPresenter.View,GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {


    private MapPresenter presenter;

    MapView mMapView;
    private GoogleMap map;

    private ProgressDialog progress;

    private String progressMessage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        presenter = new MapPresenter(this);

        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);


        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e("Map failure", "Can not initialize map");
            e.printStackTrace();
        }


        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        if (Persistence.getLocationPermission()){
            Log.e("Location","Location permission given");
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
        } else {
            Log.e("Location","Location permission NOT given");
            //we will not request anymore
        }


        map.getUiSettings().setMyLocationButtonEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng hamburg = new LatLng(53.550598, 9.992286);

        map.addMarker(new MarkerOptions().position(hamburg).title("Hamburg").snippet("Town house"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(hamburg).zoom(12).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("MAP VIEW");
    }

    @Override
    public void populateView(Placemarks placemarks) {

    }



    @Override
    public void updateUserLocation(LatLng userLocation) {

    }

    public void dismissProgress(){
        progress.dismiss();
    }
    public void showProgress(){
        progress = ProgressDialog.show(getActivity(), "Fetching Car Data",
                progressMessage, true);
    }

    public void showNoPlacemarkError(){
        Toast.makeText(getActivity(), "No Placemark to show please retry later..", Toast.LENGTH_LONG).show();
    }

}