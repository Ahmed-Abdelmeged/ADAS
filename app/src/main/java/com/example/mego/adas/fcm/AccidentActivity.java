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


package com.example.mego.adas.fcm;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.api.directions.DirectionsApiClient;
import com.example.mego.adas.api.directions.DirectionsApiInterface;
import com.example.mego.adas.api.directions.model.Direction;
import com.example.mego.adas.application.MainActivity;
import com.example.mego.adas.R;
import com.example.mego.adas.adapter.StepAdapter;
import com.example.mego.adas.api.directions.DirectionsAPIConstants;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.api.directions.model.Step;
import com.example.mego.adas.utils.Constant;
import com.example.mego.adas.api.directions.DirectionsUtilities;
import com.example.mego.adas.utils.LocationUtilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Accident activity open form FCM notification to show user
 * accident location and can go to it using directions API
 */
public class AccidentActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    /**
     * UI Element
     */
    private TextView distanceTextView, durationTextView, destinationTextView;
    private ProgressBar loadingbar;
    private FloatingActionButton directionLocationButton, myLocationButton, accidentLocationButton;
    private ImageView locationImageView;
    private LinearLayout detailView;
    private ImageView stepsListButton;
    private LinearLayout mapView;
    private ListView stepListView;
    private Toast toast;


    /**
     * Tag fro the Log and debug
     */
    private static final String LOG_TAG = AccidentActivity.class.getSimpleName();

    /**
     * Accident location
     */
    private double accidentLongitude;
    private double accidentLatitude;


    /**
     * Array list to hold the step information
     */
    ArrayList<Step> stepsArrayList = new ArrayList<>();

    /**
     * Google Maps Objects
     */
    GoogleApiClient mGoogleApiClient;
    Location location;
    GoogleMap mMap;
    boolean mapReady = false;
    CameraPosition accidentCameraPosition, myLocaitonCameraPosition;
    MarkerOptions carPlaceMarker, accidentLocationMarkerOption;
    LatLng carPlace, accidentPlace;
    Marker myLocationMarker;
    LocationRequest mLocationRequest;

    /**
     * PolyLine that will draw the car way
     */
    Polyline carWayPolyLine;

    /**
     * adapter for step list view
     */
    private StepAdapter mAdapter;

    /**
     * camera settings
     */
    private float zoom = 0, bearing = 0, tilt = 0;

    /**
     * the current position of the car
     */
    double longitude, latitude = 0;

    /**
     * start point is the user current location
     */
    private String startLocation;

    private boolean atAccidentLocation = true;

    private DirectionsApiInterface directionsApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_accident);

        initializeScreen();

        Intent intent = getIntent();
        if (intent != null) {
            accidentLongitude = intent.getDoubleExtra(Constant.FCM_LONGITUDE_EXTRA, 0.0);
            accidentLatitude = intent.getDoubleExtra(Constant.FCM_LATITUDE_EXTRA, 0.0);
        }

        //get the current settings for the camera settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //extract  the camera values value
        setMapView(sharedPreferences);

        //set the button to onClick Listener
        directionLocationButton.setOnClickListener(this);
        myLocationButton.setOnClickListener(this);
        accidentLocationButton.setOnClickListener(this);
        stepsListButton.setOnClickListener(this);

        //set the adapter
        mAdapter = new StepAdapter(AccidentActivity.this, new ArrayList<Step>());
        stepListView.setAdapter(mAdapter);

        if (!AuthenticationUtilities.isAvailableInternetConnection(this)) {
            showToast(getString(R.string.no_internet_connection));
        }

        //initialize google map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.google_map_location_accident_activity);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainIntent = new Intent(AccidentActivity.this, MainActivity.class);
                //clear the application stack (clear all  former the activities)
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(AccidentActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
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
        if (AuthenticationUtilities.isAvailableInternetConnection(this)) {
            buildGoogleApiClient();
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
        }
        LocationUtilities.enableSetLocation(this, mMap);
        if (AuthenticationUtilities.isAvailableInternetConnection(this)) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        location = LocationUtilities.enableMyLocation(this, mGoogleApiClient);

        if (location != null) {
            //get the longitude and the  latitude from the location object
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            startLocation = longitude + "," + latitude;

            carPlaceMarker = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Your location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));

            carPlace = new LatLng(latitude, longitude);

            myLocaitonCameraPosition = new CameraPosition.Builder()
                    .target(carPlace).zoom(zoom).bearing(bearing).tilt(tilt).build();


            //check for the map state if it's ready start
            if (mapReady) {
                myLocationMarker = mMap.addMarker(carPlaceMarker);
                setCurrentAccidentToMap();
            }
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationUtilities.enableUpdateMyLocation(this, mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * method called when the car current location changed
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null && !atAccidentLocation) {
            startLocation = location.getLatitude() + "," + location.getLongitude();

            carPlace = new LatLng(location.getLatitude(), location.getLongitude());

            DirectionsUtilities.AnimateMarker(myLocationMarker, carPlace, false, mMap);
            myLocaitonCameraPosition = new CameraPosition.Builder()
                    .target(carPlace).zoom(zoom).bearing(bearing).tilt(tilt).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myLocaitonCameraPosition));
        }
    }

    /**
     * method triggered when the app need a run time permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LocationUtilities.LOCATION_PERMISSION_REQUEST_CODE: {
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
     * helper method to build google api client
     */
    protected synchronized void buildGoogleApiClient() {
        //Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, this)
                .build();
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
     * Method to set current accident to the map
     */
    private void setCurrentAccidentToMap() {
        if (mapReady && accidentLatitude != 0 && accidentLongitude != 0) {

            accidentLocationMarkerOption = new MarkerOptions()
                    .position(new LatLng(accidentLatitude, accidentLongitude))
                    .title("Accident Place")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker));

            accidentPlace = new LatLng(accidentLatitude, accidentLongitude);

            accidentCameraPosition = CameraPosition.builder()
                    .target(new LatLng(accidentLatitude, accidentLongitude))
                    .zoom(zoom)
                    .bearing(bearing)
                    .tilt(tilt)
                    .build();

            mMap.addCircle(new CircleOptions()
                    .center(accidentPlace)
                    .radius(75)
                    .strokeColor(getResources().getColor(R.color.red))
                    .fillColor(Color.argb(64, 255, 0, 0)));

            mMap.addMarker(accidentLocationMarkerOption);
            flyTo(accidentCameraPosition);
        }
    }

    /**
     * helper method to go to specific location
     */
    private void flyTo(CameraPosition target) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }


    /**
     * Method used to initialize the UI element
     */
    private void initializeScreen() {
        directionLocationButton = (FloatingActionButton) findViewById(R.id.location_directions_fab_accident_activity);
        myLocationButton = (FloatingActionButton) findViewById(R.id.my_location_fab_accident_activity);
        accidentLocationButton = (FloatingActionButton) findViewById(R.id.accident_location_fab_accident_activity);

        detailView = (LinearLayout) findViewById(R.id.detail_view_accident_activity);

        locationImageView = (ImageView) findViewById(R.id.locationImageView_accident_activity);
        stepsListButton = (ImageView) findViewById(R.id.down_page_Image_View_accident_activity);

        loadingbar = (ProgressBar) findViewById(R.id.loading_indicator_progress_accident_activity);

        distanceTextView = (TextView) findViewById(R.id.distance_textView_accident_activity);
        durationTextView = (TextView) findViewById(R.id.duration_textView_accident_activity);
        destinationTextView = (TextView) findViewById(R.id.destination_textView_accident_activity);

        stepListView = (ListView) findViewById(R.id.steps_listView_accident_activity);

        mapView = (LinearLayout) findViewById(R.id.map_view_accident_activity);
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

    @Override
    public void onClick(View v) {
        if (AuthenticationUtilities.isAvailableInternetConnection(this)) {
            switch (v.getId()) {
                case R.id.my_location_fab_accident_activity:
                    atAccidentLocation = false;
                    if (mapReady && mGoogleApiClient.isConnected()) {
                        flyTo(myLocaitonCameraPosition);
                    }
                    break;
                case R.id.accident_location_fab_accident_activity:
                    setCurrentAccidentToMap();
                    atAccidentLocation = true;
                    break;
                case R.id.location_directions_fab_accident_activity:
                    locationImageView.setVisibility(View.INVISIBLE);
                    loadingbar.setVisibility(View.VISIBLE);
                    fetchDirectionsData();
                    break;
                case R.id.down_page_Image_View_accident_activity:
                    //setup the animation
                    Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
                    Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_map);

                    if (mapView.getVisibility() == View.GONE) {
                        myLocationButton.setVisibility(View.GONE);
                        accidentLocationButton.setVisibility(View.GONE);
                        mapView.setVisibility(View.VISIBLE);
                        mapView.startAnimation(slideDown);
                        stepsListButton.setImageResource(R.drawable.ic_up_page);
                    } else {
                        mapView.startAnimation(slideUp);
                        mapView.setVisibility(View.GONE);
                        stepsListButton.setImageResource(R.drawable.ic_down_page);
                        myLocationButton.setVisibility(View.VISIBLE);
                        accidentLocationButton.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        } else {
            loadingbar.setVisibility(View.INVISIBLE);
        }
    }

    private void fetchDirectionsData() {

        mAdapter.clear();
        stepsArrayList.clear();

        if (carWayPolyLine != null) {
            carWayPolyLine.remove();
        }

        directionsApiInterface = DirectionsApiClient.getDirectionApiClient()
                .create(DirectionsApiInterface.class);

        Call<Direction> call = directionsApiInterface.
                getDirections(startLocation, accidentLatitude + "," + accidentLongitude);

        call.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {

                loadingbar.setVisibility(View.INVISIBLE);

                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                if (detailView.getVisibility() == View.INVISIBLE) {
                    detailView.startAnimation(slideUp);
                    detailView.setVisibility(View.VISIBLE);
                }

                if (response.body().getStatus().equals(DirectionsAPIConstants.STATUES_OK)) {
                    distanceTextView.setText(DirectionsUtilities.getLegDistance(response.body()));
                    durationTextView.setText(DirectionsUtilities.getLegDuration(response.body()));
                    drawPolyline(DirectionsUtilities.getOverViewPolyLine(response.body()));
                    destinationTextView.setText(accidentLatitude + "," + accidentLongitude);
                    mAdapter.addAll(DirectionsUtilities.getSteps(response.body()));
                } else {
                    showToast(DirectionsUtilities.checkResponseState(response.body().getStatus()));
                }
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                loadingbar.setVisibility(View.INVISIBLE);
                Log.e(LOG_TAG, t.getLocalizedMessage());
            }
        });
    }
}
