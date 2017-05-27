package com.example.mego.adas.model;

/**
 * Created by Mego on 2/20/2017.
 */

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
