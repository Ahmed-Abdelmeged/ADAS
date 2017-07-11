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


package com.example.mego.adas.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.adapter.YouTubeVideosAdapter;
import com.example.mego.adas.api.youtube.YouTubeApiClient;
import com.example.mego.adas.api.youtube.YouTubeApiInterface;
import com.example.mego.adas.api.youtube.YouTubeApiUtilities;
import com.example.mego.adas.api.youtube.model.Item;
import com.example.mego.adas.api.youtube.model.YouTubeVideo;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * to show list of videos
 */
public class VideosFragments extends Fragment implements YouTubeVideosAdapter.YouTubeVideosAdapterOnClickHandler {


    /**
     * UI Elements
     */
    private ProgressBar loadingBar;
    private TextView emptyText;
    private RecyclerView videosRecycler;

    /**
     * adapter for  video list view
     */
    private YouTubeVideosAdapter youTubeVideosAdapter;

    /**
     * Tag for the log
     */
    private static final String LOG_TAG = VideosFragments.class.getSimpleName();

    private YouTubeApiInterface youTubeApiInterface;


    public VideosFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        initializeScreen(rootView);

        //setup the adapter
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        videosRecycler.setLayoutManager(layoutManager);
        videosRecycler.setHasFixedSize(true);

        youTubeVideosAdapter = new YouTubeVideosAdapter(this);
        videosRecycler.setAdapter(youTubeVideosAdapter);

        //if the internet is work start the loader if not show toast message
        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            loadingBar.setVisibility(View.VISIBLE);
            fetchVideosData();
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            emptyText.setText(getString(R.string.no_internet_connection));
        }

        //open a video fragment and shown it after the item in the list is clicked
     /*   videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the current video
                Item currentVideo = mAdapter.getItem(position);

                WatchVideoFragment watchVideoFragment = new WatchVideoFragment();

                //set the video information to the next fragment
                Bundle args = new Bundle();
                args.putSerializable(Constant.KEY_ITEM_VIDEO, currentVideo);
                watchVideoFragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, watchVideoFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });*/


        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Link the UI Element with XML
     */
    private void initializeScreen(View view) {
        videosRecycler = (RecyclerView) view.findViewById(R.id.videos_recycler_view);
        loadingBar = (ProgressBar) view.findViewById(R.id.loading_bar);
        emptyText = (TextView) view.findViewById(R.id.empty_text_videos);
    }

    private void fetchVideosData() {

        //get the current settings for the video settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //extract  the playlist value
        String playlistId = sharedPreferences.getString(
                getString(R.string.settings_playlist_id_key),
                getString(R.string.settings_playlist_id_default));


        youTubeApiInterface = YouTubeApiClient.getYoutubeApiClient()
                .create(YouTubeApiInterface.class);
        Call<YouTubeVideo> call = youTubeApiInterface.getVideos(playlistId);
        call.enqueue(new Callback<YouTubeVideo>() {
            @Override
            public void onResponse(Call<YouTubeVideo> call, Response<YouTubeVideo> response) {
                loadingBar.setVisibility(View.INVISIBLE);
                youTubeVideosAdapter.clear();
                emptyText.setText(getString(R.string.no_videos));
                if (response.body() != null) {
                    youTubeVideosAdapter.setVideos(YouTubeApiUtilities.getVideos(response.body()));
                }
            }

            @Override
            public void onFailure(Call<YouTubeVideo> call, Throwable t) {
                loadingBar.setVisibility(View.INVISIBLE);
                emptyText.setText(getString(R.string.no_videos));
            }
        });
    }

    @Override
    public void onCLick(Item item) {
        //get the current video

        WatchVideoFragment watchVideoFragment = new WatchVideoFragment();

        //set the video information to the next fragment
        Bundle args = new Bundle();
        args.putSerializable(Constant.KEY_ITEM_VIDEO, item);
        watchVideoFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, watchVideoFragment)
                .addToBackStack(null)
                .commit();
    }
}
