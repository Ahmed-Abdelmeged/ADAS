package com.example.mego.adas.sync;

import android.content.Context;
import android.util.Log;

import com.example.mego.adas.utils.NotificationUtils;
import com.example.mego.adas.utils.PreferenceUtilities;

/**
 * Created by Mego on 3/27/2017.
 */

/**
 * Class the have the tasks will be implemented by the intent service
 */
public class AdasSyncTasks {

    public static final String ACTION_CAR_ADVICE_ASSISTANT = "car-advice-assistant";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_CAR_ACCIDENT = "car-accident";

    public static void executeTask(Context context, String action) {
        boolean notificationsEnabled = PreferenceUtilities.areAdviceNotificationsEnabled(context);
        if (ACTION_CAR_ADVICE_ASSISTANT.equals(action)) {
            if (notificationsEnabled) {
                PreferenceUtilities.incrementAdvicesCount(context);
                NotificationUtils.remindUserWithCarAdvices(context);
            }
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            Log.e("das", "s");
            NotificationUtils.clearAllNotifications(context);
        }
    }
}
