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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.mego.adas.R;

/**
 * Contains useful utilities for a ADAS app, such as conversion between Celsius and Fahrenheit ..etc
 */
public final class AdasUtils {

    /**
     * This method will convert a temperature from Celsius to Fahrenheit.
     *
     * @param temperatureInCelsius Temperature in degrees Celsius(°C)
     * @return Temperature in degrees Fahrenheit (°F)
     */
    public static double celsiusToFahrenheit(int temperatureInCelsius) {
        double temperatureInFahrenheit = (temperatureInCelsius * 1.8) + 32;
        return temperatureInFahrenheit;
    }

    /**
     * Helper Method to determine is the temperature is C or F
     *
     * @param state the current state from the preference
     * @return true if it's Fahrenheit
     */
    public static boolean isCelsiusOrFahrenheit(String state, Context context) {
        if (state.equals(context.getString(R.string.settings_temp_units_imperial_key))) {
            return true;
        }
        return false;
    }

    /**
     * send a message  to call the emergency with location of the accident
     *
     * @param latitude
     * @param longitude
     */
    public static void sendEmergencyToMessages(double latitude, double longitude, Context context) {
        Intent sendSmsIntent = new Intent(Intent.ACTION_VIEW);
        sendSmsIntent.setData(Uri.parse("sms:"));
        sendSmsIntent.setType("vnd.android-dir/mms-sms");
        String message = "The Car had accident at: " + latitude + " , " + longitude;
        sendSmsIntent.putExtra(Intent.EXTRA_TEXT, message);
        if (sendSmsIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(sendSmsIntent);
        }
    }
}
