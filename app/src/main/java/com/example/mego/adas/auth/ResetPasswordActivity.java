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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity to reset the user password if he forget him
 */
public class ResetPasswordActivity extends AppCompatActivity {

    /**
     * Tag for the logs
     */
    private static final String LOG_TAG = ResetPasswordActivity.class.getSimpleName();

    /**
     * UI Element
     */
    private Button sendNewPasswordButtton;
    private EditText emailEditText;
    private TextInputLayout emailWrapper;
    private ProgressDialog mProgressDialog;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_reset_password);

        initializeScreen();

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();

        sendNewPasswordButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                AuthenticationUtilities.hideKeyboard(ResetPasswordActivity.this);
                if (AuthenticationUtilities.isAvailableInternetConnection(getApplicationContext())) {
                    resetPassword(email);
                } else {
                    Toast.makeText(ResetPasswordActivity.this, R.string.error_message_failed_sign_in_no_network,
                            Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    /**
     * Helper method to reset the password at this email
     *
     * @param email
     */
    private void resetPassword(String email) {

        //validate the input data
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mFirebaseAuth.sendPasswordResetEmail(email).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showPasswordResetDialog();
                        }
                        hideProgressDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showPasswordResetDialogFailed(e.getLocalizedMessage());
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
        return valid;
    }


    /**
     * Helper method to show progress dialog
     */
    public void showProgressDialog() {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.send_reset_request));
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
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        sendNewPasswordButtton = (Button) findViewById(R.id.reset_password_button_reset_password_activity);

        emailEditText = (EditText) findViewById(R.id.email_editText_reset_password_activity);

        emailWrapper = (TextInputLayout) findViewById(R.id.email_wrapper_reset_password_activity);
    }

    /**
     * show a dialog that till that the reset process is done
     */
    private void showPasswordResetDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        builder.setMessage(R.string.send_reset_request);
        builder.setTitle(R.string.reset_password_label);

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
     * show a dialog that till that the reset process is done
     */
    private void showPasswordResetDialogFailed(String error) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
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

}
