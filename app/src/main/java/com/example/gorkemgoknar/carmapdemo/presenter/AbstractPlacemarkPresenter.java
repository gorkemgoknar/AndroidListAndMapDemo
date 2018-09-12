package com.example.gorkemgoknar.carmapdemo.presenter;

import android.os.AsyncTask;
import android.util.Log;

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

    private static Placemarks placemarks ;

    //Each child should implement its own handling
    protected abstract void handlePlacemarkIsRefreshedFromNet();
    protected abstract void handlePlacemarkIsRefreshedFromCache();
    protected abstract void handleNoPlacemarkInfoError();


    //Implement what will be done after data is fetched
    public void httpCallback(String response) {
        if (response == null){
            //could not fetch json
            //if any local cache use it
            String jsonStr = Persistence.getLocalJson("placemarkJson");

            if(jsonStr != null){
                //try to fill placemark with json
                handlePlacemarkIsRefreshedFromCache();

            } else {
                handleNoPlacemarkInfoError();
            }

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

    public boolean placemarksExistsInCache() {

        return Persistence.isPlacemarkInfoAvailable();

    }

    //Save information to cache for getting faster next time
    private void savePlacemarkToLocalCache(String jsonStr){
        Persistence.persistJson("placemarkJson", jsonStr);
    }


    //fill placemark information from json
    protected void populatePlacemarkFromJson(String jsonStr){

        //Provided json string is our format now we can populate placemark information
        Gson gson = new Gson();

        //populate placemark
        placemarks = gson.fromJson(jsonStr, Placemarks.class);

        //mark placemark is fetched
        Persistence.setPlacemarksAvailable(true);

        return;
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
