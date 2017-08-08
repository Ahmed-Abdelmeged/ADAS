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
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.mego.adas.R;
import com.google.android.gms.location.places.Place;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Contains useful utilities for a ADAS app, such as conversion between Celsius and Fahrenheit ..etc
 */
public final class AdasUtils {

    /**
     * Constant for the user image shared preference
     */
    private static final String USER_IMAGE_PATH = "user_image_path";
    private static final String USER_DEFAULT_IMAGE_PATH = null;

    /**
     * This method will convert a temperature from Celsius to Fahrenheit.
     *
     * @param temperatureInCelsius Temperature in degrees Celsius(°C)
     * @return Temperature in degrees Fahrenheit (°F)
     */
    public static double celsiusToFahrenheit(int temperatureInCelsius) {
        return (temperatureInCelsius * 1.8) + 32;
    }

    /**
     * Helper Method to determine is the temperature is C or F
     *
     * @param state the current state from the preference
     * @return true if it's Fahrenheit
     */
    public static boolean isCelsiusOrFahrenheit(String state, Context context) {
        return state.equals(context.getString(R.string.settings_temp_units_imperial_key));
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

    /**
     * Method to save save user image in internal storage for the device
     *
     * @param bitmapImage the downloaded bitmap
     * @param context
     * @param name        the image name (the last segment)
     * @return the saved path for th image
     */
    public static String saveImageIntoInternalStorage(Bitmap bitmapImage, Context context, String name) {

        // path to /data/data/adas/app_data/imageDir
        ContextWrapper contextWrapper = new ContextWrapper(context);

        //Folder name in device android/data/
        String folderName = "UserImages";
        File directory = contextWrapper.getDir(folderName, Context.MODE_PRIVATE);

        //Create imageDir
        File imagePath = new File(directory, name);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(imagePath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
        }
        return directory.getAbsolutePath();
    }

    /**
     * Method to get the current user image saved
     *
     * @param path the saved path for the image
     * @return the bitmap that will be displayed
     */
    public static Bitmap loadUserImageFromStorage(String path) {
        Bitmap bitmap;
        try {
            if (path != null) {
                File file = new File(path);
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;
            }
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    /**
     * Method to delete saved user image when sign out
     *
     * @param path the saved image path
     * @return the boolean to indicate if the photo is deleted or not
     */
    public static boolean deleteUserImageFromStorage(String path) {
        if (path != null) {
            File file = new File(path);
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * Method to set the current user image path to retrieve ans display it
     *
     * @param context   the application context
     * @param imagePath the saved image path
     */
    synchronized public static void setCurrentUserImagePath(Context context, String imagePath) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_IMAGE_PATH, imagePath);
        editor.apply();

    }

    /**
     * Method to get the current user image path to retrieve ans display it
     *
     * @param context the application context
     * @return the saved image path
     */
    public static String getCurrentUserImagePath(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(USER_IMAGE_PATH, USER_DEFAULT_IMAGE_PATH);
    }

    /**
     * Method to get a short address from the place picker
     *
     * @param selectedPlace the elected place
     * @return the shorten address
     */
    public static String getShortenAddress(Place selectedPlace) {
        //get the first name of the address
        String shortAddress = "";
        for (int i = 0; i < selectedPlace.getAddress().length(); i++) {
            if (selectedPlace.getAddress().charAt(i) == ',') {
                shortAddress = (String) selectedPlace.getAddress().subSequence(0, i);
                break;
            }
        }
        return shortAddress;
    }

}
