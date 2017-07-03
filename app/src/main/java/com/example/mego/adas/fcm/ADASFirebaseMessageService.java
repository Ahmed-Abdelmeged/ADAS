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


package com.example.mego.adas.fcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.mego.adas.R;
import com.example.mego.adas.utils.Constant;
import com.example.mego.adas.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Listens for accidents FCM messages both in the background and the foreground and responds
 * appropriately
 * depending on type of message
 */
public class ADASFirebaseMessageService extends FirebaseMessagingService {

    /**
     * Tag for debugging
     */
    private static final String LOG_TAG = ADASFirebaseMessageService.class.getSimpleName();

    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int ADAS_ACCIDENT_FCM_PENDING_INTENT_ID = 5368;

    /**
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update
     */
    private static final int ADAS_ACCIDENT_FCM_NOTIFICATION_ID = 9631;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //Get the data from the message
        Map<String, String> data = remoteMessage.getData();

        //Check if the data contain a payload
        if (data.size() > 0) {
            double longitude = Double.parseDouble(data.get(Constant.FCM_LONGITUDE));
            double latitude = Double.parseDouble(data.get(Constant.FCM_LATITIDE));
            boolean accidentState = Boolean.parseBoolean(data.get(Constant.FCM_STATE));
            String title = data.get(Constant.FCM_TITLE);
            String sound = data.get(Constant.FCM_SOUND);
            sendAccidentNotification(this, longitude, latitude, accidentState, title);
        }
    }

    /**
     * Create and show a accident notification containing the received FCM message
     *
     * @param longitude     accident longitude
     * @param latitude      accident latitude
     * @param accidentState accident state true if the accident update location , false if new accident
     * @param title         the accident title
     */

    private void sendAccidentNotification(Context context, double longitude, double latitude,
                                          boolean accidentState, String title) {

        //Check if new accident or updated one
        String notificationText = "An accident occurred at longitude: " + longitude +
                " and latitude: " + latitude;
        if (accidentState) {
            notificationText = "An accident location changed to longitude: " + longitude +
                    " and latitude: " + latitude;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(NotificationUtils.largeIcon(context))
                .setContentTitle(title)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent(context, longitude, latitude))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(ADAS_ACCIDENT_FCM_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * This method will create pending intent that will trigger when the notification is pressed
     *
     * @param context
     * @return a pending intent with activity that will be open form the notification
     */
    private static PendingIntent contentIntent(Context context, double longitude, double latitude) {
        Intent startAccidentActivityIntent = new Intent(context, AccidentActivity.class);
        startAccidentActivityIntent.putExtra(Constant.FCM_LONGITUDE_EXTRA, longitude);
        startAccidentActivityIntent.putExtra(Constant.FCM_LATITUDE_EXTRA, longitude);
        return PendingIntent.getActivity(
                context,
                ADAS_ACCIDENT_FCM_PENDING_INTENT_ID,
                startAccidentActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
