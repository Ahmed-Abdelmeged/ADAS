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
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.utils.Constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Fragment to show single accident details
 */
public class AccidentDetailFragment extends Fragment implements OnMapReadyCallback {

    /**
     * UI Element
     */
    TextView accidentTitleTextView, accidentPositionTextView, accidentDateTextView, accidentTimeTextView;

    /**
     * The google Map elements
     */
    GoogleMap mMap;
    boolean mapReady = false;
    CameraPosition cameraPosition;
    MarkerOptions carPlace;
    LatLng accidentPlace;

    /**
     * the information get from the
     */
    private String accidentTitle = null, accidentTime = null, accidentDate = null;
    double accidentLongitude = 0, accidentLatitude = 0;

    public AccidentDetailFragment() {
        // Required empty public constructor
    }

    /**
     * camera settings
     */
    private float zoom = 0, bearing = 0, tilt = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_accident_detail, container, false);
        initializeScreen(rootView);

        //get the current settings for the camera settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //extract  the camera values value
        setMapView(sharedPreferences);

        accidentTitle = getArguments().getString(Constant.ACCIDENT_TITLE_KEY);
        accidentTime = getArguments().getString(Constant.ACCIDENT_TIME_KEY);
        accidentDate = getArguments().getString(Constant.ACCIDENT_DATE_KEY);
        accidentLatitude = getArguments().getDouble(Constant.ACCIDENT_LATITUDE_KEY);
        accidentLongitude = getArguments().getDouble(Constant.ACCIDENT_LONGITUDE_KEY);


        //setup the map fragment
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.google_map_accident_location);
        mapFragment.getMapAsync(this);

        //check for the received information
        if (accidentTime != null && accidentDate != null && accidentTitle != null) {
            accidentTitleTextView.setText(accidentTitle);
            accidentDateTextView.setText(accidentDate);
            accidentTimeTextView.setText(accidentTime);
            String accidentPosition = "lng: " + accidentLongitude + " ,lat: " + accidentLatitude;
            accidentPositionTextView.setText(accidentPosition);

        }


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapReady = true;
        mMap = googleMap;

        if (mapReady && accidentLatitude != 0 && accidentLongitude != 0) {

            carPlace = new MarkerOptions()
                    .position(new LatLng(accidentLatitude, accidentLongitude))
                    .title("Accident Place")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));

            accidentPlace = new LatLng(accidentLatitude, accidentLongitude);

            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(accidentLatitude, accidentLongitude))
                    .zoom(zoom)
                    .bearing(bearing)
                    .tilt(tilt)
                    .build();

            mMap.addCircle(new CircleOptions()
                    .center(accidentPlace)
                    .radius(50)
                    .strokeColor(getResources().getColor(R.color.red))
                    .fillColor(Color.argb(64, 255, 0, 0)));

            mMap.addMarker(carPlace);
            flyTo(cameraPosition);
        }

    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        //remove google map fragment
        //because it not destroy when switch between fragments
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.google_map_accident_location);
        if (mapFragment != null) {
            getActivity().getFragmentManager().beginTransaction().remove(mapFragment).commit();
        }
    }

    /**
     * Line the UI Element with the UI
     */
    private void initializeScreen(View view) {
        accidentDateTextView = (TextView) view.findViewById(R.id.accident_date_detail_text_view);
        accidentPositionTextView = (TextView) view.findViewById(R.id.accident_position_detail_textView);
        accidentTimeTextView = (TextView) view.findViewById(R.id.accident_time_detail_text_view);
        accidentTitleTextView = (TextView) view.findViewById(R.id.accident_name_detail_textView);
    }

    /**
     * helper method to go to specific location
     */
    private void flyTo(CameraPosition target) {
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
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

