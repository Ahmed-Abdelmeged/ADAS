package com.example.mego.adas.model;

/**
 * Created by Mego on 2/21/2017.
 */

/**
 * Define the DataSend structure for directions  information
 */
public class Directions {

    String statues;

    double boundsNorthEastLatitude;
    double boundsNorthEastLongitude;

    double boundSouthWestLatitude;
    double boundsSouthWestLongitude;

    String overview_polyline_string;

    int legDistanceValue;
    String legDistanceText;

    int legDurationValue;
    String legDurationText;

    String legEndAddress;
    String legStartAddress;

    double legStartLatitude;
    double legStartLongitude;

    double legEndLatitude;
    double legEndLongitude;

    String html_instructions;

    String points;

    int stepDistanceValue;
    String stepDistanceText;

    int stepDurationValue;
    String stepDurationText;

    double stepStartLatitude;
    double stepStartLongitude;

    double stepEndLatitude;
    double stepEndLongitude;

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
