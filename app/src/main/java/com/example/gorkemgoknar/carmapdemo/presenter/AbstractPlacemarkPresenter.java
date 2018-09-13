package com.example.gorkemgoknar.carmapdemo.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gorkemgoknar.carmapdemo.model.GlobalApplication;
import com.example.gorkemgoknar.carmapdemo.model.Persistence;
import com.example.gorkemgoknar.carmapdemo.model.Placemarks;
import com.example.gorkemgoknar.carmapdemo.utility.HttpHandler;
import com.google.gson.Gson;

/*
  Abstract presenter for placemarks
  In this base abstract class getting json from url and storing to cache is implemented
  Each child presenter should implement is own handling method for json file as views may be
  different.

 */
public abstract class AbstractPlacemarkPresenter{


    // URL to get car JSON info
    private static String url = "https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json";

    private static String jsonCacheName = "placemarkJson";
    private static String cacheExists = "cacheExists";

    private static Placemarks placemarks ;

    //Each child should implement its own handling
    protected abstract void handlePlacemarkIsRefreshedFromNet();
    protected abstract void handlePlacemarkIsRefreshedFromCache();
    protected abstract void handleNoPlacemarkInfoError();
    protected abstract void handleNetworkError();

    //Child should implement how to fetch data and show necessary information
    //we could implement here as well but opted to show
    public abstract void fetchPlacemarks();

    //used for retriggering data from network
    public abstract void fetchPlacemarksFromNetwork();

    protected boolean getLocalCache(){
        String jsonStr = null;

        if(Persistence.getBoolKey(cacheExists)) {
            jsonStr = Persistence.getLocalJson(jsonCacheName);

        }

        if(jsonStr != null){
            //try to fill placemark with json
            populatePlacemarkFromJson(jsonStr);
            handlePlacemarkIsRefreshedFromCache();

            return true;
        }
        //No cache
        return false;
    }

    //Implement what will be done after data is fetched
    public void httpCallback(String response) {

        if (response == null){
            handleNetworkError();
            return;
        }
        //TODO Future: validate json
        //it may be possible response is not the String we want

        //Assume response is valid json format
        //Save to cache
        savePlacemarkToLocalCache(response);


        //fill placemark information
        populatePlacemarkFromJson(response);
        handlePlacemarkIsRefreshedFromNet();
    }

    public static void setPlacemarks(Placemarks placemarks) {

        AbstractPlacemarkPresenter.placemarks = placemarks;
    }

    public Placemarks getPlacemarks() {

        return placemarks;
    }

    public void removeJsonCache(){
        Persistence.removeKey(jsonCacheName);
        Persistence.setBoolKey(cacheExists,false);

    }
    public boolean placemarksExistsInCache() {

        return Persistence.getBoolKey(cacheExists);

    }

    //Save information to cache for getting faster next time
    private void savePlacemarkToLocalCache(String jsonStr){
        Persistence.persistJson(jsonCacheName, jsonStr);
        Persistence.setBoolKey(cacheExists, true);

    }


    public String getJsonFromLocalCache(){
        if (Persistence.getBoolKey(cacheExists)){

            return Persistence.getLocalJson(jsonCacheName);
        }
        else {
            return null;
        }
    }




    //fill placemark information from json
    protected void populatePlacemarkFromJson(String jsonStr){

        if (jsonStr == null || jsonStr.isEmpty())
        {
            this.setPlacemarks(null);

            return;
        }
        //Provided json string is our format now we can populate placemark information
        Gson gson = new Gson();

        //populate placemark
        this.setPlacemarks(gson.fromJson(jsonStr, Placemarks.class));

        return;
    }

    public boolean isConnectedToNetwork(){

        Context context = GlobalApplication.getAppContext();

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    public class FetchData extends AsyncTask<Void, Void, Void> {

        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //Connect to url to get json
            HttpHandler sh = new HttpHandler();

            response = null;

            try {
                response = sh.makeServiceCall(url);

            } catch (Exception e) {
                Log.e("Data Fetch", "Could not fetch data:" + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            httpCallback(response);


        }

    }


}
