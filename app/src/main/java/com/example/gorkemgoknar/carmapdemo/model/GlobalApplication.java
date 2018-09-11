package com.example.gorkemgoknar.carmapdemo.model;

import android.app.Application;
import android.content.Context;

/*
   Global storage for application context
 */
public class GlobalApplication extends Application {

    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //do other classes initilization needing application context here.

    }

    public static Context getAppContext() {
        return context;
    }
}