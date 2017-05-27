package com.example.mego.adas.model;

/**
 * Created by Mego on 2/21/2017.
 */

/**
 * Define the DataSend structure for steps  information
 */
public class Steps {

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

    public Steps() {
    }

    /**
     * Use the constructor to create new step object
     *
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
    public Steps(String html_instructions, String points, int stepDistanceValue,
                 String stepDistanceText, int stepDurationValue, String stepDurationText,
                 double stepStartLatitude, double stepStartLongitude, double stepEndLatitude,
                 double stepEndLongitude) {
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
