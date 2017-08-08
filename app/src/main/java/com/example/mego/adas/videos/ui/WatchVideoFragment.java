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


package com.example.mego.adas.videos.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.videos.api.YouTubeApiUtilities;
import com.example.mego.adas.videos.api.model.Item;
import com.example.mego.adas.utils.Constant;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Fragment to show the video in youtube player
 */
public class WatchVideoFragment extends Fragment {


    /**
     * UI Element
     */
    private TextView videoTitleTextView, videoDateTextView, videoTimeTextView;

    private Item item;

    public WatchVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get the video information from the other fragment
        item = (Item) getArguments().getSerializable(Constant.KEY_ITEM_VIDEO);

        View rootView = inflater.inflate(R.layout.fragment_watch_video, container, false);
        initializeScreen(rootView);


        //setup the player fragment
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.watch_video_player, youTubePlayerFragment).commit();

        if (item != null) {

            //get the publish date and convert it to date object
            Date dateObject = YouTubeApiUtilities.fromISO8601(YouTubeApiUtilities.getVideoPublishTime(item));

            //format the object into date and time
            String date = YouTubeApiUtilities.formatDate(dateObject);
            String time = YouTubeApiUtilities.formatTime(dateObject);


            //set the current video information
            videoTitleTextView.setText(YouTubeApiUtilities.getVideoTitle(item));
            videoTimeTextView.setText(time);
            videoDateTextView.setText(date);


            youTubePlayerFragment.initialize(Constant.API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

                    //load and play the video with current id
                    youTubePlayer.loadVideo(YouTubeApiUtilities.getVideoId(item));
                    youTubePlayer.play();
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Toast.makeText(getContext(), R.string.can_not_initialize_youtube_player, Toast.LENGTH_LONG).show();

                }
            });


        }

        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Line the UI Element with the UI
     */
    private void initializeScreen(View view) {
        videoDateTextView = view.findViewById(R.id.video_date_text_view);
        videoTimeTextView = view.findViewById(R.id.video_time_text_view);
        videoTitleTextView = view.findViewById(R.id.video_title_text_view);

    }

}
