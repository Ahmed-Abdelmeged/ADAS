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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.utils.Constants;
import com.example.mego.adas.utils.networking.NetworkUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

/**
 * Activity used for signing in
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * Constants for the saving the values in saved instance
     */
    private static final String USERS = "users";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";


    /**
     * User information
     */
    private String fullName, email, password, phoneNumber, location;


    /**
     * UI Element
     */
    private TextView termsAndConditionTextView;
    private Button signUpButton;
    private EditText fullNameEditText, emailEditText, passwordEditText, phoneNumberEditText, locationEditText;
    private TextInputLayout fullNameWrapper, emailWrapper, passwordWrapper, phoneNumberWrapper, locationWrapper;
    private ProgressDialog mProgressDialog;
    private CountryCodePicker countryCodePicker;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_sign_up);

        initializeScreen();

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            AuthenticationUtilities.hideKeyboard(SignUpActivity.this);
            if (NetworkUtil.isAvailableInternetConnection(getApplicationContext())) {
                createAccount(email, password);
            } else {
                Toast.makeText(SignUpActivity.this, R.string.error_message_failed_sign_in_no_network,
                        Toast.LENGTH_SHORT).show();
            }
        });

        termsAndConditionTextView.setOnClickListener(v -> {
            Intent termsAndConditionsIntent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
            startActivity(termsAndConditionsIntent);
        });
    }


    /**
     * Helper method to make the sign up process
     *
     * @param email
     * @param password
     */
    private void createAccount(final String email, final String password) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {

                }).addOnFailureListener(e -> {
            showErrorDialog(e.getLocalizedMessage());
            hideProgressDialog();
        }).addOnSuccessListener(authResult -> {
            FirebaseUser user = authResult.getUser();
            String uid = user.getUid();
            getUserInfo();
            createUserInFirebaseHelper(uid);
            hideProgressDialog();
            signIn(email, password);
        });
    }

    /**
     * Helper method to create a user in firebase database
     *
     * @param uid the user unique id
     */
    private void createUserInFirebaseHelper(String uid) {
        //Get the current device token
        final String deviceToken = FirebaseInstanceId.getInstance().getToken();

        mUsersDatabaseReference = mFirebaseDatabase.getReference().child(USERS).child(uid);
        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* If there is no user, make one */
                if (dataSnapshot.getValue() == null) {
                    /* Set raw version of date to the ServerValue.TIMESTAMP value and save into dateCreatedMap */
                    HashMap<String, Object> timestampJoined = new HashMap<String, Object>();
                    timestampJoined.put(FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(email, "+" +
                            countryCodePicker.getSelectedCountryCode() + phoneNumber
                            , location, fullName, timestampJoined, false, deviceToken);
                    mUsersDatabaseReference.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Helper Method to sign the user up after creating an account
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Intent mainIntent = new Intent(SignUpActivity.this, VerifyPhoneNumberActivity.class);
                        //clear the application stack (clear all  former the activities)
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mainIntent.putExtra(Constants.VERIFY_NUMBER_INTENT_EXTRA_KEY,
                                "+" + countryCodePicker.getSelectedCountryCode() + phoneNumber);
                        startActivity(mainIntent);
                        finish();
                    }
                }).addOnFailureListener(e -> showErrorDialog(e.getLocalizedMessage()));
    }


    /**
     * Helper method to validate the data from the edit text
     *
     * @return boolean to indicate form validation
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailWrapper.setError(getString(R.string.error_message_required));
            valid = false;
        } else if (!AuthenticationUtilities.isEmailValid(email)) {
            emailWrapper.setError(getString(R.string.error_message_valid_email));
            valid = false;
        } else {
            emailWrapper.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordWrapper.setError(getString(R.string.error_message_required));
            valid = false;
        } else if (!AuthenticationUtilities.isPasswordValid(password)) {
            passwordWrapper.setError(getString(R.string.password_not_strong));
            valid = false;
        } else {
            passwordWrapper.setError(null);
        }

        String fullName = fullNameEditText.getText().toString();
        if (TextUtils.isEmpty(fullName) || !AuthenticationUtilities.isUserNameValid(fullName)) {
            fullNameWrapper.setError(getString(R.string.error_message_required));
            valid = false;
        } else {
            fullNameWrapper.setError(null);
        }

        String location = locationEditText.getText().toString();
        if (TextUtils.isEmpty(location) || !AuthenticationUtilities.isUserNameValid(location)) {
            locationWrapper.setError(getString(R.string.error_message_required));
            valid = false;
        } else {
            locationWrapper.setError(null);
        }

        String phoneNumber = phoneNumberEditText.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || !AuthenticationUtilities.isUserNameValid(phoneNumber)) {
            phoneNumberWrapper.setError(getString(R.string.error_message_required));
            valid = false;
        } else if (!AuthenticationUtilities.isPhoneNumberValid(phoneNumber)) {
            phoneNumberWrapper.setError(getString(R.string.error_message_valid_number));
            valid = false;
        } else {
            phoneNumberWrapper.setError(null);
        }

        return valid;
    }

    /**
     * Helper method to show progress dialog
     */
    public void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.sign_up_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Helper method to get the user information form the edit text
     */
    private void getUserInfo() {
        fullName = fullNameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        phoneNumber = phoneNumberEditText.getText().toString();
        location = locationEditText.getText().toString();
    }

    /**
     * Helper method to hide progress dialog
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    /**
     * show a dialog that till that the reset process is done
     */
    private void showErrorDialog(String error) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(error);
        builder.setTitle(R.string.error);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        termsAndConditionTextView = findViewById(R.id.terms_conditions_textView_sign_up_activity);

        signUpButton = findViewById(R.id.sign_up_Button_sign_up_activity);

        fullNameEditText = findViewById(R.id.full_name_editText_sign_up_activity);
        emailEditText = findViewById(R.id.email_editText_sign_up_activity);
        passwordEditText = findViewById(R.id.password_editText_sign_up_activity);
        phoneNumberEditText = findViewById(R.id.phone_number_editText_sign_up_activity);
        locationEditText = findViewById(R.id.location_editText_sign_up_activity);

        fullNameWrapper = findViewById(R.id.full_name_wrapper_sign_up_activity);
        emailWrapper = findViewById(R.id.email_wrapper_sign_up_activity);
        passwordWrapper = findViewById(R.id.password_wrapper_sign_up_activity);
        phoneNumberWrapper = findViewById(R.id.phone_number_wrapper_sign_up_activity);
        locationWrapper = findViewById(R.id.location_wrapper_sign_up_activity);

        countryCodePicker = findViewById(R.id.country_code_picker);
    }
}
