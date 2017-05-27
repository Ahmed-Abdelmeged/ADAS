package com.example.mego.adas.loader;

/**
 * Created by Mego on 2/20/2017.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.mego.adas.model.YouTubeVideo;
import com.example.mego.adas.utils.QueryUtils;

import java.util.ArrayList;

/**
 * loader to load the videos DataSend from the Youtube DataSend  API in background thread
 */
public class VideosLoader extends AsyncTaskLoader<ArrayList<YouTubeVideo>> {

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
     * @param url     the api request url
     */
    public VideosLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    /**
     * start the loader after it created
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "onStartLoading");
    }

    /**
     *  make the request in background thread
     *
     * @return
     */

    @Override
    public ArrayList<YouTubeVideo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        ArrayList<YouTubeVideo> videos = QueryUtils.fetchVideosData(mUrl);
        Log.e(LOG_TAG, "load in the background");
        return videos;
    }

}
