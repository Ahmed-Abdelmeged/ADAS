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
 * Define the DataSend structure for directions  information
 */
public class Directions {

    private String statues;

    private double boundsNorthEastLatitude;
    private double boundsNorthEastLongitude;

    private double boundSouthWestLatitude;
    private double boundsSouthWestLongitude;

    private String overview_polyline_string;

    private int legDistanceValue;
    private String legDistanceText;

    private int legDurationValue;
    private String legDurationText;

    private String legEndAddress;
    private String legStartAddress;

    private double legStartLatitude;
    private double legStartLongitude;

    private double legEndLatitude;
    private double legEndLongitude;

    private String html_instructions;

    private String points;

    private int stepDistanceValue;
    private String stepDistanceText;

    private int stepDurationValue;
    private String stepDurationText;

    private double stepStartLatitude;
    private double stepStartLongitude;

    private double stepEndLatitude;
    private double stepEndLongitude;

    public Directions() {
    }

    /**
     * Use the constructor to create new direction object
     *
     * @param statues
     * @param boundsNorthEastLatitude
     * @param boundsNorthEastLongitude
     * @param boundSouthWestLatitude
     * @param boundsSouthWestLongitude
     * @param overview_polyline_string
     * @param legDistanceValue
     * @param legDistanceText
     * @param legDurationValue
     * @param legDurationText
     * @param legEndAddress
     * @param legStratAddress
     * @param legStartLatitude
     * @param legStartLongitude
     * @param legEndLatitude
     * @param legEndLongitude
     * @param html_instructions
     * @param points
     * @param stepDistanceValue
     * @param stepDistanceText
     * @param stepDurationValue
     * @param stepDurationText
     * @param stepStartLatitude
     * @param stepStartLongitude
     * @param stepEndLatitude
     * @param stepEndLongitude
     */
    public Directions(String statues, double boundsNorthEastLatitude,
                      double boundsNorthEastLongitude, double boundSouthWestLatitude,
                      double boundsSouthWestLongitude, String overview_polyline_string,
                      int legDistanceValue, String legDistanceText, int legDurationValue,
                      String legDurationText, String legEndAddress, String legStratAddress,
                      double legStartLatitude, double legStartLongitude, double legEndLatitude,
                      double legEndLongitude, String html_instructions, String points,
                      int stepDistanceValue, String stepDistanceText, int stepDurationValue,
                      String stepDurationText, double stepStartLatitude, double stepStartLongitude,
                      double stepEndLatitude, double stepEndLongitude) {
        this.statues = statues;
        this.boundsNorthEastLatitude = boundsNorthEastLatitude;
        this.boundsNorthEastLongitude = boundsNorthEastLongitude;
        this.boundSouthWestLatitude = boundSouthWestLatitude;
        this.boundsSouthWestLongitude = boundsSouthWestLongitude;
        this.overview_polyline_string = overview_polyline_string;
        this.legDistanceValue = legDistanceValue;
        this.legDistanceText = legDistanceText;
        this.legDurationValue = legDurationValue;
        this.legDurationText = legDurationText;
        this.legEndAddress = legEndAddress;
        this.legStartAddress = legStratAddress;
        this.legStartLatitude = legStartLatitude;
        this.legStartLongitude = legStartLongitude;
        this.legEndLatitude = legEndLatitude;
        this.legEndLongitude = legEndLongitude;
        this.html_instructions = html_instructions;
        this.points = points;
        this.stepDistanceValue = stepDistanceValue;
        this.stepDistanceText = stepDistanceText;
        this.stepDurationValue = stepDurationValue;
        this.stepDurationText = stepDurationText;
        this.stepStartLatitude = stepStartLatitude;
        this.stepStartLongitude = stepStartLongitude;
        this.stepEndLatitude = stepEndLatitude;
        this.stepEndLongitude = stepEndLongitude;
    }

    public String getStatues() {
        return statues;
    }

    public double getBoundsNorthEastLatitude() {
        return boundsNorthEastLatitude;
    }

    public double getBoundsNorthEastLongitude() {
        return boundsNorthEastLongitude;
    }

    public double getBoundSouthWestLatitude() {
        return boundSouthWestLatitude;
    }

    public double getBoundsSouthWestLongitude() {
        return boundsSouthWestLongitude;
    }

    public String getOverview_polyline_string() {
        return overview_polyline_string;
    }

    public int getLegDistanceValue() {
        return legDistanceValue;
    }

    public String getLegDistanceText() {
        return legDistanceText;
    }

    public int getLegDurationValue() {
        return legDurationValue;
    }

    public String getLegDurationText() {
        return legDurationText;
    }

    public String getLegEndAddress() {
        return legEndAddress;
    }

    public String getLegStartAddress() {
        return legStartAddress;
    }

    public double getLegStartLatitude() {
        return legStartLatitude;
    }

    public double getLegStartLongitude() {
        return legStartLongitude;
    }

    public double getLegEndLatitude() {
        return legEndLatitude;
    }

    public double getLegEndLongitude() {
        return legEndLongitude;
    }

    public String getHtml_instructions() {
        return html_instructions;
    }

    public String getPoints() {
        return points;
    }

    public int getStepDistanceValue() {
        return stepDistanceValue;
    }

    public String getStepDistanceText() {
        return stepDistanceText;
    }

    public int getStepDurationValue() {
        return stepDurationValue;
    }

    public String getStepDurationText() {
        return stepDurationText;
    }

    public double getStepStartLatitude() {
        return stepStartLatitude;
    }

    public double getStepStartLongitude() {
        return stepStartLongitude;
    }

    public double getStepEndLatitude() {
        return stepEndLatitude;
    }

    public double getStepEndLongitude() {
        return stepEndLongitude;
    }
}
