package com.example.gorkemgoknar.carmapdemo.model;

import java.util.List;

/*
   Placemarks used to store placemark information
 */
public class Placemarks {

    private List<Placemark> placemarks;

    public List<Placemark> getPlacemarks() {

        return placemarks;
    }

    public int getSize(){

        return this.placemarks.size();
    }


}
