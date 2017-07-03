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
import android.location.Location;
import android.util.Log;

import com.example.mego.adas.model.Directions;
import com.example.mego.adas.utils.QueryUtils;

import java.util.ArrayList;

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
