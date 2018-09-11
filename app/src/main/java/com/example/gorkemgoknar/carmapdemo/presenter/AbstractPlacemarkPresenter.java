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
public abstract class AbstractPlacemarkPresenter {

    // URL to get car JSON info
    private static String url = "https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json";

    private static Placemarks placemarks ;

    //Each child should implement its own handling
    //e.g. passing to view
    protected abstract void handlePostJson();

    public boolean isFetchingFromLocal() {
        return Persistence.isPlacemarkInfoAvailable();

    }

    public Placemarks getPlacemarks() {

        return placemarks;
    }

    /**
     * Async task class to get json by making HTTP call
     */

    class FetchPlacemarks extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TODO show some progress dialog or info
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            String jsonStr = Persistence.getLocalJson("placemarkJson");

            if (jsonStr == null) {
                Log.d("App","First time running...");

                //It is the first time this app launches
                //Connect to url to get json
                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                //TODO: handle no internet connection

                jsonStr = null;

                try {
                    jsonStr = sh.makeServiceCall(url);

                    //Persist this json to shared preferences for simple storage
                    Persistence.persistJson("placemarkJson", jsonStr);
                    Log.d("JSON","Placemarks saved");

                } catch (Exception e) {
                    Log.e("JSON Fetch", "Exception:" + e.toString());
                }
            } else {
                //Provided json string is our format now we can populate placemark information
                Gson gson = new Gson();

                //populate placemark
                placemarks = gson.fromJson(jsonStr, Placemarks.class);

                if (placemarks != null) {

                    //will send placemark info on post execute
                    /*
                    for (Placemark placemark : placemarks.getPlacemarks()) {
                        Log.e("Placemark:", placemark.toString());
                    }
                    */

                } else {
                    //some fatal error occured could not get json
                    Log.e("JSON Error", "Cannot get placemark from json");

                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //TODO disable loader progress bar

            //Done fetching populate list view
            handlePostJson();

          //  view.populateView(placemarks);


        }
    }

}
