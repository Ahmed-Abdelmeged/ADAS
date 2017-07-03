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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.mego.adas.application.MainActivity;
import com.example.mego.adas.R;
import com.example.mego.adas.sync.AdasSyncTasks;
import com.example.mego.adas.sync.CarAdviceAssistantIntentService;

/**
 * Utility class for creating ADAS notifications
 */
public class NotificationUtils {

    /**
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update
     */
    private static final int ADAS_ADVICES_NOTIFICATION_ID = 1138;

    private static final int ADAS_WARNING_NOTIFICATION_ID = 3264;

    private static final int ADAS_ACCIDENT_NOTIFICATION_ID = 4598;

    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int ADAS_ADVICES_PENDING_INTENT_ID = 3417;

    private static final int ADAS_ACCIDENT_PENDING_INTENT_ID = 1256;


    /**
     * Helper Method to create and display the notification for advices
     *
     * @param context
     */
    public static void remindUserWithCarAdvices(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_remind_user_advices_title))
                .setContentText(showRandomCarAdvices(context))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(showRandomCarAdvices(context)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(ADAS_ADVICES_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Helper Method to create and display waring notification
     *
     * @param context
     * @param notificationBody the content text with in notification
     */
    public static void showWarningNotification(Context context, String notificationBody) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_waring))
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(ADAS_WARNING_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Helper Method to create and display accident notification
     *
     * @param context
     */
    public static void showAccidentNotification(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_accident))
                .setContentText(context.getString(R.string.car_accident))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.car_accident)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(ADAS_ACCIDENT_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Helper method for the action cancel
     */
    private static NotificationCompat.Action ignoreAccident(Context context) {
        Intent ignoreAccidentIntent = new Intent(context, CarAdviceAssistantIntentService.class);

        ignoreAccidentIntent.setAction(AdasSyncTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreAccidentPendingIntent = PendingIntent.getService(
                context,
                ADAS_ACCIDENT_PENDING_INTENT_ID,
                ignoreAccidentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action ignoreAccidentAction = new NotificationCompat.Action(
                R.drawable.ic_cancel_black_24px,
                context.getString(R.string.cancel),
                ignoreAccidentPendingIntent);

        return ignoreAccidentAction;
    }


    /**
     * This method will create pending intent that will trigger when the notification is pressed
     *
     * @param context
     * @return a pending intent with activity that will be open form the notification
     */
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);

        return PendingIntent.getActivity(
                context,
                ADAS_ADVICES_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Helper Method to clear all the app notification
     */
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * This method is necessary to decode a bitmap needed for the notification.
     *
     * @param context
     * @return a large icon bitmap
     */
    public static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher);
        return largeIcon;
    }

    /**
     * Method used to display random advices from the exist one already defined
     *
     * @param context
     * @return a random advice
     */
    private static String showRandomCarAdvices(Context context) {
        String advice = "";
        int currentAdvice = PreferenceUtilities.getAdviceCount(context);
        switch (currentAdvice) {
            case 0:
                advice = context.getString(R.string.car_advice_0_help);
                break;
            case 1:
                advice = context.getString(R.string.car_advice_1_check_battery);
                break;
            case 2:
                advice = context.getString(R.string.car_advice_2_check_temperature);
                break;
            case 3:
                advice = context.getString(R.string.car_advice_3_check_internet_connection);
                break;
            case 4:
                advice = context.getString(R.string.car_advice_4_check_camera);
                break;
            case 5:
                advice = context.getString(R.string.car_advice_5_check_problem);
                break;
            case 6:
                advice = context.getString(R.string.car_advice_6_settings);
                break;
            case 7:
                advice = context.getString(R.string.car_advice_7_help);
                break;
            case 8:
                advice = context.getString(R.string.car_advice_8_accident);
                break;
            case 9:
                advice = context.getString(R.string.car_advice_9_behavior);
                break;
            case 10:
                advice = context.getString(R.string.car_advice_10_directions);
                break;
            case 11:
                advice = context.getString(R.string.car_advice_11_videos);
                break;
            default:
                advice = context.getString(R.string.car_advice_0_help);
                break;
        }
        return advice;
    }

}
