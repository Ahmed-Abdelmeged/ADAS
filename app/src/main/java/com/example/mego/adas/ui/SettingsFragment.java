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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mego.adas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }


    //setup the fragment settings
    public static class adasPreferenceFragment extends PreferenceFragment implements
            Preference.OnPreferenceChangeListener,
            SharedPreferences.OnSharedPreferenceChangeListener {


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
                        msg("Please select a number between 1 and 21");
                        return false;
                    }
                } catch (NumberFormatException nfe) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    msg("Please select a number between 1 and 21");
                    return false;
                }
            } else if (preference.getKey().equals(tiltKey)) {
                try {
                    float tilt = Float.parseFloat(stringValue);
                    if (tilt > 90 || tilt < 0) {
                        msg("Please select a number between 0 and 90");
                        return false;
                    }
                } catch (NumberFormatException nfe) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    msg("Please select a number between 0 and 90");
                    return false;
                }
            } else if (preference.getKey().equals(bearingKey)) {
                try {
                    float bearing = Float.parseFloat(stringValue);
                    if (bearing > 360 || bearing < 0) {
                        msg("Please select a number between 0 and 360");
                        return false;
                    }
                } catch (NumberFormatException nfe) {
                    // If whatever the user entered can't be parsed to a number, show an error
                    msg("Please select a number between 0 and 360");
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
        }

        /**
         * Helper method to call a Toast
         */
        private void msg(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
