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


package com.example.mego.adas.main;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.main.model.MappingServices;
import com.example.mego.adas.main.model.SensorsValues;
import com.example.mego.adas.utils.AdasUtils;
import com.example.mego.adas.utils.Constant;
import com.example.mego.adas.directions.api.DirectionsApiUtilities;
import com.example.mego.adas.utils.LocationUtilities;
import com.example.mego.adas.utils.NotificationUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.widget.Toast.makeText;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * fragment show the car statues for the user
 */
public class UserFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 5;

    /**
     * The google Map elements
     */
    GoogleApiClient mGoogleApiClient;
    Location location;
    GoogleMap mMap;
    boolean mapReady = false;
    CameraPosition cameraPosition;
    MarkerOptions carPlace;
    LatLng accidentPlace;
    Marker marker, userMarker;
    LocationRequest mLocationRequest;

    /**
     * UI elements
     */
    private TextView tempSensorValueTextView, lDRSensorValueTextView, potSensorValueTextView,
            tempTextView, ldrTextView, potTextView;
    private LinearLayout userFragment;
    private ProgressBar tempProgressBar, potProgressBar, ldrProgressBar;
    private FloatingActionButton lightsButton, startButton, lockButton, userLocationButton;
    private Toast toast;

    /**
     * Handler to set the progressbar progress
     */
    private Handler tempProgressBarHandler = new Handler(), ldrProgressBarHandler = new Handler(), potProgressBarHandler = new Handler();


    /**
     * command state
     */
    private long lightsState = 0, connectionState = 0, accidentState = 0, lockState = 0, startState = 0;


    /**
     * Sensors Value
     */
    private int ldrSensorValue = 0, potSensorValue = 0;
    private int tempSensorValue = 0;
    private int tempSensorInFahrenheit = 0;

    /**
     * flag to determine to the client that the service is connected
     */
    public int onConnectedFlag = 0;

    /**
     * flag to determine to the client that the location is changed
     */
    public int onLocationChangedFlag = 0;

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference connectionStateDatabaseReference, accidentStateDatabaseReference,
            startStateStateDatabaseReference, lightsStateDatabaseReference, lockStateDatabaseReference,
            mappingServicesDatabaseReference, sensorsValuesDatabaseReference, carDatabaseReference;

    private ValueEventListener startStateEventListener, lockStateEventListener, lightsStateEventListener,
            sensorsValueEventListener, accidentStateEventListener, mappingServicesEventListener,
            connectionStateEventListener;

    /**
     * the current position of the car
     */
    double longitude, latitude = 0;

    /**
     * the current position for the user
     */
    double userLongitude, userLatitude;

    /**
     * Thread for handle the progress
     */
    Thread potThread, tempThread, ldrThread;

    boolean fragmentIsRunning = false;


    UserFragment userFragments;

    /**
     * progress bar to load the data from the cloud
     */
    private ProgressDialog progressDialog;

    /**
     * camera settings
     */
    private float zoom = 0, bearing = 0, tilt = 0;

    int userConnected = 0, locationButtonClicked = 0;

    /**
     * Flag to determine which the Temperature in form of C or F
     */
    private boolean isFahrenheit = false;

    int accidentNotificationFlag = 0;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        initializeScreen(rootView);

        //get the current settings for the camera settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //extract  the camera values value
        setMapView(sharedPreferences);

        String currentTempFormatState = sharedPreferences.getString(
                getString(R.string.settings_temp_units_key),
                getString(R.string.settings_temp_units_metric_key));

        isFahrenheit = AdasUtils.isCelsiusOrFahrenheit(currentTempFormatState, getContext());

        userFragments = (UserFragment) getFragmentManager().findFragmentById(R.id.fragment_container);

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //get the current user uid
        User currentUser = AuthenticationUtilities.getCurrentUser(getContext());
        String uid = currentUser.getUserUid();

        //get the references for the childes
        //the main child for the directions services
        getFirebaseObjectReferences(uid);

        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            //show a progress dialog in the BluetoothServerActivity
            //progressDialog = ProgressDialog.show(getContext(),
            //    "Connecting...", "Please wait!!!");
            buildGoogleApiClient();
        }

        //show toast  if there is no internet net connection
        else {
            if (userFragment != null) {

                makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            }
        }

        //setup ans start the threads
        potThread = new Thread(new PotProgressThread());
        tempThread = new Thread(new TemperatureProgressThread());
        ldrThread = new Thread(new LdrProgressThread());


        fragmentIsRunning = true;
        potThread.start();
        tempThread.start();
        ldrThread.start();

        //set the buttons listener
        lightsButton.setOnClickListener(this);
        lockButton.setOnClickListener(this);
        userLocationButton.setOnClickListener(this);
        startButton.setOnClickListener(this);

        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            if (userFragments.isAdded()) {
                actionResolver();
            }
        }

        //setup the map fragment
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.my_location_fragment_user);
        mapFragment.getMapAsync(this);


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //check the internet connection

        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        //check the internet connection

        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        accidentPlace = new LatLng(location.getLatitude(), location.getLongitude());

        //get the current user location
        userLongitude = location.getLongitude();
        userLatitude = location.getLatitude();
        if (userConnected == 1 && locationButtonClicked == 1) {
            DirectionsApiUtilities.AnimateMarker(userMarker, accidentPlace, false, mMap);
        }

    }

    /**
     * show the current location when the api client is connected to the service
     */
    @Override
    public void onConnected(Bundle bundle) {
        location = LocationUtilities.enableMyLocation(getActivity(), mGoogleApiClient);
        if (location != null) {
            //get the longitude and the  latitude from the location object
            userLongitude = location.getLongitude();
            userLatitude = location.getLatitude();

            userConnected = 1;

        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationUtilities.enableUpdateMyLocation(getActivity(), mGoogleApiClient, mLocationRequest, this);
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

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        //stop the threads
        fragmentIsRunning = false;
        potThread.interrupt();
        ldrThread.interrupt();
        tempThread.interrupt();

        //remove google map fragment
        //because it not destroy when switch between fragments
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.my_location_fragment_user);
        if (mapFragment != null) {
            getActivity().getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }

        if (toast != null) {
            toast.cancel();
        }
    }


    /**
     * implement the click of the floating action button click
     */
    @Override
    public void onClick(View v) {
        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {

            switch (v.getId()) {
                case R.id.lights_on_button_user:
                    if (lightsState == 0) {

                        //change the button color to the accent color when it's on
                        lightsButton.setBackgroundTintList(ColorStateList.
                                valueOf(getResources().getColor(R.color.off)));


                        //send the state of the lights to the firebase
                        lightsState = 1;
                        lightsStateDatabaseReference.setValue(lightsState);

                    } else if (lightsState == 1) {
                        //change the button color to the accent color when it's on
                        lightsButton.setBackgroundTintList(ColorStateList.
                                valueOf(getResources().getColor(R.color.colorPrimary)));

                        //send the state of the lights to the firebase
                        lightsState = 0;
                        lightsStateDatabaseReference.setValue(lightsState);
                    }
                    break;
                case R.id.lock_user_button:
                    if (lockState == 0) {
                        //change the button color to the accent color when it's on
                        lockButton.setBackgroundTintList(ColorStateList.
                                valueOf(getResources().getColor(R.color.off)));

                        lockButton.setImageResource(R.drawable.lock_outline);

                        //send the state of the lock to the firebase
                        lockState = 1;
                        lockStateDatabaseReference.setValue(lockState);

                    } else if (lockState == 1) {
                        //change the button color to the accent color when it's on
                        lockButton.setBackgroundTintList(ColorStateList.
                                valueOf(getResources().getColor(R.color.colorPrimary)));

                        lockButton.setImageResource(R.drawable.lock_open_outline);

                        //send the state of the lock to the firebase
                        lockState = 0;
                        lockStateDatabaseReference.setValue(lockState);
                    }
                    break;

                case R.id.start_user_button:
                    if (startState == 0) {
                        //change the button color to the accent color when it's on
                        startButton.setBackgroundTintList(ColorStateList.
                                valueOf(getResources().getColor(R.color.off)));

                        //send the state of the start to the firebase
                        startState = 1;
                        startStateStateDatabaseReference.setValue(startState);
                    } else if (startState == 1) {
                        //change the button color to the accent color when it's on
                        startButton.setBackgroundTintList(ColorStateList.
                                valueOf(getResources().getColor(R.color.colorPrimary)));

                        //send the state of the start to the firebase
                        startState = 0;
                        startStateStateDatabaseReference.setValue(startState);
                    }
                    break;
                case R.id.user_location_button:
                    if (locationButtonClicked == 0) {
                        if (userConnected == 1) {
                            if (userFragments.isAdded()) {

                                //change the button color to the accent color when it's on
                                userLocationButton.setBackgroundTintList(ColorStateList.
                                        valueOf(getResources().getColor(R.color.off)));

                                locationButtonClicked = 1;
                                accidentPlace = new LatLng(userLatitude, userLatitude);

                                cameraPosition = CameraPosition.builder()
                                        .target(new LatLng(userLatitude, userLongitude))
                                        .zoom(zoom)
                                        .bearing(bearing)
                                        .tilt(tilt)
                                        .build();

                                //check for the map state if it's ready start
                                if (mapReady) {

                                    carPlace = new MarkerOptions()
                                            .position(new LatLng(userLatitude, userLongitude))
                                            .title("My Place")
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker));

                                    userMarker = mMap.addMarker(carPlace);
                                    flyTo(cameraPosition);
                                }
                            }
                        }
                    } else if (locationButtonClicked == 1) {
                        if (userFragments.isAdded()) {
                            //change the button color to the accent color when it's on
                            userLocationButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.colorPrimary)));
                            locationButtonClicked = 0;
                            userMarker.remove();
                        }
                    }
                    break;
            }
        }
    }


    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen(View view) {
        tempSensorValueTextView = view.findViewById(R.id.sensor1_value_temp_id_user);
        lDRSensorValueTextView = view.findViewById(R.id.sensor2_value_LDR_id_user);
        potSensorValueTextView = view.findViewById(R.id.sensor3_value_pot_id_user);

        tempProgressBar = view.findViewById(R.id.temp_progress_bar_user);
        ldrProgressBar = view.findViewById(R.id.ldr_progress_bar_user);
        potProgressBar = view.findViewById(R.id.pot_progress_bar_user);

        tempTextView = view.findViewById(R.id.temp_text_view_user);
        ldrTextView = view.findViewById(R.id.LDR_text_view_user);
        potTextView = view.findViewById(R.id.pot_text_view_user);

        userFragment = view.findViewById(R.id.user_fragment);

        startButton = view.findViewById(R.id.start_user_button);
        lockButton = view.findViewById(R.id.lock_user_button);
        lightsButton = view.findViewById(R.id.lights_on_button_user);
        userLocationButton = view.findViewById(R.id.user_location_button);
    }

    /**
     * background thread to show the progress of the pot sensor in the progress bar
     */
    private class PotProgressThread implements Runnable {

        int notificationPotFlag = 0;

        @Override
        public void run() {
            while (potSensorValue <= 1025 && fragmentIsRunning) {

                if (fragmentIsRunning) {

                    // Update the progress bar
                    potProgressBarHandler.post(() -> {
                        potProgressBar.setProgress(potSensorValue);

                        if (userFragments.isAdded()) {
                            if (potSensorValue >= 800) {
                                potProgressBar.setProgressDrawable(getActivity().
                                        getResources().getDrawable(R.drawable.progressbarred));
                                notificationPotFlag++;
                                if (notificationPotFlag == 1) {
                                    NotificationUtils.showWarningNotification(getContext(), getString(R.string.car_waring_high_pot));
                                }
                            } else {
                                potProgressBar.setProgressDrawable(getActivity().
                                        getResources().getDrawable(R.drawable.progressbarblue));
                                notificationPotFlag = 0;
                            }
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * background thread to show the progress of the temp sensor in the progress bar
     */
    private class TemperatureProgressThread implements Runnable {

        int notificationTempFlag = 0;

        @Override
        public void run() {
            while (tempSensorValue <= 200 & fragmentIsRunning) {

                if (fragmentIsRunning) {
                    // Update the progress bar
                    tempProgressBarHandler.post(() -> {
                        tempProgressBar.setProgress(tempSensorValue);
                        if (userFragments.isAdded()) {
                            if (tempSensorValue >= 40) {
                                tempProgressBar.setProgressDrawable(getActivity().
                                        getResources().getDrawable(R.drawable.progressbarred));
                                notificationTempFlag++;
                                if (notificationTempFlag == 1) {
                                    NotificationUtils.showWarningNotification(getContext(), getString(R.string.car_warning_high_temp));
                                }
                            } else {
                                tempProgressBar.setProgressDrawable(getActivity().
                                        getResources().getDrawable(R.drawable.progressbarblue));
                                notificationTempFlag = 0;
                            }
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * background thread to show the progress of the ldr sensor in the progress bar
     */
    private class LdrProgressThread implements Runnable {

        int notificationLdrFlag = 0;

        @Override
        public void run() {

            while (ldrSensorValue <= 1025 && fragmentIsRunning) {

                if (fragmentIsRunning) {
                    // Update the progress bar
                    ldrProgressBarHandler.post(() -> {
                        ldrProgressBar.setProgress(ldrSensorValue);
                        if (userFragments.isAdded()) {
                            if (ldrSensorValue >= 800) {
                                ldrProgressBar.setProgressDrawable(getActivity().
                                        getResources().getDrawable(R.drawable.progressbarred));
                                notificationLdrFlag++;
                                if (notificationLdrFlag == 1) {
                                    NotificationUtils.showWarningNotification(getContext(), getString(R.string.car_warning_high_ldr));
                                }
                            } else {
                                ldrProgressBar.setProgressDrawable(getActivity().
                                        getResources().getDrawable(R.drawable.progressbarblue));
                                notificationLdrFlag = 0;
                            }
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }

        }
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
    public void onConnectionFailed(@Nullable ConnectionResult connectionResult) {
        showToast(getString(R.string.connection_maps_failed));
    }


    /**
     * method to get the data from the firebase and take action based on it
     */
    private void actionResolver() {

        startStateEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startState = (long) dataSnapshot.getValue();
                    if (startState == 1) {
                        if (userFragments.isAdded()) {
                            //change the button color to the accent color when it's on
                            startButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.off)));
                        }
                    } else if (startState == 0) {
                        //change the button color to the accent color when it's on
                        if (userFragments.isAdded()) {
                            startButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.colorPrimary)));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        lockStateEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lockState = (long) dataSnapshot.getValue();
                    if (lockState == 1) {
                        if (userFragments.isAdded()) {
                            //change the button color to the accent color when it's on
                            lockButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.off)));
                        }
                    } else if (lockState == 0) {
                        //change the button color to the accent color when it's on
                        if (userFragments.isAdded()) {
                            lockButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.colorPrimary)));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        lightsStateEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lightsState = (long) dataSnapshot.getValue();

                    if (lightsState == 1) {
                        if (userFragments.isAdded()) {
                            //change the button color to the accent color when it's on
                            lightsButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.off)));
                        }
                    } else if (lightsState == 0) {
                        if (userFragments.isAdded()) {
                            //change the button color to the accent color when it's on
                            lightsButton.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.colorPrimary)));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        sensorsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SensorsValues sensorsValues = dataSnapshot.getValue(SensorsValues.class);

                    //get the data from the firebase
                    tempSensorValue = sensorsValues.getTemperatureSensorValue();

                    //Temperature in Fahrenheit
                    tempSensorInFahrenheit = (int) AdasUtils.celsiusToFahrenheit(tempSensorValue);

                    ldrSensorValue = sensorsValues.getLdrSensorValue();
                    potSensorValue = sensorsValues.getPotSensorValue();

                    refreshUI();
                } else {
                    showToast(getString(R.string.no_car_for_this_user));
                    //progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };


        mappingServicesEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MappingServices mappingServices = dataSnapshot.getValue(MappingServices.class);

                    latitude = mappingServices.getLatitude();
                    longitude = mappingServices.getLongitude();

                    if (onConnectedFlag <= 1) {
                        onConnectedFlag = mappingServices.getOnConnectedFlag();
                    }
                    onLocationChangedFlag = mappingServices.getOnLocationChangedFlag();

                    accidentPlace = new LatLng(latitude, longitude);

                    cameraPosition = CameraPosition.builder()
                            .target(new LatLng(latitude, longitude))
                            .zoom(zoom)
                            .bearing(bearing)
                            .tilt(tilt)
                            .build();

                    //check for the map state if it's ready start
                    if (mapReady) {

                        carPlace = new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("Car Place")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));


                        if (onConnectedFlag == 1) {
                            marker = mMap.addMarker(carPlace);
                            flyTo(cameraPosition);

                        }

                        if (onLocationChangedFlag == 1) {
                            DirectionsApiUtilities.AnimateMarker(marker, accidentPlace, false, mMap);
                            cameraPosition = new CameraPosition.Builder()
                                    .target(accidentPlace).zoom(zoom).bearing(bearing).tilt(tilt).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                        if (onConnectedFlag < 5) {
                            onConnectedFlag++;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        accidentStateEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Circle circle = null;
                    accidentState = (long) dataSnapshot.getValue();
                    if (accidentState == 1) {
                        accidentNotificationFlag++;
                        if (accidentNotificationFlag == 1) {
                            showAccidentHappenDialog();
                            circle = mMap.addCircle(new CircleOptions()
                                    .center(accidentPlace)
                                    .radius(50)
                                    .strokeColor(getResources().getColor((R.color.red)))
                                    .fillColor(Color.argb(64, 255, 0, 0)));
                            //NotificationUtils.showAccidentNotification(getContext());
                        }

                    } else if (accidentState == 0) {
                        accidentNotificationFlag = 0;
                        if (circle != null) {
                            circle.remove();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        connectionStateEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    connectionState = (long) dataSnapshot.getValue();
                    if (connectionState == 0) {
                        if (userFragments.isAdded()) {
                            makeText(getContext(), R.string.car_not_connected, Toast.LENGTH_LONG).show();
                        }
                    } else if (connectionState == 1) {
                        if (userFragments.isAdded()) {
                            makeText(getContext(), R.string.car_is_connected, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //add a listener to the reference
        mappingServicesDatabaseReference.addValueEventListener(mappingServicesEventListener);
        accidentStateDatabaseReference.addValueEventListener(accidentStateEventListener);
        sensorsValuesDatabaseReference.addValueEventListener(sensorsValueEventListener);
        startStateStateDatabaseReference.addValueEventListener(startStateEventListener);
        lockStateDatabaseReference.addValueEventListener(lockStateEventListener);
        lightsStateDatabaseReference.addValueEventListener(lightsStateEventListener);
        connectionStateDatabaseReference.addValueEventListener(connectionStateEventListener);

    }

    /**
     * call when the google map is ready
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mMap = googleMap;

        LocationUtilities.enableSetLocation(getActivity(), mMap);

        //check the internet connection
        if (AuthenticationUtilities.isAvailableInternetConnection(getContext())) {
            mGoogleApiClient.connect();
        }
    }


    /**
     * buildGoogleApiClient to access the api
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * helper method to go to specific location
     */
    private void flyTo(CameraPosition target) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    /**
     * methods used to refresh Ui state and organize the data
     */
    public void refreshUI() {

        lDRSensorValueTextView.setText(ldrSensorValue + "");
        if (isFahrenheit) {
            tempSensorValueTextView.setText(tempSensorInFahrenheit + "");
        } else {
            tempSensorValueTextView.setText(tempSensorValue + "");
        }
        potSensorValueTextView.setText(potSensorValue + "");

        if (userFragments.isAdded()) {
            if (tempSensorValue >= 40) {
                tempSensorValueTextView.setTextColor(getResources().getColor(R.color.red));
                tempTextView.setTextColor(getResources().getColor(R.color.red));
            } else {
                tempSensorValueTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                tempTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            if (ldrSensorValue >= 800) {
                lDRSensorValueTextView.setTextColor(getResources().getColor(R.color.red));
                ldrTextView.setTextColor(getResources().getColor(R.color.red));
            } else {
                lDRSensorValueTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                ldrTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            if (potSensorValue >= 800) {
                potSensorValueTextView.setTextColor(getResources().getColor(R.color.red));
                potTextView.setTextColor(getResources().getColor(R.color.red));
            } else {
                potSensorValueTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                potTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        }

    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (userFragments.isAdded()) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    /**
     * Show a dialog that warns the user that an accident happen
     * if press ok the accident state will still 1 and if choose call
     * the
     */
    private void showAccidentHappenDialog(
    ) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.an_accident_happen);
        builder.setPositiveButton(R.string.call_emergency, (dialog, which) ->
                AdasUtils.sendEmergencyToMessages(latitude, longitude, getContext()));

        builder.setNegativeButton(R.string.ok, (dialog, which) -> {
            // User clicked the "Cancel" button, so dismiss the dialog
            // and continue in the BluetoothServerActivity
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Method to get references to firebase objects
     */
    private void getFirebaseObjectReferences(String uid) {
        carDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR);

        //the childes for the direction root
        connectionStateDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_CONNECTION_STATE);

        accidentStateDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_ACCIDENT_STATE);

        startStateStateDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_START_STATE);

        lightsStateDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_LIGHTS_STATE);

        lockStateDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_LOCK_STATE);

        mappingServicesDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_MAPPING_SERVICES);

        sensorsValuesDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_CAR).child(Constant.FIREBASE_SENSORES_VALUES);

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



