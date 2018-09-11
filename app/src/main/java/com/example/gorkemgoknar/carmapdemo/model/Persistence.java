package com.example.gorkemgoknar.carmapdemo.model;

import android.content.Context;
import android.util.Log;

import com.example.gorkemgoknar.carmapdemo.utility.TinyDB;

/*
   Persistence used to save and load Json file from Shared preferences
   Uses Tinydb for shared preference storage
 */
public class Persistence {

    //Save json file to local preference
    public static void persistJson(String key, String value ){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);
        tinydb.putString(key, value);
    }

    //get json file from local preferences
    public static String getLocalJson(String key ){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);
        String jsonStr;
        try{
            jsonStr = tinydb.getString(key);
        } catch(Exception e){
            return null;
        }

        return jsonStr;
    }

    //Set FirstRun value of the application
    public static void setPlacemarksAvailable(boolean val){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);
        tinydb.putBoolean("placemarkAvailable", val);
    }


    //Check if this is the first time app runs
    public static boolean isPlacemarkInfoAvailable(){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);
        Boolean placemarkAvailable = false;

        try {
            placemarkAvailable = tinydb.getBoolean("placemarkAvailable");
        }
        catch (Exception e){
            Log.d("Placamark Available","Key not existing");

            setPlacemarksAvailable(false);

        }

        return placemarkAvailable;

    }

}
