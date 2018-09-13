package com.example.gorkemgoknar.carmapdemo.presenter;

import android.util.Log;

import com.example.gorkemgoknar.carmapdemo.model.Persistence;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Gorkem Goknar 2018
 */
/*
  Presenter to get placemark model and use on List View

 */
public class ListPresenter extends AbstractPlacemarkPresenter {


    private View view;

    public ListPresenter(View view) {

        this.view = view;

    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * fetchPlacemarks main function to get data
     * If local cache is available uses it else fetches from net
     */
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

    //writing abstract method
    protected void handlePostJson(){

        view.populateView(this.getPlacemarks());
    }

    public interface View{

        void populateView(Placemarks placemarks);
        void dismissProgress();
        void showProgress();

        void showNoPlacemarkError();
        void showNetworkError();



    }


}
