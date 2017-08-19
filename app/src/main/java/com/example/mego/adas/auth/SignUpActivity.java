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
import com.example.mego.adas.utils.NetworkUtil;
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
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

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
    private String fullName, email, phoneNumber, location;

    @BindView(R.id.terms_conditions_textView_sign_up_activity)
    TextView termsAndConditionTextView;

    @BindView(R.id.sign_up_Button_sign_up_activity)
    Button signUpButton;

    @BindView(R.id.full_name_editText_sign_up_activity)
    EditText fullNameEditText;

    @BindView(R.id.email_editText_sign_up_activity)
    EditText emailEditText;

    @BindView(R.id.password_editText_sign_up_activity)
    EditText passwordEditText;

    @BindView(R.id.phone_number_editText_sign_up_activity)
    EditText phoneNumberEditText;

    @BindView(R.id.location_editText_sign_up_activity)
    EditText locationEditText;

    @BindView(R.id.full_name_wrapper_sign_up_activity)
    TextInputLayout fullNameWrapper;

    @BindView(R.id.email_wrapper_sign_up_activity)
    TextInputLayout emailWrapper;

    @BindView(R.id.password_wrapper_sign_up_activity)
    TextInputLayout passwordWrapper;

    @BindView(R.id.phone_number_wrapper_sign_up_activity)
    TextInputLayout phoneNumberWrapper;

    @BindView(R.id.location_wrapper_sign_up_activity)
    TextInputLayout locationWrapper;

    @BindView(R.id.country_code_picker)
    CountryCodePicker countryCodePicker;

    private ProgressDialog mProgressDialog;

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

    private DisposableSubscriber<Boolean> disposableObserver = null;
    private Flowable<CharSequence> nameChangeObservable;
    private Flowable<CharSequence> emailChangeObservable;
    private Flowable<CharSequence> passwordChangeObservable;
    private Flowable<CharSequence> phoneNumberChangeObservable;
    private Flowable<CharSequence> locationChangeObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        nameChangeObservable = RxTextView.textChanges(fullNameEditText)
                .skip(1).toFlowable(BackpressureStrategy.LATEST);

        emailChangeObservable = RxTextView.textChanges(emailEditText)
                .skip(1).toFlowable(BackpressureStrategy.LATEST);

        passwordChangeObservable = RxTextView.textChanges(passwordEditText)
                .skip(1).toFlowable(BackpressureStrategy.LATEST);

        phoneNumberChangeObservable = RxTextView.textChanges(phoneNumberEditText)
                .skip(1).toFlowable(BackpressureStrategy.LATEST);

        locationChangeObservable = RxTextView.textChanges(locationEditText)
                .skip(1).toFlowable(BackpressureStrategy.LATEST);

        validateFormFields();

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
     */
    private void createAccount(final String email, final String password) {
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
     */
    private void validateFormFields() {
        disposableObserver = new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean formValid) {
                if (formValid) {
                    signUpButton.setEnabled(true);
                } else {
                    signUpButton.setEnabled(false);
                }
            }

            @Override
            public void onError(Throwable t) {
                Timber.e("error sign up validate");
            }

            @Override
            public void onComplete() {
                Timber.e("complete sign up validate");
            }
        };

        Flowable.combineLatest(
                nameChangeObservable,
                emailChangeObservable,
                passwordChangeObservable,
                phoneNumberChangeObservable,
                locationChangeObservable,
                (lName, lEmail, lPassword, lPhoneNumber, lLocation) -> {

                    //Check name
                    boolean nameNotEmpty = !TextUtils.isEmpty(lName);
                    if (!nameNotEmpty) {
                        fullNameWrapper.setError(getString(R.string.error_message_required));
                    } else {
                        fullNameWrapper.setError(null);
                    }

                    //Check email
                    boolean emailNotEmpty = !TextUtils.isEmpty(lEmail);
                    boolean emailValid = AuthenticationUtilities.isEmailValid(lEmail);
                    if (!emailNotEmpty) {
                        emailWrapper.setError(getString(R.string.error_message_required));
                    } else if (!emailValid) {
                        emailWrapper.setError(getString(R.string.error_message_valid_email));
                    } else {
                        emailWrapper.setError(null);
                    }

                    //Check password
                    boolean passwordNotEmpty = !TextUtils.isEmpty(lPassword);
                    boolean passwordValid = AuthenticationUtilities.isPasswordValid(lPassword);
                    if (!passwordNotEmpty) {
                        passwordWrapper.setError(getString(R.string.error_message_required));
                    } else if (!passwordValid) {
                        passwordWrapper.setError(getString(R.string.password_not_strong));
                    } else {
                        passwordWrapper.setError(null);
                    }

                    //Check location
                    boolean locationNotEmpty = !TextUtils.isEmpty(lLocation);
                    if (!locationNotEmpty) {
                        locationWrapper.setError(getString(R.string.error_message_required));
                    } else {
                        locationWrapper.setError(null);
                    }

                    //Check phone number
                    boolean phoneNumberNotEmpty = !TextUtils.isEmpty(lPhoneNumber);
                    boolean phoneNumberValid = AuthenticationUtilities.isPhoneNumberValid(lPhoneNumber);
                    if (!phoneNumberNotEmpty) {
                        phoneNumberWrapper.setError(getString(R.string.error_message_required));
                    } else if (!phoneNumberValid) {
                        phoneNumberWrapper.setError(getString(R.string.error_message_valid_number));
                    } else {
                        phoneNumberWrapper.setError(null);
                    }

                    return emailNotEmpty && emailValid && nameNotEmpty && passwordNotEmpty &&
                            passwordValid && locationNotEmpty && phoneNumberNotEmpty && phoneNumberValid;
                }
        ).subscribe(disposableObserver);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposableObserver.dispose();
    }
}
