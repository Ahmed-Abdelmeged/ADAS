package com.example.mego.adas.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.utils.constant;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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


    /**
     * the single video information
     */
    private String videoId = null;
    private String publishAt = null;
    private String title = null;


    public WatchVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get the video information from the other fragment
        videoId = getArguments().getString(constant.VIDEO_KEY);
        title = getArguments().getString(constant.TITLE_KEY);
        publishAt = getArguments().getString(constant.PUBLISHED_AT__KEY);

        View rootView = inflater.inflate(R.layout.fragment_watch_video, container, false);
        initializeScreen(rootView);


        //setup the player fragment
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.watch_video_player, youTubePlayerFragment).commit();

        //get the publish date and convert it to date object
        Date dateObject = fromISO8601(publishAt);

        //format the object into date and time
        String date = formatDate(dateObject);
        String time = formatTime(dateObject);

        if (videoId != null && title != null && publishAt != null) {

            //set the current video information
            videoTitleTextView.setText(title);
            videoTimeTextView.setText(time);
            videoDateTextView.setText(date);


            youTubePlayerFragment.initialize(constant.API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

                    //load and play the video with current id
                    youTubePlayer.loadVideo(videoId);
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
        videoDateTextView = (TextView) view.findViewById(R.id.video_date_text_view);
        videoTimeTextView = (TextView) view.findViewById(R.id.video_time_text_view);
        videoTitleTextView = (TextView) view.findViewById(R.id.video_title_text_view);

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
