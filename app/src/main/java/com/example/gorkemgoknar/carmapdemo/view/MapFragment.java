package com.example.gorkemgoknar.carmapdemo.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.example.gorkemgoknar.carmapdemo.model.Placemark;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.example.gorkemgoknar.carmapdemo.presenter.ListPresenter;
import com.example.gorkemgoknar.carmapdemo.presenter.MapPresenter;
import com.example.gorkemgoknar.carmapdemo.utility.HttpHandler;
import com.example.gorkemgoknar.carmapdemo.view.adapters.CustomInfoWindowAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/*
   Handles showing placemarks in Google Mapview
 */

//TODO : better inherit list and map fragments from common base for same functionality

public class MapFragment extends Fragment implements MapPresenter.View,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();

    private MapPresenter presenter;

    MapView mMapView;
    private GoogleMap map;

    //Provide a list of markers on hashmap
    //Note may use markers as array as well as we are using marker.getId() for String
    //But it is possible that we may use other ID like name for each marker

    private HashMap<String, Marker> markers = new HashMap<String, Marker>();

    //current marker that info window is open (is clicked)
    private Marker currentMarker;

    private ProgressDialog progress;

    private String progressMessage;

    private boolean dataReady;
    private boolean mapReady;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //only true when population is called and cleared if put to map
        dataReady = false;

        //until map is ready false
        mapReady = false;


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

        progressMessage = "Fetching new data..";

        fetchData();

        return rootView;
    }

    //can be called from outside the class too
    public void fetchData(){
        presenter.fetchPlacemarks();
    }

    public void fetchDataFromNet(){
        Log.i(TAG,"fetch data");
        presenter.fetchPlacemarksFromNetwork();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        mapReady = true;

        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.getContext()));

        try{
            //if location permission is given this will pass
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnMyLocationClickListener(this);
        } catch (SecurityException e)  {
            Log.e("Location","Location permission NOT given");

        }


        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);

        map.getUiSettings().setMyLocationButtonEnabled(true);


        // For dropping a marker at a point on the Map
        LatLng hamburg = new LatLng(53.550598, 9.992286);

        map.addMarker(new MarkerOptions().position(hamburg).title("Hamburg").snippet("Town house"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(hamburg).zoom(12).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (dataReady){
            //there is pending data to show
            //populate it
            this.populateView(presenter.getPlacemarks());
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            marker.hideInfoWindow();
            showAllMarkers();

        } catch (NullPointerException e)
        {
            Log.e(TAG, "Marker Info Null Pointer exception");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            if (currentMarker != null) {
                //TODO: marker.isInfoWindowShown() is not working correctly
                //using a current marker reference instead

                marker.hideInfoWindow();
                currentMarker = null;
                showAllMarkers();
            } else {
                //First time clicking this marker
                //get this marker
                showOnlyThisMarker(marker);

                //animate to this marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(marker.getPosition()).zoom(12).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                currentMarker  = marker;

                marker.showInfoWindow();

            }
        }catch (NullPointerException e){
            Log.e(TAG, "marker onclick Null Pointer" + e.getMessage());

        }

        //Important must return true to actually use overriden function
        return true;

    }

    private void showOnlyThisMarker(Marker marker){
        //hide all markers except this
        try {
            for (Map.Entry<String, Marker> markerEntry : markers.entrySet()) {

                if (markerEntry.getValue().getId().equals(marker.getId())){
                    markerEntry.getValue().setVisible(true);
                } else {
                    markerEntry.getValue().setVisible(false);

                }

            }

        }catch (NullPointerException e){
            Log.e(TAG,"Error while showing all markers : " + e.getMessage());
        }

    }


    private void showAllMarkers(){

        //Show all markers
        try {
            for (Map.Entry<String, Marker> markerEntry : markers.entrySet()) {
                markerEntry.getValue().setVisible(true);
            }

        }catch (NullPointerException e){
            Log.e(TAG,"Error while showing all markers : " + e.getMessage());
        }

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

    //Will be called once json is ready
    //or data is refetched from network
    @Override
    public void populateView(Placemarks placemarks) {


        if (placemarks == null){
            //no car placemark info was fetched likely network error
            this.showNoPlacemarkError();
            return;
        }

        if (placemarks.getSize() == 0){
            //Info fetched contained no car
            this.showNoPlacemarkError();
            return;
        }

        //there is some information to show
        dataReady = true;

        //it is possible that we are fetching from cache
        //and google map is not initialized yet

        try {
            //clear map
            map.clear();
        }catch (NullPointerException e){
            //Map was not yet initialized.
            //we can access placemarks from presenter later
            //tag as there is data but no map
            return;

        }

        //new positions coming clear current
        currentMarker = null;

        //clear our marker container
        if (markers != null) {
            markers.clear();
        }

        for (Placemark placemark : placemarks.getPlacemarks()) {

            Marker marker = map.addMarker(new MarkerOptions().position(placemark.getCoordinateAsLatLng()).
                                title(placemark.getName()).
                                snippet("Engine: " + placemark.getEngineType() + "\nVin:" +placemark.getVin())

                                );

            // Changing marker icon to car!
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon_bitmap));


            //each marker should have a auto unique id so can use it to access later for show/hide
            markers.put(marker.getId(), marker);
        }
        //done with original data
        dataReady = false;

        //Show all markers on map
        moveCameraToMarkerBounds();



    }

    private void moveCameraToMarkerBounds(){
        //build bounds for all the markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(Map.Entry<String, Marker> markerEntry : markers.entrySet() ){
            builder.include(markerEntry.getValue().getPosition());
        }
        LatLngBounds bounds = builder.build();

        //Get camera bounds
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        //animate movement to bounds
        map.moveCamera(cameraUpdate);
        //alternative use map.animateCamera(cameraUpdate) for animation


    }

    @Override
    public void updateUserLocation(LatLng userLocation) {

        //Maybe presenter would like to do something with userlocation change
        presenter.userLocationUpdated(userLocation);

    }

    public void dismissProgress(){
        if(this.progress != null) {
            this.progress.dismiss();
        }
    }
    public void showProgress(){
        progress = ProgressDialog.show(getActivity(), "Fetching Car Data",
                progressMessage, true);
    }


    public void showNoPlacemarkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getResources().getString(R.string.title_noplacemark));
        builder.setMessage(getResources().getString(R.string.message_noplacemark));
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //retry fetching
                fetchDataFromNet();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showNetworkError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getResources().getString(R.string.title_network_unreachable));
        builder.setMessage(getResources().getString(R.string.message_network_unreachable));
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                //retry fetching
                fetchData();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}