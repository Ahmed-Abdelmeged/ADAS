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


package com.example.mego.adas.accidents.ui;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.accidents.adapter.AccidentAdapterRecycler;
import com.example.mego.adas.accidents.adapter.AccidentClickCallBacks;
import com.example.mego.adas.accidents.viewmodel.AccidentViewModel;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.accidents.db.entity.Accident;
import com.example.mego.adas.utils.Constants;
import com.example.mego.adas.utils.networking.NetworkUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * <p>
 * to show list of accidents
 */
public class AccidentFragment extends LifecycleFragment implements
        AccidentClickCallBacks {

    /**
     * UI Elements
     */
    private RecyclerView accidentsRecycler;
    private ProgressBar loadingBar;
    private TextView emptyText;

    /**
     * adapter for accidents list
     */
    private AccidentAdapterRecycler accidentAdapterRecycler;
    private AccidentFragment accidentFragment;
    private AccidentViewModel viewModel;
    private List<Accident> currentAccdeints = new ArrayList<>();

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference accidentsDatabaseReference;

    private ChildEventListener accidentsEventListener;


    public AccidentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accident, container, false);
        initializeScreen(rootView);

        accidentFragment = (AccidentFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
        accidentAdapterRecycler = new AccidentAdapterRecycler(this);
        viewModel = ViewModelProviders.of(this).get(AccidentViewModel.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        accidentsRecycler.setLayoutManager(layoutManager);
        accidentsRecycler.setHasFixedSize(true);
        accidentsRecycler.setAdapter(accidentAdapterRecycler);

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //get the current user uid
        User currentUser = AuthenticationUtilities.getCurrentUser(getContext());
        String uid = currentUser.getUserUid();

        //get the references for the childes
        accidentsDatabaseReference = mFirebaseDatabase.getReference().child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO).child(Constants.FIREBASE_ACCIDENTS);

        //if the internet is work start the loader if not show toast message
        if (NetworkUtil.isAvailableInternetConnection(getContext())) {
            if (accidentFragment.isAdded()) {
                getAccidentsFromFromFirebase();
            }
        } else {
            viewModel.getAccidents().observe(this, accidents -> {
                if (accidents != null) {
                    emptyText.setVisibility(View.GONE);
                    accidentAdapterRecycler.setAccidents(accidents);
                } else {
                    emptyText.setVisibility(View.VISIBLE);
                }
            });
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAccidentClick(String accidentID) {
        AccidentDetailFragment accidentDetailFragment = new AccidentDetailFragment();
        //set the accident information to the next fragment
        Bundle args = new Bundle();
        args.putString(Constants.ACCIDENT_ID_KEY, accidentID);
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
        loadingBar.setVisibility(View.VISIBLE);
        deleteAllAccidents();
        //initialize the child listener
        accidentsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    emptyText.setVisibility(View.GONE);
                    Accident accident = dataSnapshot.getValue(Accident.class);
                    accident.setAccidentId(dataSnapshot.getKey());
                    viewModel.addAccident(accident);
                    currentAccdeints.add(accident);
                    accidentAdapterRecycler.addAccident(accident);
                    loadingBar.setVisibility(View.INVISIBLE);
                } else {
                    loadingBar.setVisibility(View.INVISIBLE);
                    emptyText.setVisibility(View.VISIBLE);
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
            if (currentAccdeints != null)
                accidentAdapterRecycler.clearAccidents();
            viewModel.deleteAccidents(currentAccdeints);
        }
    }

    /**
     * Link the UI Element with XML
     */
    private void initializeScreen(View view) {
        accidentsRecycler = view.findViewById(R.id.accident_recycler_view);
        loadingBar = view.findViewById(R.id.loading_bar_accident);
        emptyText = view.findViewById(R.id.empty_text_accident);
    }

}
