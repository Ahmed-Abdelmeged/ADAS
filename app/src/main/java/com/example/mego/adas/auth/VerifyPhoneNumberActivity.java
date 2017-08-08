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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.mego.adas.ui.MainActivity;
import com.example.mego.adas.R;
import com.example.mego.adas.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Activity used for verify user phone number
 */
public class VerifyPhoneNumberActivity extends AppCompatActivity {

    /**
     * UI Element
     */
    private TextView resendTextView, changeNumberTextView, currentPhoneNumber;
    private Button continueVerifyingButton;
    private PinEntryEditText pinCodeEditText;
    private ProgressDialog mProgressDialog;

    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference isPhoneAuthDatabaseReference, phoneNumberDatabaseReference;
    private ValueEventListener phoneNumberValueEventListener;


    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String uid = null;

    /**
     * Firebase phone verification
     */
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mVerificationCallbacks;
    private String userPhoneNumber = null;

    /**
     * Flag
     */
    private static final int INVALID_CODE_FLAG = 34;
    private static final int INVALID_LINKING = 35;

    private String currentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_verify_phone_number);

        initializeScreen();

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        //get the phone number from the sign up activity
        currentNumber = getIntent().getStringExtra(Constant.VERIFY_NUMBER_INTENT_EXTRA_KEY);

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();
        User currentUser = AuthenticationUtilities.getCurrentUser(VerifyPhoneNumberActivity.this);
        currentPhoneNumber.setText(currentUser.getPhoneNumber());
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * Method to start change phone number activity
     */
    private void startChangeCurrentNumber() {
        changeNumberTextView.setOnClickListener(v -> {
            Intent changeCurrentNumber = new Intent(
                    VerifyPhoneNumberActivity.this, ChangeCurrentNumber.class);
            startActivity(changeCurrentNumber);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * Method to track the verification states
     */
    private void verificationStatesCallbacks() {
        mVerificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // the verification complete and set the is phone auth to true
                mVerificationInProgress = false;
                Timber.e("success");
                linkEmailToPhoneNumber(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;
                Timber.e("failed");
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    pinCodeEditText.setText(null);
                    Toast.makeText(VerifyPhoneNumberActivity.this,
                            getString(R.string.invalid_phone_number), Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(VerifyPhoneNumberActivity.this,
                            getString(R.string.unexpected_error_call_the_support), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Timber.e("code send");
                // Save verification ID and resending token
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        startVerificationFlow();
        if (mVerificationInProgress && userPhoneNumber != null) {
            startPhoneNumberVerification(userPhoneNumber);
        }
    }

    /**
     * Method to request that Firebase verify the user's phone number
     */
    private void startPhoneNumberVerification(String phoneNumber) {
        Timber.e("start");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mVerificationCallbacks);        // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    /**
     * Method to resend the code
     */
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        Timber.e("resend");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mVerificationCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    /**
     * Method to get the user phone number
     */
    private void getUserPhoneNumber() {
        phoneNumberValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userPhoneNumber = dataSnapshot.getValue(String.class);
                    if (userPhoneNumber != null) {
                        startPhoneNumberVerification(userPhoneNumber);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        phoneNumberDatabaseReference.addListenerForSingleValueEvent(phoneNumberValueEventListener);
    }

    /**
     * Method to verify phone number with code
     */
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        if (code != null && verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            linkEmailToPhoneNumber(credential);
        }
    }

    /**
     * Method to start verifying
     */
    private void startVerification() {

        pinCodeEditText.setOnPinEnteredListener(str -> {
            if (str.length() == 6) {
                continueVerifyingButton.setEnabled(true);
            } else {
                continueVerifyingButton.setEnabled(false);
            }
        });

        continueVerifyingButton.setOnClickListener(v -> {
            showProgressDialog();
            String code = pinCodeEditText.getText().toString();
            if (code.length() == 6) {
                verifyPhoneNumberWithCode(mVerificationId, code);
            } else {
                hideProgressDialog();
            }
        });
    }

    /**
     * Method call after the auth done and start the app
     */
    private void startApp() {
        isPhoneAuthDatabaseReference.setValue(true);
        //start the main activity
        Intent mainIntent = new Intent(VerifyPhoneNumberActivity.this, MainActivity.class);
        //clear the application stack (clear all  former the activities)
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    /**
     * Method to link the phone number to email
     */
    private void linkEmailToPhoneNumber(final PhoneAuthCredential credential) {
        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Timber.e("linking");
                        hideProgressDialog();
                        startApp();
                    }
                }).addOnFailureListener(e -> {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                hideProgressDialog();
                showErrorDialog("Invalid code.", INVALID_CODE_FLAG);
                pinCodeEditText.setText(null);
            } else {
                hideProgressDialog();
                showErrorDialog(e.getLocalizedMessage(), INVALID_LINKING);
            }
        });
    }

    /**
     * Method to start verification flow
     */
    private void startVerificationFlow() {
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                logOut();
                Intent mainIntent = new Intent(VerifyPhoneNumberActivity.this, NotAuthEntryActivity.class);
                //clear the application stack (clear all  former the activities)
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            } else {
                uid = firebaseUser.getUid();
                startChangeCurrentNumber();
                if (AuthenticationUtilities.isAvailableInternetConnection(VerifyPhoneNumberActivity.this)) {
                    if (uid != null) {
                        isPhoneAuthDatabaseReference = mFirebaseDatabase.getReference().child(Constant.FIREBASE_USERS)
                                .child(uid).child(Constant.FIREBASE_IS_VERIFIED_PHONE);

                        phoneNumberDatabaseReference = mFirebaseDatabase.getReference().child(Constant.FIREBASE_USERS)
                                .child(uid).child(Constant.FIREBASE_USER_PHONE);

                        verificationStatesCallbacks();
                        if (currentNumber == null) {
                            getUserPhoneNumber();
                        } else {
                            userPhoneNumber = currentNumber;
                            startPhoneNumberVerification(userPhoneNumber);
                        }
                        startVerification();

                        resendTextView.setOnClickListener(v -> {
                            if (userPhoneNumber != null) {
                                resendVerificationCode(userPhoneNumber, mResendToken);
                            }
                        });
                    } else {
                        logOut();
                    }
                } else {
                    Toast.makeText(VerifyPhoneNumberActivity.this, R.string.error_message_failed_sign_in_no_network,
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * Method to sign in with phone number
     */
    /*private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e(LOG_TAG, "sign in");
                            startApp();
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    showErrorDialog("Invalid code.");
                    pinCodeEditText.setText(null);
                } else {
                    showErrorDialog(e.getLocalizedMessage());
                }
            }
        });
    }*/

    /**
     * show a dialog that till that the reset process is done
     */
    private void showErrorDialog(String error, final int closeAppFlag) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(VerifyPhoneNumberActivity.this);
        builder.setMessage(error);
        builder.setTitle(error);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (dialog != null) {
                if (closeAppFlag == INVALID_CODE_FLAG) {
                    dialog.dismiss();
                } else if (closeAppFlag == INVALID_LINKING) {
                    Intent mainIntent = new Intent(VerifyPhoneNumberActivity.this, NotAuthEntryActivity.class);
                    //clear the application stack (clear all  former the activities)
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Helper method to show progress dialog
     */
    public void showProgressDialog() {
        AuthenticationUtilities.hideKeyboard(VerifyPhoneNumberActivity.this);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.sign_in_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Helper method to hide progress dialog
     */
    public void hideProgressDialog() {
        AuthenticationUtilities.hideKeyboard(VerifyPhoneNumberActivity.this);

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
     * Method to log out
     */
    private void logOut() {
        mFirebaseAuth.signOut();
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        resendTextView = findViewById(R.id.resend_code_textView);
        changeNumberTextView = findViewById(R.id.change_phone_number_textView);
        currentPhoneNumber = findViewById(R.id.current_phone_number_textView);

        continueVerifyingButton = findViewById(R.id.continue_verifying_button);

        pinCodeEditText = findViewById(R.id.pin_code_editText);

        continueVerifyingButton.setEnabled(false);
    }
}
