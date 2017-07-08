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


package com.example.mego.adas.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.mego.adas.api.directions.DirectionsAPIConstants;
import com.example.mego.adas.api.YoutubeAPI;
import com.example.mego.adas.model.YouTubeVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * class the has the Query utilities
 * <p>
 * for the youtube api and directions api
 */
public class QueryUtils {

    /**
     * Tag for the log
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /**
     * fetch the DataSend from the Youtube API and return an YouTubeVideo object
     */
    public static ArrayList<YouTubeVideo> fetchVideosData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<YouTubeVideo> videos = extractVideos(jsonResponse);
        return videos;
    }

    /**
     * Return a list of YoutubeVideo  objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<YouTubeVideo> extractVideos(String videoJson) {

        //check if the text is empty return null
        if (TextUtils.isEmpty(videoJson)) {
            return null;
        }

        //create an empty ArrayList that we can start adding the videos to
        ArrayList<YouTubeVideo> videos = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //  Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Videos objects with the corresponding DataSend.
            JSONObject baseJSONResponse = new JSONObject(videoJson);

            //get the next page id
            //String nextPageToken = baseJSONResponse.getString(YoutubeAPI.ELEMENT_NEXTPAGETOKEN);

            //get the previous page id
            //String prevPageToken = baseJSONResponse.getString(YoutubeAPI.ELEMENT_PREVPAGETOKEN);

            //get the page info , total results and results per page
            JSONObject pageInfo = baseJSONResponse.getJSONObject(YoutubeAPI.ELEMENT_PAGE_INFO);
            int totalResults = pageInfo.getInt(YoutubeAPI.ELEMENT_TOTAL_RESULTS);
            int resultsPerPage = pageInfo.getInt(YoutubeAPI.ELEMENT_RESULT_PER_PAGE);

            //get the items array
            JSONArray items = baseJSONResponse.getJSONArray(YoutubeAPI.ELEMENT_ITEMS);
            for (int counter = 0; counter < items.length(); counter++) {

                //get the current item
                JSONObject currentVideo = items.getJSONObject(counter);

                //get the snippet information
                JSONObject snippet = currentVideo.getJSONObject(YoutubeAPI.ELEMENT_SNIPPET);

                //get the video published date
                String publishedAt = snippet.getString(YoutubeAPI.ELEMENT_PUBLISHED_AT);

                //get the video Thumbnail url
                JSONObject thumbnail = snippet.getJSONObject(YoutubeAPI.ELEMENT_THUMBNAILS);
                JSONObject mediumPhotoUrl = thumbnail.getJSONObject(YoutubeAPI.ELEMENT_MEDIUM);
                String url = mediumPhotoUrl.getString(YoutubeAPI.ELEMENT_URL);


                //get the video title
                String title = snippet.getString(YoutubeAPI.ELEMENT_TITLE);

                //get the video position
                int position = snippet.getInt(YoutubeAPI.ELEMENT_POSITION);

                //get the video ID to play in the youtube media player
                JSONObject resourceId = snippet.getJSONObject(YoutubeAPI.ELEMENT_RESOURCE_ID);
                String videoId = resourceId.getString(YoutubeAPI.ELEMENT_VIDEO_ID);

                YouTubeVideo youTubeVideo = new YouTubeVideo("no", "no", totalResults,
                        resultsPerPage, publishedAt, title, position, videoId, url);

                videos.add(youTubeVideo);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the directions JSON results", e);
        }


        //return a array list of YouTubeVideo objects
        return videos;
    }


    /**
     * create a url from string
     */
    private static URL createUrl(String StringUrl) {
        URL url = null;
        try {
            url = new URL(StringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            //set up the connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");

            //connect
            urlConnection.connect();

            //receive DataSend if the response code is ok
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the directions JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String Which Contains the
     * whole JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
