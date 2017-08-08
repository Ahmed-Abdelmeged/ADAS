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


package com.example.mego.adas.car_advice_assistant;

import android.content.Context;
import android.util.Log;

import com.example.mego.adas.utils.NotificationUtils;
import com.example.mego.adas.utils.PreferenceUtilities;


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
