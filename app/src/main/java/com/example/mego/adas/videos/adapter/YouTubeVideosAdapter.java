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


package com.example.mego.adas.videos.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mego.adas.R;
import com.example.mego.adas.videos.api.YouTubeApiUtilities;
import com.example.mego.adas.videos.api.model.Item;

import java.util.ArrayList;
import java.util.Date;


public class YouTubeVideosAdapter extends RecyclerView.Adapter<YouTubeVideosAdapter.VideosViewHolder> {
    private ArrayList<Item> mItems;

    private final YouTubeVideosAdapterOnClickHandler mClickHandler;

    public interface YouTubeVideosAdapterOnClickHandler {
        void onCLick(Item item);
    }

    public YouTubeVideosAdapter(YouTubeVideosAdapterOnClickHandler youTubeVideosAdapterOnClickHandler) {
        mClickHandler = youTubeVideosAdapterOnClickHandler;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.youtube_video_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        //get the current video object to extract DataSend from it
        Item currentVideo = mItems.get(position);

        //set the current video title
        String title = YouTubeApiUtilities.getVideoTitle(currentVideo);
        holder.videoTitle.setText(title);

        //get the publish date and convert it to date object
        Date dateObject = YouTubeApiUtilities.fromISO8601(YouTubeApiUtilities.getVideoPublishTime(currentVideo));

        //set the current video date
        String date = YouTubeApiUtilities.formatDate(dateObject);
        holder.videoDate.setText(date);

        //set the video time
        String time = YouTubeApiUtilities.formatTime(dateObject);
        holder.videoTime.setText(time);

        //set the video Thumbnail photo
        Glide.with(holder.videoThumbnail.getContext())
                .load(YouTubeApiUtilities.getVideoImageUrl(currentVideo))
                .into(holder.videoThumbnail);
    }


    @Override
    public int getItemCount() {
        if (mItems == null) return 0;
        return mItems.size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView videoTitle;
        TextView videoDate;
        TextView videoTime;
        ImageView videoThumbnail;

        VideosViewHolder(View view) {
            super(view);
            videoTitle = view.findViewById(R.id.video_title_text_view);
            videoTime = view.findViewById(R.id.video_time_text_view);
            videoThumbnail = view.findViewById(R.id.video_image_view);
            videoDate = view.findViewById(R.id.video_date_text_view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItems != null) {
                int adapterPosition = getAdapterPosition();
                mClickHandler.onCLick(mItems.get(adapterPosition));
            }
        }
    }

    public void setVideos(ArrayList<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void addVideos(ArrayList<Item> items) {
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mItems != null) {
            this.mItems.clear();
            notifyDataSetChanged();
        }
    }
}
