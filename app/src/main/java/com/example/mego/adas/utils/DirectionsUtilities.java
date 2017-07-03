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
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.example.mego.adas.api.DirectionsAPI;
import com.example.mego.adas.model.Accident;
import com.example.mego.adas.model.Directions;
import com.example.mego.adas.model.Steps;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Class offer utility methods to location services
 */
public final class DirectionsUtilities {

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
     * a function to move the marker from place to other
     *
     * @param marker     marker object
     * @param toPosition to the position i want from started one
     * @param hideMarker set true if i want to hide the marker
     */
    public static void AnimateMarker(final Marker marker, final LatLng toPosition,
                                     final boolean hideMarker, GoogleMap map) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    /**
     * Helper method to get the step information from direction object
     *
     * @param directionsData direction object from the direction API
     * @return step object with it's information
     */
    public static Steps getStepInformation(Directions directionsData) {

        String html_instructions = directionsData.getHtml_instructions();
        String points = directionsData.getPoints();
        int stepDistanceValue = directionsData.getStepDistanceValue();
        String stepDistanceText = directionsData.getStepDistanceText();
        int stepDurationValue = directionsData.getStepDurationValue();
        String stepDurationText = directionsData.getStepDurationText();
        double stepStartLatitude = directionsData.getStepStartLatitude();
        double stepStartLongitude = directionsData.getStepStartLongitude();
        double stepEndLatitude = directionsData.getStepEndLatitude();
        double stepEndLongitude = directionsData.getStepEndLongitude();

        return new Steps(html_instructions, points, stepDistanceValue, stepDistanceText,
                stepDurationValue, stepDurationText, stepStartLatitude, stepStartLongitude,
                stepEndLatitude, stepEndLongitude);
    }
}
