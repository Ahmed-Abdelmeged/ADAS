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

package com.example.mego.adas.loader;


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
