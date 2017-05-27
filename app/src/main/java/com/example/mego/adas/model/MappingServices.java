package com.example.mego.adas.model;

/**
 * Created by Mego on 2/24/2017.
 */

/**
 * this a class of the  location states from the car to the user
 */
public class MappingServices {


    private double longitude;
    private double latitude;
    private int onConnectedFlag;
    private int onLocationChangedFlag;


    /**
     * Use the constructor to create new mapping services object
     *
     * @param longitude
     * @param latitude
     * @param onConnectedFlag
     * @param onLocationChangedFlag
     */
    public MappingServices(double longitude, double latitude, int onConnectedFlag, int onLocationChangedFlag) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.onConnectedFlag = onConnectedFlag;
        this.onLocationChangedFlag = onLocationChangedFlag;
    }



    public MappingServices() {
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public int getOnConnectedFlag() {
        return onConnectedFlag;
    }

    public int getOnLocationChangedFlag() {
        return onLocationChangedFlag;
    }


    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setOnConnectedFlag(int onConnectedFlag) {
        this.onConnectedFlag = onConnectedFlag;
    }

    public void setOnLocationChangedFlag(int onLocationChangedFlag) {
        this.onLocationChangedFlag = onLocationChangedFlag;
    }
}
