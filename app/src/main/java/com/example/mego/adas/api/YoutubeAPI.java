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

package com.example.mego.adas.api;

/**
 * the class have the constant of  youtube api json response
 */
public class YoutubeAPI {


    /**
     * part query parameter
     */
    public static final String QUERY_PARAMETER_PART = "part";

    /**
     * playlistID query parameter
     */
    public static final String QUERY_PARAMETER_PLAYLISTID = "playlistId";

    /**
     * page token query parameter
     */
    public static final String QUERY_PARAMETER_PAGETOKEN = "pageToken";

    /**
     * key  query parameter
     */
    public static final String QUERY_PARAMETER_KEY = "key";

    /**
     * the max number of returned video per one request query parameter
     */
    public static final String QUERY_PARAMETER_MAX_RESULTS = "maxResults";

    /**
     * The Google Directions API Request URL
     */
    public static final String YOUTUBE_DATA_API_REQUEST_URL =
            "https://www.googleapis.com/youtube/v3/playlistItems?";

    /**
     * request a video URL
     */
    public static final String YOUTUBE_VIDEO_REQUEST_URL =
            "https://www.youtube.com/watch?v=";


    /**
     * the element for the next page of the video response
     * it load the next 5 items
     */
    public static final String ELEMENT_NEXTPAGETOKEN = "nextPageToken";

    /**
     * the element for the previous page of the video response
     * it load the previous 5 items it does't exist in the first response
     */
    public static final String ELEMENT_PREVPAGETOKEN = "prevPageToken";

    /**
     * the element for the thumbnails
     */
    public static final String ELEMENT_THUMBNAILS = "thumbnails";


    /**
     * the element for the medium thumbnail
     */
    public static final String ELEMENT_MEDIUM = "medium";

    /**
     * the element for the image url
     */
    public static final String ELEMENT_URL = "url";

    /**
     * the element for know the total number of results
     */
    public static final String ELEMENT_TOTAL_RESULTS = "totalResults";

    /**
     * the element for know the result per page it always be 5
     */
    public static final String ELEMENT_RESULT_PER_PAGE = "resultsPerPage";

    /**
     * the element the contain the information about
     * <p>
     * {@link #ELEMENT_RESULT_PER_PAGE}
     * {@link #ELEMENT_TOTAL_RESULTS}
     */
    public static final String ELEMENT_PAGE_INFO = "pageInfo";

    /**
     * the element item that contain the video information
     * and retrieve an array of videos
     */
    public static final String ELEMENT_ITEMS = "items";

    /**
     * the element snippet that have a single video information
     */
    public static final String ELEMENT_SNIPPET = "snippet";

    /**
     * the element published at to know the DataSend of the publish the video
     */
    public static final String ELEMENT_PUBLISHED_AT = "publishedAt";

    /**
     * the element for know the title of the video
     */
    public static final String ELEMENT_TITLE = "title";

    /**
     * the element to know the video position to put it into  a list
     */
    public static final String ELEMENT_POSITION = "position";

    /**
     * the  element to contain the video resource information
     */
    public static final String ELEMENT_RESOURCE_ID = "resourceId";

    /**
     * the element to know the video id to play it in the youtube player
     */
    public static final String ELEMENT_VIDEO_ID = "videoId";

}
