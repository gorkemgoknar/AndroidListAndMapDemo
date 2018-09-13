package com.example.gorkemgoknar.carmapdemo.presenter;

import android.util.Log;

import com.example.gorkemgoknar.carmapdemo.model.Persistence;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.google.android.gms.maps.model.LatLng;

/*
  Presenter to get placemark model and use on Map View
 */
public class MapPresenter extends AbstractPlacemarkPresenter {

    private View view;

    private LatLng userLocation;

    private boolean userLocationEnabled;

    public MapPresenter(View view){
        this.view = view;
    }

    /**
     * Implementation of abstact methods
     */
    protected void handlePlacemarkIsRefreshedFromNet(){
        view.populateView(this.getPlacemarks());
        view.dismissProgress();
    }
    protected void handlePlacemarkIsRefreshedFromCache(){
        view.populateView(this.getPlacemarks());
        view.dismissProgress();
    }

    protected void handleNetworkError(){
        view.dismissProgress();
        view.showNetworkError();
    }

    protected void handleNoPlacemarkInfoError(){
        view.dismissProgress();
        view.showNoPlacemarkError();
    }


    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public void userLocationUpdated(LatLng userLocation){

        this.setUserLocation(userLocation);
        //TODO: do something on the new location
        //maybe update distance to selected item

    }
    public boolean isUserLocationEnabled() {

       return Persistence.getLocationPermission();

    }

    public void setUserLocationEnabled(boolean userLocationEnabled) {
        Persistence.setLocationPermission(true);
    }

    @Override
    public void fetchPlacemarks(){

        if (!isConnectedToNetwork()){
            //show error in no connection to network

            handleNoPlacemarkInfoError();
            return;
        }
        //TODO: Also check if connected to internet

        //if cache exists get it from there
        if (getLocalCache()){
            handlePlacemarkIsRefreshedFromCache();

            return;
        }

        view.showProgress();
        new FetchData().execute();
    }

    @Override
    public void fetchPlacemarksFromNetwork(){

        if (!isConnectedToNetwork()){
            //show error in no connection to network

            handleNoPlacemarkInfoError();
            return;
        }

        view.showProgress();
        new FetchData().execute();
    }


    public interface View{


        void populateView(Placemarks placemarks);

        void updateUserLocation(LatLng userLocation);

        //Progress bar
        void dismissProgress();
        void showProgress();

        //show popup with error
        void showNoPlacemarkError();
        void showNetworkError();



    }


}
