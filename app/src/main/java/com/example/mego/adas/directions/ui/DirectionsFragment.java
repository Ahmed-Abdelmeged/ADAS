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


package com.example.mego.adas.directions.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.directions.adapter.StepAdapter;
import com.example.mego.adas.directions.api.DirectionsApiClient;
import com.example.mego.adas.directions.api.DirectionsApiConstants;
import com.example.mego.adas.directions.api.DirectionsApiInterface;
import com.example.mego.adas.directions.api.model.Direction;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.utils.AdasUtils;
import com.example.mego.adas.utils.Constants;
import com.example.mego.adas.directions.api.DirectionsApiUtilities;
import com.example.mego.adas.utils.LocationUtilities;
import com.example.mego.adas.utils.networking.NetworkUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * for the directions
 */
public class DirectionsFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

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
    private Toast toast;
    private RecyclerView stepsRecyclerView;
    private StepAdapter stepAdapter;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 5;

    /**
     * Place is choose from place picker
     */
    private Place goingPlace = null;

    /**
     * start point is the user current location
     */
    private String startLocation;

    /**
     * Animatable object to cae the ripple effect
     */
    private Animatable mAnimatable;

    /**
     * boolean to indicate is view is visible or not
     */
    private boolean isEditTextVisible;

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
     * PolyLine that will draw the car way
     */
    Polyline carWayPolyLine;

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
            mOverViewPolylineDatabaseReference, mCurrentLocationDatabaseReference;

    private static final int PLACE_PICKER_REQUEST = 2576;

    private DirectionsApiInterface directionsApiInterface;

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
        setMapView(sharedPreferences);

        //set the button to onClick Listener
        addLocationButton.setOnClickListener(this);
        stepsListButton.setOnClickListener(this);

        //animation
        isEditTextVisible = false;
        revealView.setVisibility(View.INVISIBLE);

        //set the adapter
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        stepsRecyclerView.setLayoutManager(layoutManager);

        stepsRecyclerView.setHasFixedSize(true);

        stepAdapter = new StepAdapter();
        stepsRecyclerView.setAdapter(stepAdapter);

        if (NetworkUtil.isAvailableInternetConnection(getContext())) {
            //set up the firebase
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            //get the current user uid
            User currentUser = AuthenticationUtilities.getCurrentUser(getContext());
            String uid = currentUser.getUserUid();

            //get the references for the childes
            //the main child for the directions services
            setFirebaseReferences(uid);

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

        if (toast != null) {
            toast.cancel();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
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
        //if the internet is work start the loader if not show toast message
        if (NetworkUtil.isAvailableInternetConnection(getContext())) {
            buildGoogleApiClient();
        } else {
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
        LocationUtilities.enableSetLocation(getActivity(), mMap);
        if (NetworkUtil.isAvailableInternetConnection(getContext())) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationUtilities.enableMyLocation(getActivity(), mGoogleApiClient);

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

        LocationUtilities.enableUpdateMyLocation(getActivity(), mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * call when the connection with google api client Suspended
     */
    @Override
    public void onConnectionSuspended(int i) {
        showToast(getString(R.string.connection_maps_suspend));

    }

    /**
     * call when the connection with google api client failed
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showToast(getString(R.string.connection_maps_failed));
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

        DirectionsApiUtilities.AnimateMarker(marker, carPlace, false, mMap);
        cameraPosition = new CameraPosition.Builder()
                .target(carPlace).zoom(zoom).bearing(bearing).tilt(tilt).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
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
        if (NetworkUtil.isAvailableInternetConnection(getContext())) {
            switch (v.getId()) {
                case R.id.add_new_location_fab:
                    if (!isEditTextVisible) {
                        detailView.setVisibility(View.INVISIBLE);
                        revealEditText(revealView);
                        locationEditText.requestFocus();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            addLocationButton.setImageResource(R.drawable.icn_morp);
                            mAnimatable = (Animatable) (addLocationButton).getDrawable();
                            if (mAnimatable != null) {
                                mAnimatable.start();
                            }
                        }
                        revealView.setBackgroundColor(Color.WHITE);
                        startPlacePicker();
                    } else {
                        if (goingPlace != null) {
                            //put the going location in the firebase
                            mGoingLocationDatabaseReference.setValue(goingPlace.getLatLng());
                        }

                        hideEditText(revealView);
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                            revealView.setVisibility(View.INVISIBLE);
                        }

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            addLocationButton.setImageResource(R.drawable.icn_morph_reverse);
                            mAnimatable = (Animatable) (addLocationButton).getDrawable();
                            if (mAnimatable != null) {
                                mAnimatable.start();
                            }
                        }
                        locationImageView.setVisibility(View.INVISIBLE);

                        //check for the internet connection if is ok init the loader
                        loadingbar.setVisibility(View.VISIBLE);

                        if (NetworkUtil.isAvailableInternetConnection(getContext())) {
                            fetchDirectionsData();
                        } else {
                            showToast(getString(R.string.no_internet_connection));
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

    private void fetchDirectionsData() {

        stepAdapter.clear();

        if (carWayPolyLine != null) {
            carWayPolyLine.remove();
        }

        //put the start location in the firebase
        mStartLocationDatabaseReference.setValue(startLocation);

        directionsApiInterface = DirectionsApiClient.getDirectionApiClient()
                .create(DirectionsApiInterface.class);

        Call<Direction> call = directionsApiInterface.
                getDirections(startLocation, LocationUtilities.getLatLang(goingPlace));

        call.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                loadingbar.setVisibility(View.INVISIBLE);

                if (response.body() != null) {
                    mStepsDatabaseReference.removeValue();

                    Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
                    if (detailView.getVisibility() == View.INVISIBLE) {
                        detailView.startAnimation(slideUp);
                        detailView.setVisibility(View.VISIBLE);
                    }
                    if (response.body().getStatus().equals(DirectionsApiConstants.STATUES_OK)) {
                        distanceTextView.setText(DirectionsApiUtilities.getLegDistance(response.body()));
                        durationTextView.setText(DirectionsApiUtilities.getLegDuration(response.body()));
                        drawPolyline(DirectionsApiUtilities.getOverViewPolyLine(response.body()));
                        stepAdapter.setSteps(DirectionsApiUtilities.getSteps(response.body()));

                        //Put the value in the firebase
                        mStepsDatabaseReference.push().setValue(DirectionsApiUtilities.getSteps(response.body()));

                        mLegDurationTextDatabaseReference.setValue(DirectionsApiUtilities.getLegDuration(response.body()));
                        mLegDistanceTextDatabaseReference.setValue(DirectionsApiUtilities.getLegDistance(response.body()));
                        mOverViewPolylineDatabaseReference.setValue(DirectionsApiUtilities.getOverViewPolyLine(response.body()));


                    } else {
                        showToast(DirectionsApiUtilities.checkResponseState(response.body().getStatus()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                loadingbar.setVisibility(View.INVISIBLE);
                Timber.e(t.getLocalizedMessage());
            }
        });
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
     * helper method to build google api client
     */
    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(getActivity(), this)
                .build();
    }

    /**
     * Called when the place picker activity returns back with a selected place
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(getContext(), data);
            goingPlace = place;

            //get the first name of the address
            String shortAddress = AdasUtils.getShortenAddress(place);

            locationEditText.setText(shortAddress);
            destinationTextView.setText(shortAddress);
            locationEditText.setOnClickListener(v -> startPlacePicker());
            if (place == null) {
                return;
            }
        }
    }

    /**
     * Method to start place picker
     */
    private void startPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Intent placePickerIntent = null;
        try {
            placePickerIntent = builder.build(getActivity());
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        } catch (Exception e) {
        }
        startActivityForResult(placePickerIntent, PLACE_PICKER_REQUEST);
    }

    /**
     * Method to set firebase references
     */
    private void setFirebaseReferences(String uid) {
        //the childes for the direction root
        mStartLocationDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_START_LOCATION);

        mGoingLocationDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_GOING_LOCATION);

        mCurrentLocationDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_CURRENT_LOCATION);

        mLegDistanceTextDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_LEG_DISTANCE_TEXT);

        mLegDurationTextDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_LEG_DURATION_TEXT);

        mOverViewPolylineDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_LEG_OVERVIEW_POLYLINE);

        mStepsDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constants.FIREBASE_USERS)
                .child(uid).child(Constants.FIREBASE_USER_INFO)
                .child(Constants.FIREBASE_DIRECTIONS).child(Constants.FIREBASE_STEPS);
    }


    /**
     * Method used to initialize the UI element
     */
    private void initializeScreen(View view) {
        addLocationButton = view.findViewById(R.id.add_new_location_fab);

        locationEditText = view.findViewById(R.id.location_edit_text);

        revealView = view.findViewById(R.id.edit_text_holder);
        detailView = view.findViewById(R.id.detail_view);

        locationImageView = view.findViewById(R.id.locationImageView);
        stepsListButton = view.findViewById(R.id.down_page_Image_View);

        loadingbar = view.findViewById(R.id.loading_indicator_progress);

        distanceTextView = view.findViewById(R.id.distance_textView);
        durationTextView = view.findViewById(R.id.duration_textView);
        destinationTextView = view.findViewById(R.id.destination_textView);

        stepsRecyclerView = view.findViewById(R.id.steps_recyclerView);

        mapView = view.findViewById(R.id.map_view);
    }

    /**
     * Method to set map view
     */
    private void setMapView(SharedPreferences sharedPreferences) {
        zoom = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.settings_map_zoom_key),
                getString(R.string.settings_map_zoom_default)));

        bearing = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.settings_map_bearing_key),
                getString(R.string.settings_map_bearing_default)));

        tilt = Float.parseFloat(sharedPreferences.getString(
                getString(R.string.settings_map_tilt_key),
                getString(R.string.settings_map_tilt_default)));
    }
}
