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


package com.example.mego.adas.directions.api;


import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.example.mego.adas.directions.api.model.Direction;
import com.example.mego.adas.directions.api.model.Leg;
import com.example.mego.adas.directions.api.model.Step;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Class offer utility methods to location services
 */
public final class DirectionsApiUtilities {

    /**
     * helper method to check the response code form the direction API Response State
     */
    public static String checkResponseState(String statues) {
        switch (statues) {
            case DirectionsApiConstants.STATUES_INVALID_REQUEST:
                return (DirectionsApiConstants.STATUES_INVALID_REQUEST);
            case DirectionsApiConstants.STATUES_MAX_ROUTE_LENGTH_EXCEEDED:
                return (DirectionsApiConstants.STATUES_MAX_ROUTE_LENGTH_EXCEEDED);
            case DirectionsApiConstants.STATUES_NOT_FOUND:
                return (DirectionsApiConstants.STATUES_NOT_FOUND);
            case DirectionsApiConstants.STATUES_OVER_QUERY_LIMIT:
                return (DirectionsApiConstants.STATUES_OVER_QUERY_LIMIT);
            case DirectionsApiConstants.STATUES_REQUEST_DENIED:
                return (DirectionsApiConstants.STATUES_REQUEST_DENIED);
            case DirectionsApiConstants.STATUES_UNKNOWN_ERROR:
                return (DirectionsApiConstants.STATUES_UNKNOWN_ERROR);
            case DirectionsApiConstants.STATUES_ZERO_RESULTS:
                return (DirectionsApiConstants.STATUES_ZERO_RESULTS);
            default:
                return (DirectionsApiConstants.STATUES_OK);
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
        if (map != null && toPosition != null && marker != null) {
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
    }

    /**
     * Method to get the leg distance form the directions API
     *
     * @param direction
     * @return
     */
    public static String getLegDistance(Direction direction) {
        String distanceText = "";
        for (int i = 0; i < direction.getRoutes().size(); i++) {
            List<Leg> legs = direction.getRoutes().get(i).getLegs();
            for (int j = 0; j < legs.size(); j++) {
                distanceText = legs.get(j).getDistance().getText();
            }
        }
        return distanceText;
    }

    /**
     * Method to get the leg duration form the directions API
     *
     * @param direction
     * @return
     */
    public static String getLegDuration(Direction direction) {
        String durationText = "";
        for (int i = 0; i < direction.getRoutes().size(); i++) {
            List<Leg> legs = direction.getRoutes().get(i).getLegs();
            for (int j = 0; j < legs.size(); j++) {
                durationText = legs.get(j).getDuration().getText();
            }
        }
        return durationText;
    }

    /**
     * Method to get the leg polyline that will be draw in the map form the directions API
     *
     * @param direction
     * @return
     */
    public static String getOverViewPolyLine(Direction direction) {
        String overViewPolyLine = "";
        for (int i = 0; i < direction.getRoutes().size(); i++) {
            overViewPolyLine = direction.getRoutes().get(i).getOverviewPolyline().getPoints();
        }
        return overViewPolyLine;
    }

    /**
     * Method to get the leg steps form the directions API
     *
     * @param direction
     * @return
     */
    public static ArrayList<Step> getSteps(Direction direction) {
        ArrayList<Step> steps = new ArrayList<>();
        for (int i = 0; i < direction.getRoutes().size(); i++) {
            List<Leg> legs = direction.getRoutes().get(i).getLegs();
            for (int j = 0; j < legs.size(); j++) {
                steps = (ArrayList<Step>) legs.get(i).getSteps();
            }
        }
        return steps;
    }
}
