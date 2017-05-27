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
