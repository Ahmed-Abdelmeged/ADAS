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

package com.example.mego.adas.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mego.adas.R;
import com.example.mego.adas.model.YouTubeVideo;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * custom array adapter to view the list of videos
 */
public class YoutubeVideoAdapter extends ArrayAdapter<YouTubeVideo> {

    /**
     * Required public constructor
     */
    public YoutubeVideoAdapter(Context context, ArrayList<YouTubeVideo> youTubeVideos) {
        super(context, 0, youTubeVideos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;

        //check if the view is created or not if not inflate new one
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.youtube_video_list, parent, false);
        }


        //get a instance from the viewHolder class
        ViewHolder holder = new ViewHolder();
        holder.videoTitle = (TextView) listView.findViewById(R.id.video_title_text_view);
        holder.videoTime = (TextView) listView.findViewById(R.id.video_time_text_view);
        holder.videoThumbnail = (ImageView) listView.findViewById(R.id.video_image_view);
        listView.setTag(holder);

        //get the current video object to extract DataSend from it
        YouTubeVideo currentVideo = getItem(position);

        //set the current video title
        String title = currentVideo.getTitle();
        holder.videoTitle.setText(title);


        //get the publish date and convert it to date object
        Date dateObject = fromISO8601(currentVideo.getPublishedAt());


        //set the current video date
        holder.videoDate = (TextView) listView.findViewById(R.id.video_date_text_view);
        String date = formatDate(dateObject);
        holder.videoDate.setText(date);

        //set the video time
        String time = formatTime(dateObject);
        holder.videoTime.setText(time);

        //set the video Thumbnail photo
        Glide.with(holder.videoThumbnail.getContext())
                .load(currentVideo.getImageUrl())
                .into(holder.videoThumbnail);

        return listView;

    }

    /**
     * View holder stores each of the component views inside the tag field of the Layout
     */
    static class ViewHolder {
        TextView videoTitle;
        TextView videoDate;
        TextView videoTime;
        ImageView videoThumbnail;
    }

    /**
     * return a DataSend object to parse it to extract the time and date
     */
    private Date fromISO8601(String publishedDate) {
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
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy", Locale.ENGLISH);
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        return timeFormat.format(dateObject);
    }
}
