package com.example.mego.adas.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.adapter.YoutubeVideoAdapter;
import com.example.mego.adas.api.YoutubeAPI;
import com.example.mego.adas.loader.VideosLoader;
import com.example.mego.adas.model.YouTubeVideo;
import com.example.mego.adas.utils.constant;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * to show list of videos
 */
public class VideosFragments extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<YouTubeVideo>> {


    /**
     * UI Elements
     */
    private ListView videosListView;
    private ProgressBar loadingBar;
    private TextView emptyText;

    /**
     * adapter for  video list view
     */
    private YoutubeVideoAdapter mAdapter;

    /**
     * Tag for the log
     */
    private static final String LOG_TAG = VideosFragments.class.getSimpleName();

    /**
     * unique id to identify videos loader
     */
    private static final int VIDEOS_LOADER_ID = 1;


    public VideosFragments() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_videos_fragments, container, false);
        initializeScreen(rootView);

        //setup the adapter
        mAdapter = new YoutubeVideoAdapter(getContext(), new ArrayList<YouTubeVideo>());
        videosListView.setAdapter(mAdapter);
        videosListView.setEmptyView(emptyText);

        //check the internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //if the internet is work start the loader if not show toast message
        if (networkInfo != null && networkInfo.isConnected()) {
            loadingBar.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getActivity().getLoaderManager();
            loaderManager.initLoader(VIDEOS_LOADER_ID, null, VideosFragments.this);
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            emptyText.setText(getString(R.string.no_internet_connection));
        }

        //open a video fragment and shown it after the item in the list is clicked
        videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the current video
                YouTubeVideo currentVideo = mAdapter.getItem(position);

                WatchVideoFragment watchVideoFragment = new WatchVideoFragment();

                //set the video information to the next fragment
                Bundle args = new Bundle();

                args.putString(constant.VIDEO_KEY, currentVideo.getVideoId());
                args.putString(constant.TITLE_KEY, currentVideo.getTitle());
                args.putString(constant.PUBLISHED_AT__KEY, currentVideo.getPublishedAt());

                watchVideoFragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, watchVideoFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Link the UI Element with XML
     */
    private void initializeScreen(View view) {
        videosListView = (ListView) view.findViewById(R.id.videos_listView);
        loadingBar = (ProgressBar) view.findViewById(R.id.loading_bar);
        emptyText = (TextView) view.findViewById(R.id.empty_text_videos);
    }


    /**
     * call when the fragment is created to build the uri and create the loader
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<ArrayList<YouTubeVideo>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(YoutubeAPI.YOUTUBE_DATA_API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        //get the current settings for the video settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //extract  the playlist value
        String playlistId = sharedPreferences.getString(
                getString(R.string.settings_playlist_id_key),
                getString(R.string.settings_playlist_id_default));

        uriBuilder.appendQueryParameter(YoutubeAPI.QUERY_PARAMETER_PART, YoutubeAPI.ELEMENT_SNIPPET);
        uriBuilder.appendQueryParameter(YoutubeAPI.QUERY_PARAMETER_PLAYLISTID, playlistId);
        uriBuilder.appendQueryParameter(YoutubeAPI.QUERY_PARAMETER_MAX_RESULTS, "50");
        uriBuilder.appendQueryParameter(YoutubeAPI.QUERY_PARAMETER_KEY, constant.API_KEY);


        Log.e(LOG_TAG, "onCreate");
        Log.e(LOG_TAG, uriBuilder.toString());
        return new VideosLoader(getContext(), uriBuilder.toString());
    }

    /**
     * call when the fetching DataSend finish and add all the video to  the adpter
     *
     * @param loader
     * @param videos
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<YouTubeVideo>> loader, ArrayList<YouTubeVideo> videos) {


        loadingBar.setVisibility(View.INVISIBLE);
        mAdapter.clear();
        emptyText.setText(getString(R.string.no_videos));
        if (videos != null && !videos.isEmpty()) {
            mAdapter.addAll(videos);
        }
        Log.e(LOG_TAG, "onLoadFinished");
    }

    /**
     * call when restart the loader and clear the list
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<YouTubeVideo>> loader) {
        mAdapter.clear();
        Log.e(LOG_TAG, "onLoadReset");
    }
}
