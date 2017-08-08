package com.example.mego.adas.app;

import android.app.Application;

import timber.log.BuildConfig;
import timber.log.Timber;

/**
 * Created by ahmed on 8/8/2017.
 */

public class ADASApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
