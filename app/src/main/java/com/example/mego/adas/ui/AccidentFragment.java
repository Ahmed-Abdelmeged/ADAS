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


import android.app.LoaderManager;
import android.arch.lifecycle.LifecycleFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.adapter.AccidentAdapter;
import com.example.mego.adas.adapter.AccidentCursorAdapter;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.db.AccidentsContract.AccidentsEntry;
import com.example.mego.adas.db.entity.Accident;
import com.example.mego.adas.utils.Constant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * to show list of accidents
 */
public class AccidentFragment extends LifecycleFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * UI Elements
     */
    private ListView accidentsListView;
    private ProgressBar loadingBar;
    private TextView emptyText;

    /**
     * adapter for accidents list
     */
    private AccidentAdapter mAccidentAdapter;
    private AccidentFragment accidentFragment;

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference accidentsDatabaseReference;

    private ChildEventListener accidentsEventListener;

    /**
     * Identifier for the accident data loader
     */
    private static final int ACCIDENT_LOADER_ID = 2698;

    private AccidentCursorAdapter mCursorAdapter;


    public AccidentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accident, container, false);
        initializeScreen(rootView);

        accidentFragment = (AccidentFragment) getFragmentManager().findFragmentById(R.id.fragment_container);


        accidentsListView.setAdapter(mAccidentAdapter);
        accidentsListView.setEmptyView(emptyText);

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //get the current user uid
        User currentUser = AuthenticationUtilities.getCurrentUser(getContext());
        String uid = currentUser.getUserUid();

        //get the references for the childes
        accidentsDatabaseReference = mFirebaseDatabase.getReference().child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO).child(Constant.FIREBASE_ACCIDENTS);

        //if the internet is work start the loader if not show toast message
        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            mAccidentAdapter = new AccidentAdapter(getContext(), new ArrayList<Accident>());
            accidentsListView.setAdapter(mAccidentAdapter);
            getAccidentsFromFromFirebase();
            startAccidentDetailFragment();
        } else {
            mCursorAdapter = new AccidentCursorAdapter(getContext(), null);
            accidentsListView.setAdapter(mCursorAdapter);

            //Kick of the loader
            getActivity().getLoaderManager().
                    initLoader(ACCIDENT_LOADER_ID, null, AccidentFragment.this);

            startAccidentDetailFragmentNoConnection();
        }

        accidentsListView.setEmptyView(emptyText);


        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Link the UI Element with XML
     */
    private void initializeScreen(View view) {
        accidentsListView = view.findViewById(R.id.accident_listView);
        loadingBar = view.findViewById(R.id.loading_bar_accident);
        emptyText = view.findViewById(R.id.empty_text_accident);
    }

    /**
     * Method to start the accident detail fragment with the accident details
     * When an accident is pressed
     */
    private void startAccidentDetailFragment() {
        accidentsListView.setOnItemClickListener((parent, view, position, id) -> {

            //get the current accident
            Accident currentAccident = mAccidentAdapter.getItem(position);
            setAccidentDetails(currentAccident);
        });
    }

    /**
     * Method to start accident details fragment with no internet connection
     */
    private void startAccidentDetailFragmentNoConnection() {
        accidentsListView.setOnItemClickListener((parent, view, position, id) -> {
            AccidentDetailFragment accidentDetailFragment = new AccidentDetailFragment();

            //set the accident information to the next fragment
            Bundle args = new Bundle();
            Uri currentAccidentUri = ContentUris.withAppendedId(AccidentsEntry.CONTENT_URI, id);

            args.putParcelable(Constant.ACCIDENT_URI_KEY, currentAccidentUri);
            args.putString(Constant.ACCIDENT_START_MODE_KEY, Constant.ACCIDENT_MODE_OFFLINE);

            accidentDetailFragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, accidentDetailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    /**
     * Method to put the accident information and pass it as intent to the accident details fragment
     */
    private void setAccidentDetails(Accident currentAccident) {
        AccidentDetailFragment accidentDetailFragment = new AccidentDetailFragment();

        //set the accident information to the next fragment
        Bundle args = new Bundle();
        args.putString(Constant.ACCIDENT_TITLE_KEY, currentAccident.getAccidentTitle());
        args.putString(Constant.ACCIDENT_DATE_KEY, currentAccident.getDate());
        args.putString(Constant.ACCIDENT_TIME_KEY, currentAccident.getTime());
        args.putDouble(Constant.ACCIDENT_LONGITUDE_KEY, currentAccident.getAccidentLongitude());
        args.putDouble(Constant.ACCIDENT_LATITUDE_KEY, currentAccident.getAccidentLatitude());
        args.putString(Constant.ACCIDENT_START_MODE_KEY, Constant.ACCIDENT_MODE_ONLINE);

        accidentDetailFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, accidentDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Method to get th accidents form firebase
     */
    private void getAccidentsFromFromFirebase() {
        deleteAllAccidents();
        //initialize the child listener
        accidentsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    loadingBar.setVisibility(View.VISIBLE);
                    Accident accident = dataSnapshot.getValue(Accident.class);
                    insertAccident(accident, dataSnapshot.getKey());
                    mAccidentAdapter.addAll(accident);
                    loadingBar.setVisibility(View.INVISIBLE);
                } else {
                    loadingBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //set a listener to the reference
        accidentsDatabaseReference.addChildEventListener(accidentsEventListener);

    }

    /**
     * Method to delete all the accidents to accept new accidents
     */
    private void deleteAllAccidents() {
        if (accidentFragment.isAdded()) {
            int rowsDeleted = getActivity().getContentResolver()
                    .delete(AccidentsEntry.CONTENT_URI, null, null);
        }
    }

    /**
     * Method to insert accident into the SQLite database
     */
    private void insertAccident(Accident accident, String accidentID) {
        //get data form accident object
        String accidentTitle = accident.getAccidentTitle();
        double accidentLongitude = accident.getAccidentLongitude();
        double accidentLatitude = accident.getAccidentLatitude();
        String accidentTime = accident.getTime();
        String accidentDate = accident.getDate();

        // Create a ContentValues object where column names are the keys,
        ContentValues values = new ContentValues();
        values.put(AccidentsEntry.COLUMN_ACCIDENT_LATITUDE, accidentLatitude);
        values.put(AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE, accidentLongitude);
        values.put(AccidentsEntry.COLUMN_ACCIDENT_TITLE, accidentTitle);
        values.put(AccidentsEntry.COLUMN_ACCIDENT_DATE, accidentDate);
        values.put(AccidentsEntry.COLUMN_ACCIDENT_TIME, accidentTime);
        values.put(AccidentsEntry.COLUMN_ACCIDENT_ID, accidentID);

        // This is a NEW accident, so insert a new accident into the provider,
        // returning the content URI for the new accident.
        if (accidentFragment.isAdded()) {
            Uri newUri = getActivity().getContentResolver().insert(AccidentsEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(getContext(), "Error with saving accidents",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                AccidentsEntry._ID,
                AccidentsEntry.COLUMN_ACCIDENT_LATITUDE,
                AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE,
                AccidentsEntry.COLUMN_ACCIDENT_TITLE,
                AccidentsEntry.COLUMN_ACCIDENT_DATE,
                AccidentsEntry.COLUMN_ACCIDENT_TIME,
                AccidentsEntry.COLUMN_ACCIDENT_ID};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getContext(),   // Parent activity context
                AccidentsEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link AccidentCursorAdapter} with this new cursor containing updated accident data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
