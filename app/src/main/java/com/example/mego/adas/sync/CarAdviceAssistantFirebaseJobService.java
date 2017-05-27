package com.example.mego.adas.sync;

/**
 * Created by Mego on 3/27/2017.
 */

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Used to schedule the notification job
 */
public class CarAdviceAssistantFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    /**
     * The entry point to your Job. Implementations should offload work to another thread of
     * execution as soon as possible.
     *
     * This is called by the Job Dispatcher to tell us we should start our job. Keep in mind this
     * method is run on the application's main thread, so we need to offload work to a background
     * thread.
     *
     * @return whether there is more work remaining.
     */
    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context context = CarAdviceAssistantFirebaseJobService.this;
                AdasSyncTasks.executeTask(context, AdasSyncTasks.ACTION_CAR_ADVICE_ASSISTANT);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                // the false here mean the job is need to be scheduled or not
                //because the job is successful we don't need to rescheduled it
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
