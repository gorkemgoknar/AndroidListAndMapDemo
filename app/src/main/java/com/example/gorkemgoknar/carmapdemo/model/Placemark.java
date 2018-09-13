package com.example.gorkemgoknar.carmapdemo.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/*
 Placemark model will be used to hold car place information

 */
public class Placemark {


    @SerializedName("name")
    private String name ;

    @SerializedName("address")
    private String address;

    @SerializedName("coordinates")
    private Double[] coordinates;

    @SerializedName("engineType")
    private String engineType;

    @SerializedName("exterior")
    private String exterior;

    @SerializedName("fuel")
    private Integer fuel;

    @SerializedName("interior")
    private String interior;

    @SerializedName("vin")
    private String vin;

    public Placemark(String name, Double[] coordinates){
        this.name = name;
        this.coordinates = coordinates;

        this.engineType = "N/A";
        this.exterior = "N/A";
        this.fuel = 0;
        this.interior = "N/A";
        this.address = "N/A";
        this.vin = "N/A";

    }

    public String getCoordinateAsString(){
        Double lat = this.coordinates[1];
        Double lon = this.coordinates[0];

        return (lat.toString() + "," + lon.toString());

    }

    //return coordinate info as LatLng format
    public LatLng getCoordinateAsLatLng(){
        return new LatLng(this.coordinates[1], this.coordinates[0]);

    }
    public double getLatitude(){
        return this.coordinates[1];
    }

    public double getLongitude(){
        return this.coordinates[0];

    }


    public Placemark(String name,  Double[] coordinates, String engineType, String exterior, Integer fuel, String interior, String vin,String address) {
        this.address = address;
        this.coordinates = coordinates;

        this.engineType = engineType;
        this.exterior = exterior;
        this.fuel = fuel;
        this.interior = interior;
        this.name = name;
        this.vin = vin;
    }


    @Override
    public String toString(){
        return "Name:" + this.name + " coordinates:" + this.getLatitude() + ";" + this.getLongitude();

    }
    public String getAddress() {
        return address;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public String getEngineType() {
        return engineType;
    }

    public String getExterior() {
        return exterior;
    }

    public Integer getFuel() {
        return fuel;
    }

    public String getInterior() {
        return interior;
    }

    public String getName() {
        return name;
    }

    public String getVin() {
        return vin;
    }


}
