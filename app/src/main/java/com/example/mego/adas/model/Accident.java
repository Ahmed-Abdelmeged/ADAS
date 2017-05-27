package com.example.mego.adas.model;

/**
 * Created by Mego on 2/26/2017.
 */

/**
 * Define the Data structure for accident  information
 */
public class Accident {

    private String date;
    private String time;

    private String accidentTitle;
    private double accidentLongitude;
    private double accidentLatitude;

    /**
     * Required public constructor
     */
    public Accident() {
    }


    /**
     * Use the constructor to create new Accident
     *
     * @param date
     * @param time
     * @param accidentTitle
     * @param accidentLongitude
     * @param accidentLatitude
     */
    public Accident(String date, String time, String accidentTitle, double accidentLongitude,
                    double accidentLatitude) {
        this.date = date;
        this.time = time;
        this.accidentTitle = accidentTitle;
        this.accidentLongitude = accidentLongitude;
        this.accidentLatitude = accidentLatitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAccidentTitle() {
        return accidentTitle;
    }

    public double getAccidentLongitude() {
        return accidentLongitude;
    }

    public double getAccidentLatitude() {
        return accidentLatitude;
    }
}
