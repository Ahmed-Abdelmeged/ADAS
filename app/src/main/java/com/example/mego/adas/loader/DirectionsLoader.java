package com.example.mego.adas.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.mego.adas.model.Directions;
import com.example.mego.adas.utils.QueryUtils;

import java.util.ArrayList;

/**
 * Created by Mego on 2/21/2017.
 */

/**
 * loader to load the directions DataSend from the Directions API in background thread
 */
public class DirectionsLoader  extends AsyncTaskLoader<ArrayList<Directions>> {

    /**
     * Tag for the log
     */
    private static final String LOG_TAG = "DirectionsLoader";

    /**
     * the request url for Directions API
     */
    private String mUrl;

    /**
     * call the loader
     *
     * @param context the activity the result will back to
     * @param url  the api request url
     */
    public DirectionsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "onStartLoading");
    }

    @Override
    public ArrayList<Directions> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        ArrayList<Directions> directions = QueryUtils.fetchDirectionsData(mUrl);
        return directions;
    }
}
