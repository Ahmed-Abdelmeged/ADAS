package com.example.mego.adas.utils;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.mego.adas.api.DirectionsAPI;
import com.example.mego.adas.model.Accident;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Class offer utility methods to location services
 */
public class LocationUtilities {

    /**
     * Request code for location permission request.
     */
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 5;

    /**
     * helper method to check the response code form the direction API Response State
     */
    public static String checkResponseState(String statues) {
        switch (statues) {
            case DirectionsAPI.STATUES_INVALID_REQUEST:
                return (DirectionsAPI.STATUES_INVALID_REQUEST);
            case DirectionsAPI.STATUES_MAX_ROUTE_LENGTH_EXCEEDED:
                return (DirectionsAPI.STATUES_MAX_ROUTE_LENGTH_EXCEEDED);
            case DirectionsAPI.STATUES_NOT_FOUND:
                return (DirectionsAPI.STATUES_NOT_FOUND);
            case DirectionsAPI.STATUES_OVER_QUERY_LIMIT:
                return (DirectionsAPI.STATUES_OVER_QUERY_LIMIT);
            case DirectionsAPI.STATUES_REQUEST_DENIED:
                return (DirectionsAPI.STATUES_REQUEST_DENIED);
            case DirectionsAPI.STATUES_UNKNOWN_ERROR:
                return (DirectionsAPI.STATUES_UNKNOWN_ERROR);
            case DirectionsAPI.STATUES_ZERO_RESULTS:
                return (DirectionsAPI.STATUES_ZERO_RESULTS);
            default:
                return (DirectionsAPI.STATUES_OK);
        }
    }

    /**
     * helper method to check the permission and find the current location
     */
    public static Location enableMyLocation(Context context, Location location,
                                            GoogleApiClient mGoogleApiClient, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

        {
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        return location;
    }
}
