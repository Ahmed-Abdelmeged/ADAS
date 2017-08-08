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

package com.example.mego.adas.auth;

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
import com.example.mego.adas.user.EditUserPhoneActivity;
import com.example.mego.adas.utils.Constant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class ChangeCurrentNumber extends AppCompatActivity {

    /**
     * UI Element
     */
    private EditText phoneNumberEditText;
    private TextInputLayout phoneNumberWrapper;
    private Button saveNumberPhoneButton;
    private CountryCodePicker countryCodePicker;
    private Toast toast;

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersPhoneNumberDatabaseReference, isPhoneAuthDatabaseReference;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.EditInfoThemeNoActionBar);
        setContentView(R.layout.activity_change_current_number);

        initializeScreen();

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        final User currentAuthUser = AuthenticationUtilities.getCurrentUser(ChangeCurrentNumber.this);
        mUsersPhoneNumberDatabaseReference = mFirebaseDatabase.getReference()
                .child(Constant.FIREBASE_USERS).child(currentAuthUser.getUserUid())
                .child(Constant.FIREBASE_USER_PHONE);

        isPhoneAuthDatabaseReference = mFirebaseDatabase.getReference().child(Constant.FIREBASE_USERS)
                .child(currentAuthUser.getUserUid()).child(Constant.FIREBASE_IS_VERIFIED_PHONE);

        if (AuthenticationUtilities.isAvailableInternetConnection(ChangeCurrentNumber.this)) {
            saveNumberPhoneButton.setOnClickListener(v -> {
                if (validatePhone()) {
                    if (currentUser != null) {
                        String phoneNumber = phoneNumberEditText.getText().toString();

                        mUsersPhoneNumberDatabaseReference.setValue("+" +
                                countryCodePicker.getSelectedCountryCode() + phoneNumber);

                        isPhoneAuthDatabaseReference.setValue(false);

                        AuthenticationUtilities.setCurrentUserPhone(
                                ChangeCurrentNumber.this, phoneNumber);
                        finish();
                    }
                }
            });
        } else {
            showToast(getString(R.string.no_internet_connection));
        }
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        //to show white up button in the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.change_user_phone_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setTitle("");

        phoneNumberEditText = findViewById(R.id.change_number_editText_change_phone_number_activity);

        phoneNumberWrapper = findViewById(R.id.change_number_wrapper_change_phone_number_activity);

        saveNumberPhoneButton = findViewById(R.id.change_new_phone_number_button);

        countryCodePicker = findViewById(R.id.change_phone_number_country_code_picker);
    }

    /**
     * Helper method to validate the data from the edit text
     *
     * @return boolean to indicate phone number validation
     */
    private boolean validatePhone() {
        String phoneNumber = phoneNumberEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || !AuthenticationUtilities.isUserNameValid(phoneNumber)) {
            phoneNumberWrapper.setError(getString(R.string.error_message_required));
            return false;
        } else if (!AuthenticationUtilities.isPhoneNumberValid(phoneNumber)) {
            phoneNumberWrapper.setError(getString(R.string.error_message_valid_number));
            return false;
        } else {
            phoneNumberWrapper.setError(null);
        }
        return true;
    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(ChangeCurrentNumber.this, message, Toast.LENGTH_SHORT);
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
