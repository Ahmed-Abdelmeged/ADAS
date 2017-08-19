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

package com.example.mego.adas.user_info;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mego.adas.R;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.utils.NetworkUtil;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity to edit user password
 */
public class EditUserPasswordActivity extends AppCompatActivity {

    /**
     * UI Element
     */
    private EditText currentPasswordEditText, newPasswordEditText, reTypeNewPasswordEditText;
    private TextInputLayout currentPasswordWrapper, newPasswordWrapper, reTypeNewPasswordWrapper;
    private Button updatePasswordButton;
    private ProgressDialog mProgressDialog;
    private Toast toast;

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.EditInfoThemeNoActionBar);
        setContentView(R.layout.activity_edit_user_password);

        initializeScreen();

        //initialize the Firebase auth object
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        updatePasswordButton.setOnClickListener(v -> {
            if (validateForm()) {
                if (NetworkUtil.isAvailableInternetConnection(EditUserPasswordActivity.this)) {
                    if (currentUser != null) {
                        showProgressDialog(getString(R.string.updating_password));
                        updatePassword(currentUser.getEmail(),
                                currentPasswordEditText.getText().toString(),
                                newPasswordEditText.getText().toString());
                    }
                } else {
                    showToast(getString(R.string.no_internet_connection));
                }
            }
        });
    }

    /**
     * Method to update password
     */
    private void updatePassword(String email, String currentPassword, final String newPassword) {
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider.
                getCredential(email, currentPassword);

        // Prompt the user to re-provide their sign-in credentials
        currentUser.reauthenticate(credential)
                .addOnSuccessListener(EditUserPasswordActivity.this, aVoid ->
                        currentUser.updatePassword(newPassword).addOnSuccessListener(aVoid1 -> {
                            hideProgressDialog();
                            showErrorDialog(getString(R.string.update),
                                    getString(R.string.password_updating_successfully));
                            finish();
                        }).addOnFailureListener(e -> {
                            hideProgressDialog();
                            showErrorDialog(getString(R.string.error), e.getLocalizedMessage());
                        })).addOnFailureListener(EditUserPasswordActivity.this, e -> {
            hideProgressDialog();
            showErrorDialog(getString(R.string.error), e.getLocalizedMessage());
        });
    }


    /**
     * Method to validate form
     */
    private boolean validateForm() {
        boolean isValid = true;

        //validate current password
        String currentPassword = currentPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(currentPassword)) {
            currentPasswordWrapper.setError(getString(R.string.error_message_required));
            isValid = false;
        } else if (!AuthenticationUtilities.isPasswordValid(currentPassword)) {
            currentPasswordWrapper.setError(getString(R.string.password_not_strong));
            isValid = false;
        } else {
            currentPasswordWrapper.setError(null);
        }

        //validate new password
        String newPassword = newPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(newPassword)) {
            newPasswordWrapper.setError(getString(R.string.error_message_required));
            isValid = false;
        } else if (!AuthenticationUtilities.isPasswordValid(newPassword)) {
            newPasswordWrapper.setError(getString(R.string.password_not_strong));
            isValid = false;
        } else {
            newPasswordWrapper.setError(null);
        }

        //validate re type new password
        String reTypeNewPassword = reTypeNewPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(reTypeNewPassword)) {
            reTypeNewPasswordWrapper.setError(getString(R.string.error_message_required));
            isValid = false;
        } else if (!AuthenticationUtilities.isPasswordValid(reTypeNewPassword)) {
            reTypeNewPasswordWrapper.setError(getString(R.string.password_not_strong));
            isValid = false;
        } else {
            reTypeNewPasswordWrapper.setError(null);
        }

        //compare the new password to the re new type
        if (!newPassword.equals(reTypeNewPassword)) {
            showErrorDialog(getString(R.string.password_do_not_much),
                    getString(R.string.new_password_do_not_much_details));
            isValid = false;
        }

        return isValid;
    }

    /**
     * show a dialog that till that the reset process is done
     */
    private void showErrorDialog(String title, String error) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserPasswordActivity.this);
        builder.setMessage(error);
        builder.setTitle(title);

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
     * Helper method to hide progress dialog
     */
    public void hideProgressDialog() {
        AuthenticationUtilities.hideKeyboard(EditUserPasswordActivity.this);

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
     * Helper method to show progress dialog
     */
    public void showProgressDialog(String message) {
        AuthenticationUtilities.hideKeyboard(EditUserPasswordActivity.this);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(EditUserPasswordActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
        }
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen() {
        //to show white up button in the activity
        Toolbar toolbar = findViewById(R.id.edit_user_password_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setTitle("");

        currentPasswordEditText = findViewById(R.id.current_password_editText_edit_password_activity);
        newPasswordEditText = findViewById(R.id.new_password_editText_edit_password_activity);
        reTypeNewPasswordEditText = findViewById(R.id.re_type_new_password_editText_edit_password_activity);

        currentPasswordWrapper = findViewById(R.id.current_password_wrapper_edit_password_activity);
        newPasswordWrapper = findViewById(R.id.new_password_wrapper_edit_password_activity);
        reTypeNewPasswordWrapper = findViewById(R.id.re_type_new_password_wrapper_edit_password_activity);

        updatePasswordButton = findViewById(R.id.update_password_button);
    }
}
