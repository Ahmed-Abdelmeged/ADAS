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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mego.adas.R;

/**
 * This class contains utility methods which update advices counts in SharedPreferences
 */
public class PreferenceUtilities {

    public static final String KEY_ADVICE_COUNT = "advice-count";
    private static final int DEFAULT_COUNT = -1;


    /**
     * increment the current advice current until reach to 11 and restart the count
     *
     * @param context
     */
    synchronized public static void incrementAdvicesCount(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int advicesCount = preferences.getInt(KEY_ADVICE_COUNT, DEFAULT_COUNT);

        SharedPreferences.Editor editor = preferences.edit();

        if (advicesCount < 11) {
            advicesCount++;
        } else {
            advicesCount = 0;
        }
        editor.putInt(KEY_ADVICE_COUNT, advicesCount);
        editor.apply();
    }

    /**
     * get the current advices count
     *
     * @param context
     * @return
     */
    public static int getAdviceCount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int adviceCount = sharedPreferences.getInt(KEY_ADVICE_COUNT, DEFAULT_COUNT);
        return adviceCount;
    }

    /**
     * Returns true if the user prefers to see notifications from car advice assistant, false otherwise. This
     * preference can be changed by the user within the SettingsFragment.
     *
     * @param context Used to access SharedPreferences
     * @return true if the user prefers to see notifications, false otherwise
     */
    public static boolean areAdviceNotificationsEnabled(Context context) {

         /* Key for accessing the preference for showing notifications */
        String displayNotificationsKey = context.getString(R.string.settings_enable_notification_key);

        /*
         * In ADAS, the user has the ability to say whether she would like notifications
         * enabled or not. If no preference has been chosen, we want to be able to determine
         * whether or not to show them. To do this, we reference a bool stored in bools.xml.
         */
        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        /* If a value is stored with the key, we extract it here. If not, use a default. */
        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

}
