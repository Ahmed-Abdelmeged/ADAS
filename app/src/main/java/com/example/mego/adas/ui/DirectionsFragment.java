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


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.adapter.StepAdapter;
import com.example.mego.adas.api.DirectionsAPI;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.loader.DirectionsLoader;
import com.example.mego.adas.model.Directions;
import com.example.mego.adas.model.Steps;
import com.example.mego.adas.utils.constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * for the directions
 */
public class DirectionsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<ArrayList<Directions>> {

    /**
     * UI Element
     */
    private TextView distanceTextView, durationTextView, destinationTextView;
    private EditText locationEditText;
    private ProgressBar loadingbar;
    private FloatingActionButton addLocationButton;
    private ImageView locationImageView;
    private LinearLayout revealView, detailView;
    private ImageView stepsListButton;
    private LinearLayout mapView;

    /**
     * Tag fro the Log and debug
     */
    public static final String LOG_TAG = DirectionsFragment.class.getSimpleName();

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 5;

    /**
     * the strings from the edit text
     */
    private String goingLocation = "";

    /**
     * start point is the user current location
     */
    private String startLocation;

    /**
     * loader id
     */
    private static final int DIRECTIONS_LOADER_ID = 2;


    /**
     * Animatable object to cae the ripple effect
     */
    private Animatable mAnimatable;

    /**
     * boolean to indicate is view is visible or not
     */
    private boolean isEditTextVisible;

    /**
     * Array list to hold the step information
     */
    ArrayList<Steps> stepsArrayList = new ArrayList<>();

    /**
     * Google Maps Objects
     */
    GoogleApiClient mGoogleApiClient;
    Location location;
    GoogleMap mMap;
    boolean mapReady = false;
    CameraPosition cameraPosition;
    MarkerOptions carPlaceMarker;
    LatLng carPlace;
    Marker marker;
    LocationRequest mLocationRequest;

    /**
     * the current position of the car
     */
    double longitude, latitude = 0;

    /**
     * variables for the directions
     */
    String statues;
    String legDurationText;
    String legDistanceText;
    String overview_polyline_string;

    /**
     * PolyLine that will draw the car way
     */
    Polyline carWayPolyLine;

    /**
     * step variables
     */

    String html_instructions;

    String points;

    int stepDistanceValue;
    String stepDistanceText;

    int stepDurationValue;
    String stepDurationText;

    double stepStartLatitude;
    double stepStartLongitude;

    double stepEndLatitude;
    double stepEndLongitude;

    /**
     * adapter for step list view
     */
    private StepAdapter mAdapter;

    private ListView stepListView;

    /**
     * camera settings
     */
    private float zoom = 0, bearing = 0, tilt = 0;


    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mStepsDatabaseReference, mStartLocationDatabaseReference,
            mGoingLocationDatabaseReference, mLegDistanceTextDatabaseReference, mLegDurationTextDatabaseReference,
            mOverViewPolylineDatabaseReference, mDirecionsDatabaseReference, mCurrentLocationDatabaseReference;

    /**
     * check the internet state
     */
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;


    public DirectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_directions, container, false);
        initializeScreen(rootView);

        //get the current settings for the camera settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //extract  the camera values value
        zoom = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.settings_map_zoom_key),
                getString(R.string.settings_map_zoom_default)));

        bearing = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.settings_map_bearing_key),
                getString(R.string.settings_map_bearing_default)));

        tilt = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.settings_map_tilt_key),
                getString(R.string.settings_map_tilt_default)));

        //set the button to onClick Listener
        addLocationButton.setOnClickListener(this);
        stepsListButton.setOnClickListener(this);

        //animation
        isEditTextVisible = false;
        revealView.setVisibility(View.INVISIBLE);

        //set the adapter
        mAdapter = new StepAdapter(getContext(), new ArrayList<Steps>());
        stepListView.setAdapter(mAdapter);

        //check the internet connection
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            //set up the firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            //get the current user uid
            User currentUser = AuthenticationUtilities.getCurrentUser(getContext());
            String uid = currentUser.getUserUid();

            //get the references for the childes
            //the main child for the directions services
            mDirecionsDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS);

            //the childes for the direction root
            mStartLocationDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_START_LOCATION);

            mGoingLocationDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_GOING_LOCATION);

            mCurrentLocationDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_CURRENT_LOCATION);

            mLegDistanceTextDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_LEG_DISTANCE_TEXT);

            mLegDurationTextDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_LEG_DURATION_TEXT);

            mOverViewPolylineDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_LEG_OVERVIEW_POLYLINE);

            mStepsDatabaseReference = mFirebaseDatabase.getReference()
                    .child(constant.FIREBASE_USERS)
                    .child(uid).child(constant.FIREBASE_USER_INFO)
                    .child(constant.FIREBASE_DIRECTIONS).child(constant.FIREBASE_STEPS);


        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();

        }


        //initialize google map fragment
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.google_map_location);
        mapFragment.getMapAsync(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //remove google map fragment
        //because it not destroy when switch between fragments
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.google_map_location);
        if (mapFragment != null) {
            getActivity().getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }

    /**
     * when the map is ready call this
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;
        //check the internet connection

        //check the internet connection

        //if the internet is work start the loader if not show toast message
        if (networkInfo != null && networkInfo.isConnected()) {
            buildGoogleApiClient();
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
        enableSetLocation();
        if (networkInfo != null && networkInfo.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        enableMyLocation();

        if (location != null) {
            //get the longitude and the  latitude from the location object
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            startLocation = longitude + "," + latitude;

            carPlaceMarker = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Car Place")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));

            carPlace = new LatLng(latitude, longitude);

            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(zoom)
                    .bearing(bearing)
                    .tilt(tilt)
                    .build();

            //check for the map state if it's ready start
            if (mapReady) {

                marker = mMap.addMarker(carPlaceMarker);

            }
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        enableUpdateMyLocation();
    }

    /**
     * call when the connection with google api client Suspended
     */
    @Override
    public void onConnectionSuspended(int i) {
        msg("Connection With Google Maps Suspended");

    }

    /**
     * call when the connection with google api client failed
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        msg("Connection With Google Maps Failed");
    }

    /**
     * method called when the car current location changed
     */
    @Override
    public void onLocationChanged(Location location) {
        startLocation = location.getLatitude() + "," + location.getLongitude();

        carPlace = new LatLng(location.getLatitude(), location.getLongitude());

        //put the current location in the firebase
        mCurrentLocationDatabaseReference.setValue(startLocation);

        animateMarker(marker, carPlace, false);
        cameraPosition = new CameraPosition.Builder()
                .target(carPlace).zoom(zoom).bearing(bearing).tilt(tilt).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * fast way to call Toast
     */
    private void msg(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    /**
     * method triggered when the app need a run time permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * helper method to check the response code form the direction API Response State
     */
    private void checkResponseState(String statues) {
        switch (statues) {
            case DirectionsAPI.STATUES_INVALID_REQUEST:
                msg(DirectionsAPI.STATUES_INVALID_REQUEST);
                break;
            case DirectionsAPI.STATUES_MAX_ROUTE_LENGTH_EXCEEDED:
                msg(DirectionsAPI.STATUES_MAX_ROUTE_LENGTH_EXCEEDED);
                break;
            case DirectionsAPI.STATUES_NOT_FOUND:
                msg(DirectionsAPI.STATUES_NOT_FOUND);
                break;
            case DirectionsAPI.STATUES_OVER_QUERY_LIMIT:
                msg(DirectionsAPI.STATUES_OVER_QUERY_LIMIT);
                break;
            case DirectionsAPI.STATUES_REQUEST_DENIED:
                msg(DirectionsAPI.STATUES_REQUEST_DENIED);
                break;
            case DirectionsAPI.STATUES_UNKNOWN_ERROR:
                msg(DirectionsAPI.STATUES_UNKNOWN_ERROR);
                break;
            case DirectionsAPI.STATUES_ZERO_RESULTS:
                msg(DirectionsAPI.STATUES_ZERO_RESULTS);
                break;
            default:
                msg(DirectionsAPI.STATUES_OK);
                break;
        }
    }

    /**
     * helper method to draw the polyline
     */
    private void drawPolyline(String polyline) {
        List<LatLng> points = PolyUtil.decode(polyline);
        carWayPolyLine = mMap.addPolyline(new PolylineOptions().geodesic(true).addAll(points));
    }

    /**
     * implement the click of the floating action button click
     */
    @Override
    public void onClick(View v) {
        if (networkInfo != null && networkInfo.isConnected()) {
            switch (v.getId()) {
                case R.id.add_new_location_fab:
                    if (!isEditTextVisible) {
                        detailView.setVisibility(View.INVISIBLE);
                        revealEditText(revealView);
                        locationEditText.requestFocus();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            addLocationButton.setImageResource(R.drawable.icn_morp);
                            mAnimatable = (Animatable) (addLocationButton).getDrawable();
                            mAnimatable.start();
                        }
                        revealView.setBackgroundColor(Color.WHITE);

                    } else {
                        goingLocation = locationEditText.getText().toString();

                        //put the going location in the firebase
                        mGoingLocationDatabaseReference.setValue(goingLocation);

                        hideEditText(revealView);
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                            revealView.setVisibility(View.INVISIBLE);
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            addLocationButton.setImageResource(R.drawable.icn_morph_reverse);
                            mAnimatable = (Animatable) (addLocationButton).getDrawable();
                            mAnimatable.start();
                        }
                        locationImageView.setVisibility(View.INVISIBLE);

                        //check for the internet connection if is ok init the loader
                        loadingbar.setVisibility(View.VISIBLE);

                        if (networkInfo != null && networkInfo.isConnected()) {
                            //create the uri request with the specific query parameters

                            LoaderManager loaderManager = getActivity().getLoaderManager();
                            loaderManager.restartLoader(DIRECTIONS_LOADER_ID, null, DirectionsFragment.this).forceLoad();
                        } else {
                            msg("No Internet Connection");
                            loadingbar.setVisibility(View.INVISIBLE);
                        }


                    }
                    break;
                case R.id.down_page_Image_View:
                    //setup the animation
                    Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
                    Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_map);

                    if (mapView.getVisibility() == View.GONE) {
                        mapView.setVisibility(View.VISIBLE);
                        mapView.startAnimation(slideDown);
                        stepsListButton.setImageResource(R.drawable.ic_up_page);
                    } else {
                        mapView.startAnimation(slideUp);
                        mapView.setVisibility(View.GONE);
                        stepsListButton.setImageResource(R.drawable.ic_down_page);
                    }
                    break;
            }
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * method used to show the reveal effect to the edit text
     */
    private void revealEditText(LinearLayout view) {
        int centerX = view.getRight() - 30;
        int centerY = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());

        //work with api 21 or above
        Animator animator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
        }
        view.setVisibility(View.VISIBLE);

        isEditTextVisible = true;
        if (animator != null) {
            animator.start();
        }
    }

    /**
     * method to hide edit text with animation
     */
    private void hideEditText(final LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        }
        if (anim != null) {
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });
        }
        isEditTextVisible = false;
        if (anim != null) {
            anim.start();
        }
    }

    /**
     * a function to move the marker from place to other
     *
     * @param marker     marker object
     * @param toPosition to the position i want from started one
     * @param hideMarker set true if i want to hide the marker
     */
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    /**
     * helper method to check the permission and find the current location
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

        {
            location = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * helper method to check the permission and find the current location
     */
    private void enableSetLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

        {
            mMap.setMyLocationEnabled(true);

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * helper method to check the permission and find the current location
     */
    private void enableUpdateMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public Loader<ArrayList<Directions>> onCreateLoader(int id, Bundle args) {


        //create the uri request with the specific query parameters
        Uri baseUri = Uri.parse(DirectionsAPI.GOOGLE_DIRECTION_API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        uriBuilder.appendQueryParameter(DirectionsAPI.QUERY_PARAMETER_ORIGIN, startLocation);

        //put the start location in the firebase
        mStartLocationDatabaseReference.setValue(startLocation);

        uriBuilder.appendQueryParameter(DirectionsAPI.QUERY_PARAMETER_DESTINATION, goingLocation);
        uriBuilder.appendQueryParameter(DirectionsAPI.QUERY_PARAMETER_KEY, constant.API_KEY);

        //Log.e(LOG_TAG, uriBuilder.toString());
        return new DirectionsLoader(getContext(), uriBuilder.toString());

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Directions>> loader, ArrayList<Directions> data) {

        String step_detials = "";

        mAdapter.clear();
        stepsArrayList.clear();

        loadingbar.setVisibility(View.INVISIBLE);
        if (data != null && !data.isEmpty()) {
            ArrayList<Directions> directions = data;
            Directions directionsData;

            mStepsDatabaseReference.removeValue();

            for (int i = 0; i < directions.size(); i++) {
                directionsData = directions.get(i);
                statues = directionsData.getStatues();
                if (statues.equals(DirectionsAPI.STATUES_OK)) {
                    legDistanceText = directionsData.getLegDistanceText();
                    legDurationText = directionsData.getLegDurationText();
                    overview_polyline_string = directionsData.getOverview_polyline_string();

                    html_instructions = directionsData.getHtml_instructions();
                    points = directionsData.getPoints();
                    stepDistanceValue = directionsData.getStepDistanceValue();
                    stepDistanceText = directionsData.getStepDistanceText();
                    stepDurationValue = directionsData.getStepDurationValue();
                    stepDurationText = directionsData.getStepDurationText();
                    stepStartLatitude = directionsData.getStepStartLatitude();
                    stepStartLongitude = directionsData.getStepStartLongitude();
                    stepEndLatitude = directionsData.getStepEndLatitude();
                    stepEndLongitude = directionsData.getStepEndLongitude();

                    Steps steps = new Steps(html_instructions, points, stepDistanceValue, stepDistanceText,
                            stepDurationValue, stepDurationText, stepStartLatitude, stepStartLongitude,
                            stepEndLatitude, stepEndLongitude);

                    stepsArrayList.add(steps);

                    //put the steps in the firebase
                    mStepsDatabaseReference.push().setValue(steps);

                    if (carWayPolyLine != null) {
                        carWayPolyLine.remove();
                    }


                    distanceTextView.setText(legDistanceText);
                    durationTextView.setText(legDurationText);
                    destinationTextView.setText(goingLocation);
                    drawPolyline(overview_polyline_string);

                    //put the led information in the firebase
                    mLegDurationTextDatabaseReference.setValue(legDurationText);
                    mLegDistanceTextDatabaseReference.setValue(legDistanceText);
                    mOverViewPolylineDatabaseReference.setValue(overview_polyline_string);

                } else {
                    checkResponseState(statues);
                }
            }

            Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
            if (detailView.getVisibility() == View.INVISIBLE) {
                detailView.startAnimation(slideUp);
                detailView.setVisibility(View.VISIBLE);
            }

        }
        mAdapter.addAll(stepsArrayList);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Directions>> loader) {
        distanceTextView.setText("");
        durationTextView.setText("");
        distanceTextView.setText("");
        mAdapter.clear();
        stepsArrayList.clear();
    }

    /**
     * helper method to build google api client
     */
    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Method used to initialize the UI element
     */
    private void initializeScreen(View view) {
        addLocationButton = (FloatingActionButton) view.findViewById(R.id.add_new_location_fab);

        locationEditText = (EditText) view.findViewById(R.id.location_edit_text);

        revealView = (LinearLayout) view.findViewById(R.id.edit_text_holder);
        detailView = (LinearLayout) view.findViewById(R.id.detail_view);

        locationImageView = (ImageView) view.findViewById(R.id.locationImageView);
        stepsListButton = (ImageView) view.findViewById(R.id.down_page_Image_View);

        loadingbar = (ProgressBar) view.findViewById(R.id.loading_indicator_progress);

        distanceTextView = (TextView) view.findViewById(R.id.distance_textView);
        durationTextView = (TextView) view.findViewById(R.id.duration_textView);
        destinationTextView = (TextView) view.findViewById(R.id.destination_textView);

        stepListView = (ListView) view.findViewById(R.id.steps_listView);

        mapView = (LinearLayout) view.findViewById(R.id.map_view);
    }

}
