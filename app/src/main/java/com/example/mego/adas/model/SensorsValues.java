package com.example.mego.adas.model;

/**
 * Created by Mego on 2/24/2017.
 */

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
