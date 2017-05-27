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


package com.example.mego.adas.model;


/**
 * Define the DataSend structure for youtube video information
 */
public class YouTubeVideo {

    String nextPageToken;
    String prevPageToken;

    int totalResults;
    int resultsPerPage;

    String publishedAt;
    String title;

    int position;

    String videoId;

    String imageUrl;

    /**
     * Required public constructor
     */
    public YouTubeVideo() {

    }

    /**
     * Use the constructor to create new Youtube Video
     *
     * @param nextPageToken
     * @param prevPageToken
     * @param totalResults
     * @param resultsPerPage
     * @param publishedAt
     * @param title
     * @param position
     * @param videoId
     * @param url
     */
    public YouTubeVideo(String nextPageToken, String prevPageToken, int totalResults, int resultsPerPage,
                        String publishedAt, String title, int position, String videoId, String url) {
        this.nextPageToken = nextPageToken;
        this.prevPageToken = prevPageToken;
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
        this.publishedAt = publishedAt;
        this.title = title;
        this.position = position;
        this.videoId = videoId;
        this.imageUrl = url;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
