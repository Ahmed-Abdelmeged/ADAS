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


package com.example.mego.adas.videos.api;


import com.example.mego.adas.videos.api.model.Item;
import com.example.mego.adas.videos.api.model.YouTubeVideo;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public final class YouTubeApiUtilities {

    /**
     * Method used to get the youtube videos from the request
     *
     * @param youTubeVideo the object that returned from the request
     * @return youtube videos items
     */
    public static ArrayList<Item> getVideos(YouTubeVideo youTubeVideo) {
        ArrayList<Item> videos = new ArrayList<>();
        for (int i = 0; i < youTubeVideo.getItems().size(); i++) {
            videos = (ArrayList<Item>) youTubeVideo.getItems();
        }
        return videos;
    }

    /**
     * Method to get the video title
     *
     * @param item youtube video item
     * @return video title
     */
    public static String getVideoTitle(Item item) {
        return item.getSnippet().getTitle();
    }

    /**
     * Method to get video Publish Time
     *
     * @param item
     * @return
     */
    public static String getVideoPublishTime(Item item) {
        return item.getSnippet().getPublishedAt();
    }

    /**
     * Method to get youtube video medium image url
     *
     * @param item
     * @return
     */
    public static String getVideoImageUrl(Item item) {
        return item.getSnippet().getThumbnails().getMedium().getUrl();
    }

    /**
     * Method to get youtube video id
     *
     * @param item
     * @return
     */
    public static String getVideoId(Item item) {
        return item.getSnippet().getResourceId().getVideoId();
    }

    /**
     * return a DataSend object to parse it to extract the time and date
     */
    public static Date fromISO8601(String publishedDate) {
        Date date = null;
        ISO8601DateFormat dateFormat = new ISO8601DateFormat();
        try {
            date = dateFormat.parse(publishedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    public static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.ENGLISH);
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    public static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        return timeFormat.format(dateObject);
    }
}
