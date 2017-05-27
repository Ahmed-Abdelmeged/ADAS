package com.example.mego.adas.sync;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mego on 3/27/2017.
 */

public class AdasSyncUtils {

    /**
     * Interval at which to send a new advice. Use TimeUnit for convenience, rather than
     * writing out a bunch of multiplication ourselves and risk making a silly mistake.
     */
    private static final int ADVICE_INTERVAL_HOURS = 24;
    private static final int ADVICE_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(ADVICE_INTERVAL_HOURS);
    private static final int ADVICE_FLEX_SECONDS = ADVICE_INTERVAL_SECONDS / 24;

    private static final String ADVICE_JOB_TAG = "car-assistant-tag";

    private static boolean sInitialized;

    /**
     * Schedules a repeating display advices notification using FirebaseJobDispatcher.
     *
     * @param context Context used to create the GooglePlayDriver that powers the
     *                FirebaseJobDispatcher
     */
    synchronized public static void scheduleAdvices(@NonNull final Context context) {

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        //create a job that display notification
        Job giveAdviceJob = dispatcher.newJobBuilder()

                /*
                 * The Service that will be used to write to preferences
                 */
                .setService(CarAdviceAssistantFirebaseJobService.class)

                /*
                 * Set the UNIQUE tag used to identify this Job.
                 */
                .setTag(ADVICE_JOB_TAG)

                /*
                 * setLifetime sets how long this job should persist. The options are to keep the
                 * Job "forever" or to have it die the next time the device boots up.
                 */
                .setLifetime(Lifetime.FOREVER)

                /*
                 * We want these reminders to continuously happen, so we tell this Job to recur.
                 */
                .setRecurring(true)

                /*
                 * We want the advice to happen every 24 hours or so. The first argument for
                 * Trigger class's static executionWindow method is the start of the time frame
                 * when the
                 * job should be performed. The second argument is the latest point in time at
                 * which the data should be synced. Please note that this end time is not
                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
                 */
                .setTrigger(Trigger.executionWindow(
                        ADVICE_INTERVAL_SECONDS,
                        ADVICE_INTERVAL_SECONDS + ADVICE_FLEX_SECONDS))

                /*
                 * If a Job with the tag with provided already exists, this new job will replace
                 * the old one.
                 */
                .setReplaceCurrent(true)

                /* Once the Job is ready, call the builder's build method to return the Job */
                .build();


         /* Schedule the Job with the dispatcher */
        dispatcher.schedule(giveAdviceJob);

        /* The job has been initialized */
        sInitialized = true;
    }

}
