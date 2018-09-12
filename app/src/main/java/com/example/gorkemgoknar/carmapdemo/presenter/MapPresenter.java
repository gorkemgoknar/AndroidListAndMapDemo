package com.example.gorkemgoknar.carmapdemo.presenter;

import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.google.android.gms.maps.model.LatLng;

/*
  Presenter to get placemark model and use on Map View
 */
public class MapPresenter extends AbstractPlacemarkPresenter {

    // URL to get car JSON info
    private static String url = "https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json";

    private View view;

    private LatLng userLocation;

    private boolean userLocationEnabled;

    public MapPresenter(View view){
        this.view = view;

        view.showProgress();

        //Fetch async json car list;
        new FetchData().execute();


       }

    public void refetchPlacemarks(){
        new FetchData().execute();
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

    public boolean isUserLocationEnabled() {
        return userLocationEnabled;
    }

    public void setUserLocationEnabled(boolean userLocationEnabled) {
        this.userLocationEnabled = userLocationEnabled;
    }

    public interface View{


        void populateView(Placemarks placemarks);

        void updateUserLocation(LatLng userLocation);

        //Progress bar
        void dismissProgress();
        void showProgress();

        //show popup with error
        void showNoPlacemarkError();



    }


}
