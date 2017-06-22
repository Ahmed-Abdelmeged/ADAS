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
 * this a class of the values send from the car to the user
 */
public class SensorsValues {

    private int temperatureSensorValue ;
    private int ldrSensorValue ;
    private int potSensorValue ;

    public SensorsValues() {
    }

    /**
     * Use the constructor to create new sensor values object
     *
     * @param temperatureSensorValue
     * @param ldrSensorValue
     * @param potSensorValue
     */
    public SensorsValues(int temperatureSensorValue, int ldrSensorValue, int potSensorValue) {
        this.temperatureSensorValue = temperatureSensorValue;
        this.ldrSensorValue = ldrSensorValue;
        this.potSensorValue = potSensorValue;

    }

    public int getTemperatureSensorValue() {
        return temperatureSensorValue;
    }

    public int getLdrSensorValue() {
        return ldrSensorValue;
    }

    public int getPotSensorValue() {
        return potSensorValue;
    }



    public void setTemperatureSensorValue(int temperatureSensorValue) {
        this.temperatureSensorValue = temperatureSensorValue;
    }

    public void setLdrSensorValue(int ldrSensorValue) {
        this.ldrSensorValue = ldrSensorValue;
    }

    public void setPotSensorValue(int potSensorValue) {
        this.potSensorValue = potSensorValue;
    }


}
