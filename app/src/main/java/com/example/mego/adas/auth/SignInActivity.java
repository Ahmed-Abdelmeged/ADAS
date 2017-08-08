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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mego.adas.ui.MainActivity;
import com.example.mego.adas.R;
import com.example.mego.adas.utils.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Activity used for signing in
 */
public class SignInActivity extends AppCompatActivity {

    /**
     * Tag for the logs
     */
    private static final String LOG_TAG = SignInActivity.class.getSimpleName();

    /**
     * UI Element
     */
    private EditText passwordEditText, emailEditText;
    private TextInputLayout passwordWrapper, emailWrapper;
    private TextView forgetPasswordTextView;
    private Button signInButton;
    private ProgressDialog mProgressDialog;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;
    private boolean isPhoneVerified = false;


    /**
     * Firebase objects
     * to specific part of the database
     */
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference isPhoneAuthDatabaseReference;
    private ValueEventListener isPhoneAuthValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_sign_in);

        initializeScreen();

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                AuthenticationUtilities.hideKeyboard(SignInActivity.this);
                if (AuthenticationUtilities.isAvailableInternetConnection(getApplicationContext())) {
                    signIn(email, password);
                } else {
                    Toast.makeText(SignInActivity.this, R.string.error_message_failed_sign_in_no_network,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resetPasswordIntent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(resetPasswordIntent);
            }
        });
    }

    /**
     * To preform the sign in operation
     *
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {

        //validate the input data
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            isPhoneAuthDatabaseReference = mFirebaseDatabase.getReference()
                                    .child(Constant.FIREBASE_USERS)
                                    .child(task.getResult().getUser().getUid())
                                    .child(Constant.FIREBASE_IS_VERIFIED_PHONE);

                            getPhoneVerificationState();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                showErrorDialog(e.getLocalizedMessage());
            }
        });
    }


    /**
     * Helper method to validate the data from the edit text
     *
     * @return
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
        } else {
            passwordWrapper.setError(null);
        }

        return valid;
    }

    /**
     * Helper method to show progress dialog
     */
    public void showProgressDialog() {

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
     * show a dialog that till that an error
     */
    private void showErrorDialog(String error) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setMessage(error);
        builder.setTitle(R.string.error);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Method to get the current phone verification state
     */
    private void getPhoneVerificationState() {
        isPhoneAuthValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isPhoneVerified = dataSnapshot.getValue(Boolean.class);
                    if (!isPhoneVerified) {
                        hideProgressDialog();
                        Intent mainIntent = new Intent(SignInActivity.this, VerifyPhoneNumberActivity.class);
                        //clear the application stack (clear all  former the activities)
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        hideProgressDialog();
                        Intent mainIntent = new Intent(SignInActivity.this, MainActivity.class);
                        //clear the application stack (clear all  former the activities)
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        isPhoneAuthDatabaseReference.addValueEventListener(isPhoneAuthValueEventListener);
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        passwordEditText = (EditText) findViewById(R.id.password_editText_sign_in_activity);
        emailEditText = (EditText) findViewById(R.id.email_editText_sign_in_activity);

        passwordWrapper = (TextInputLayout) findViewById(R.id.password_wrapper_sign_in_activity);
        emailWrapper = (TextInputLayout) findViewById(R.id.email_wrapper_sign_in_activity);

        forgetPasswordTextView = (TextView) findViewById(R.id.forget_password_textView);

        signInButton = (Button) findViewById(R.id.sign_in_Button_sign_in_activity);
    }

}
