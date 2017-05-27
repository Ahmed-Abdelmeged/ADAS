package com.example.mego.adas.ui;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.model.Accident;
import com.example.mego.adas.utils.constant;
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
public class AccidentFragment extends Fragment {

    /**
     * UI Elements
     */
    private ListView accidentsListView;
    private ProgressBar loadingBar;
    private TextView emptyText;

    /**
     * adapter for accidents list
     */
    private AccidentAdapter mAapter;

    /**
     * Tag for the log
     */
    private static final String LOG_TAG = AccidentFragment.class.getSimpleName();

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

        mAapter = new AccidentAdapter(getContext(), new ArrayList<Accident>());
        accidentsListView.setAdapter(mAapter);
        accidentsListView.setEmptyView(emptyText);

        //check the internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //if the internet is work start the loader if not show toast message
        if (networkInfo != null && networkInfo.isConnected()) {


            //set up the firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            //get the current user uid
            String uid = AuthenticationUtilities.getCurrentUser(getContext());

            //get the references for the childes
            accidentsDatabaseReference = mFirebaseDatabase.getReference().child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO).child(constant.FIREBASE_ACCIDENTS);


            //initialize the child listener
            accidentsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        loadingBar.setVisibility(View.VISIBLE);
                        Accident accident = dataSnapshot.getValue(Accident.class);
                        mAapter.addAll(accident);
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

        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            emptyText.setText(getString(R.string.no_internet_connection));
        }
        accidentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the current accident
                Accident currentAccident = mAapter.getItem(position);

                AccidentDetailFragment accidentDetailFragment = new AccidentDetailFragment();

                //set the accident information to the next fragment
                Bundle args = new Bundle();
                args.putString(constant.ACCIDENT_TITLE_KEY, currentAccident.getAccidentTitle() + " " + (position + 1));
                args.putString(constant.ACCIDENT_DATE_KEY, currentAccident.getDate());
                args.putString(constant.ACCIDENT_TIME_KEY, currentAccident.getTime());
                args.putDouble(constant.ACCIDENT_LONGITUDE_KEY, currentAccident.getAccidentLongitude());
                args.putDouble(constant.ACCIDENT_LATITUDE_KEY, currentAccident.getAccidentLatitude());

                accidentDetailFragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, accidentDetailFragment)
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
        accidentsListView = (ListView) view.findViewById(R.id.accident_listView);
        loadingBar = (ProgressBar) view.findViewById(R.id.loading_bar_accident);
        emptyText = (TextView) view.findViewById(R.id.empty_text_accident);
    }


}
