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

package com.example.mego.adas.user;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity to edit user location
 */
public class EditUserLocationActivity extends AppCompatActivity {

    /**
     * Tag for the logs
     */
    private static final String LOG_TAG = EditUserLocationActivity.class.getSimpleName();

    /**
     * UI Element
     */
    private EditText locationEditText;
    private TextInputLayout locationWrapper;
    private Button saveLocationButton;
    private Toast toast;

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersLocationDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.EditInfoThemeNoActionBar);
        setContentView(R.layout.activity_edit_user_location);

        initializeScreen();

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        User currentUser = AuthenticationUtilities.getCurrentUser(EditUserLocationActivity.this);
        mUsersLocationDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS).child(currentUser.getUserUid()).child(Constant.FIREBASE_USER_LOCATION);

        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLocation()) {
                    if (AuthenticationUtilities.isAvailableInternetConnection(EditUserLocationActivity.this)) {

                        String location = locationEditText.getText().toString();
                        mUsersLocationDatabaseReference.setValue(location);
                        AuthenticationUtilities.setCurrentUserLocation(
                                EditUserLocationActivity.this, location);
                        finish();
                    } else {
                        showToast(getString(R.string.no_internet_connection));
                    }
                }
            }
        });
    }

    /**
     * Helper method to validate the data from the edit text
     *
     * @return boolean to indicate location validation
     */
    private boolean validateLocation() {
        String location = locationEditText.getText().toString();
        if (TextUtils.isEmpty(location) || !AuthenticationUtilities.isUserNameValid(location)) {
            locationWrapper.setError(getString(R.string.error_message_required));
            return false;
        } else {
            locationWrapper.setError(null);
        }
        return true;
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        //to show white up button in the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_user_location_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setTitle("");

        locationEditText = (EditText) findViewById(R.id.location_editText_edit_location_activity);

        locationWrapper = (TextInputLayout) findViewById(R.id.location_wrapper_edit_location_activity);

        saveLocationButton = (Button) findViewById(R.id.save_new_location_button);
    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(EditUserLocationActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
        }
    }
}
