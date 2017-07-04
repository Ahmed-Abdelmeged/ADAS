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


package com.example.mego.adas.utils;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;

public final class LocationUtilities {

    /**
     * Request code for location permission request.
     */
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 8;


    /**
     * helper method to check the permission and find the current location
     *
     * @param activity        the activity that will request the permission
     * @param googleApiClient client for access google apis
     * @return the current location object
     */
    public static Location enableMyLocation(Activity activity, GoogleApiClient googleApiClient) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return LocationServices.FusedLocationApi.getLastLocation(
                    googleApiClient);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        return null;
    }

    /**
     * helper method to check the permission and find the current location
     *
     * @param activity the activity that will request the permission
     * @param map      google map object for the current displayed map
     */
    public static void enableSetLocation(Activity activity, GoogleMap map) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * helper method to check the permission and find the current location
     *
     * @param activity         the activity that will request the permission
     * @param googleApiClient  client for access google apis
     * @param locationRequest  location request to request user current location
     * @param locationListener listener for location changes
     */
    public static void enableUpdateMyLocation(Activity activity, GoogleApiClient googleApiClient,
                                              LocationRequest locationRequest, LocationListener locationListener) {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Method to get the lat , lang from the places API
     * And format to be call in the direction API request
     *
     * @param place from google place picker
     * @return formatted longitude and latitude
     */
    public static String getLatLang(Place place) {
        if (place != null) {
            String latLang = String.valueOf(place.getLatLng());
            String goingLatLang = "";
            for (int i = 0; i < latLang.length(); i++) {
                if (latLang.charAt(i) == '(') {
                    goingLatLang = latLang.substring(i + 1, latLang.length() - 1);
                }
            }
            return goingLatLang;
        }
        return null;
    }
}
