/*
 * Copyright (c) 2017 Ahmed-Abdelmeged
 *
 * github: https://github.com/Ahmed-Abdelmeged
 * email: ahmed.abdelmeged.vm@gamil.com
 * Facebook: https://www.facebook.com/ven.rto
 * Twitter: https://twitter.com/A_K_Abd_Elmeged
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.mego.adas.model;

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
