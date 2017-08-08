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
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    static DatabaseReference playListIdDatabaseReference;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //get the current user uid
        User currentUser = AuthenticationUtilities.getCurrentUser(getContext());
        String uid = currentUser.getUserUid();

        playListIdDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS)
                .child(uid).child(Constant.FIREBASE_USER_INFO)
                .child(Constant.FIREBASE_USER_PLAYLIST_ID);

        return rootView;
    }


    //setup the fragment settings
    public static class adasPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener,
            SharedPreferences.OnSharedPreferenceChangeListener {

        private Toast toast;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);

            //finding the preferences and bind the summary to each one
            Preference tempPreference = findPreference(getString(R.string.settings_temp_units_key));
            bindPreferenceSummaryToValue(tempPreference);

            Preference cameraZoomPreference = findPreference(getString(R.string.settings_map_zoom_key));
            bindPreferenceSummaryToValue(cameraZoomPreference);

            Preference cameraBearingPreference = findPreference(getString(R.string.settings_map_bearing_key));
            bindPreferenceSummaryToValue(cameraBearingPreference);

            Preference cameraTiltPreference = findPreference(getString(R.string.settings_map_tilt_key));
            bindPreferenceSummaryToValue(cameraTiltPreference);

            Preference playlistIdPreference = findPreference(getString(R.string.settings_playlist_id_key));
            bindPreferenceSummaryToValue(playlistIdPreference);

        }


        /**
         * Check for the setting values before save it
         *
         * @param preference
         * @param newValue
         * @return
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();


            // In this context, we're using the onPreferenceChange listener for checking whether the
            // size setting was set to a valid value.

            // Double check that the preference is the map camera preference
            String zoomKey = getString(R.string.settings_map_zoom_key);
            String tiltKey = getString(R.string.settings_map_tilt_key);
            String bearingKey = getString(R.string.settings_map_bearing_key);

            if (preference.getKey().equals(zoomKey)) {
                try {
                    float zoom = Float.parseFloat(stringValue);
                    if (zoom > 21 || zoom <= 0) {
                        showToast(getString(R.string.select_number_1_21));
                        return false;
                    }
                } catch (NumberFormatException nfe) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    showToast(getString(R.string.select_number_1_21));
                    return false;
                }
            } else if (preference.getKey().equals(tiltKey)) {
                try {
                    float tilt = Float.parseFloat(stringValue);
                    if (tilt > 90 || tilt < 0) {
                        showToast(getString(R.string.select_number_0_90));
                        return false;
                    }
                } catch (NumberFormatException nfe) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    showToast(getString(R.string.select_number_0_90));
                    return false;
                }
            } else if (preference.getKey().equals(bearingKey)) {
                try {
                    float bearing = Float.parseFloat(stringValue);
                    if (bearing > 360 || bearing < 0) {
                        showToast(getString(R.string.select_number_0_360));
                        return false;
                    }
                } catch (NumberFormatException nfe) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    showToast(getString(R.string.select_number_0_360));
                    return false;
                }
            }

            setPreferenceSummary(preference, stringValue);
            return true;
        }


        /**
         * Helper method used to bind the current preference to it's summary
         *
         * @param preference
         */
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Figure out which preference was changed
            Preference preference = findPreference(key);
            if (null != preference) {
                // Updates the summary for the preference
                if (!(preference instanceof CheckBoxPreference)) {
                    String value = sharedPreferences.getString(preference.getKey(), "");
                    setPreferenceSummary(preference, value);

                    /*SettingsFragment.playListIdDatabaseReference.setValue(sharedPreferences.
                            getString(getString(R.string.settings_playlist_id_key),
                                    getString(R.string.settings_playlist_id_default)));*/
                }
            }

        }

        /**
         * Updates the summary for the preference
         *
         * @param preference The preference to be updated
         * @param value      The value that the preference was updated to
         */
        private void setPreferenceSummary(Preference preference, String value) {
            if (preference instanceof ListPreference) {
                // For list preferences, figure out the label of the selected value
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0) {
                    // Set the summary to that label
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else if (preference instanceof EditTextPreference) {
                // For EditTextPreferences, set the summary to the value's simple string representation.
                preference.setSummary(value);
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);

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
            toast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
