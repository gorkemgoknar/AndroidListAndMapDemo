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

    public static void setLocationPermission(boolean val){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);
        tinydb.putBoolean("locationPermission", val);
    }

    public static boolean getLocationPermission(){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);

        try {
            return tinydb.getBoolean("locationPermission");
        }catch (Exception e){
            //key does not exists likely
            return false;
        }


    }


    public static boolean getBoolKey(String key){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);

        try {
            return tinydb.getBoolean(key);
        } catch (Exception e)
        {
            return false;
        }
    }

    public static void setBoolKey(String key, Boolean val){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);
        //TODO: handle null
        tinydb.putBoolean(key, val);

        return;

    }
    //remove key
    public static void removeKey(String key){
        Context context = GlobalApplication.getAppContext();
        TinyDB tinydb = new TinyDB(context);

        //clear everything..
        tinydb.remove(key);

    }

}
