package com.example.mego.adas.auth;

/**
 * Created by Mego on 4/14/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.mego.adas.utils.constant;

import java.util.regex.Pattern;

/**
 * Class that contain the Authentication Utilities and common used helper methods
 */
public class AuthenticationUtilities {

    /**
     * Default value for the current user
     */
    private static final String USER_DEFUALT = null;

    /**
     * Helper method to validate the email
     */
    public static boolean isEmailValid(String email) {
        boolean isGoodEmail = false;
        isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return isGoodEmail;
    }

    /**
     * Helper method to validate the password
     */
    public static boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }
        return true;
    }

    /**
     * Helper method to validate the userName
     */
    public static boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * Helper method to validate the phone number
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isGoodPhone = false;
        if (!Pattern.matches("[a-zA-Z]+", phoneNumber)) {
            if (phoneNumber.length() < 5 || phoneNumber.length() > 14) {
                isGoodPhone = false;
            } else {
                isGoodPhone = true;
            }
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
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Helper Method to hide the keyboard
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Helper method to set the current user
     */
    synchronized public static void setCurrentUser(String uid, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userUid = preferences.getString(constant.USER_UID, USER_DEFUALT);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(constant.USER_UID, uid);
        editor.apply();
    }

    /**
     * Helper method to get the current user
     */
    public static String getCurrentUser(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userUid = sharedPreferences.getString(constant.USER_UID, USER_DEFUALT);
        return userUid;
    }


}
