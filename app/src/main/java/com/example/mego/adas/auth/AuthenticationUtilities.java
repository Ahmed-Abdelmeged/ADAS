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

package com.example.mego.adas.auth;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Pattern;

/**
 * Class that contain the Authentication Utilities and common used helper methods
 */
public class AuthenticationUtilities {

    /**
     * Default value for the current user
     */
    private static final String USER_DEFAULT_UID = null;
    private static final String USER_DEFAULT_NAME = null;
    private static final String USER_DEFAULT_EMAIL = null;
    private static final String USER_DEFAULT_PHONE = null;
    private static final String USER_DEFAULT_LOCATION = null;

    /**
     * Constant for the user uid that will store in the sharped preference to load user data in each fragment
     */
    private static final String USER_UID = "user_uid";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_LOCATION = "user_location";

    /**
     * Helper method to validate the email
     */
    static boolean isEmailValid(String email) {
        boolean isGoodEmail = false;
        isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return isGoodEmail;
    }

    /**
     * Helper method to validate the password
     */
    static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Helper method to validate the userName
     */
    static boolean isUserNameValid(String userName) {
        return !userName.equals("");
    }

    /**
     * Helper method to validate the phone number
     */
    static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isGoodPhone = false;
        if (!Pattern.matches("[a-zA-Z]+", phoneNumber)) {
            isGoodPhone = !(phoneNumber.length() < 5 || phoneNumber.length() > 14);
        } else {
            isGoodPhone = false;
        }
        return isGoodPhone;
    }

    /**
     * Helper method to check the internet connection isAvailableInternetConnection
     */
    public static boolean isAvailableInternetConnection(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Helper Method to hide the keyboard
     */
    static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Helper method to set the current user
     */
    synchronized public static void setCurrentUser(String uid, String name
            , String email, String phoneNumber, String location, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        //get the current state for shared preference
        String userUid = preferences.getString(USER_UID, USER_DEFAULT_UID);
        String userName = preferences.getString(USER_NAME, USER_DEFAULT_NAME);
        String userLocation = preferences.getString(USER_LOCATION, USER_DEFAULT_LOCATION);
        String userPhone = preferences.getString(USER_PHONE, USER_DEFAULT_PHONE);
        String userEmail = preferences.getString(USER_EMAIL, USER_DEFAULT_EMAIL);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(USER_UID, uid);
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_PHONE, phoneNumber);
        editor.putString(USER_LOCATION, location);
        editor.apply();
    }

    /**
     * Helper method to get the current user
     */
    public static User getCurrentUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userUid = sharedPreferences.getString(USER_UID, USER_DEFAULT_UID);
        String userName = sharedPreferences.getString(USER_NAME, USER_DEFAULT_NAME);
        String userLocation = sharedPreferences.getString(USER_LOCATION, USER_DEFAULT_LOCATION);
        String userPhone = sharedPreferences.getString(USER_PHONE, USER_DEFAULT_PHONE);
        String userEmail = sharedPreferences.getString(USER_EMAIL, USER_DEFAULT_EMAIL);
        return new User(userEmail, userPhone, userLocation, userName, userUid);
    }

    /**
     * Helper method to clear current user
     */
    synchronized public static void clearCurrentUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(USER_UID, null);
        editor.putString(USER_NAME, null);
        editor.putString(USER_EMAIL, null);
        editor.putString(USER_PHONE, null);
        editor.putString(USER_LOCATION, null);
        editor.apply();
    }
}
