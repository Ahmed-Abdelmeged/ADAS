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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mego.adas.R;
import com.example.mego.adas.auth.AuthenticationUtilities;
import com.example.mego.adas.auth.User;
import com.example.mego.adas.utils.AdasUtils;
import com.example.mego.adas.utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.mego.adas.auth.SignUpActivity.USERS;

public class EditUserInfoActivity extends AppCompatActivity {

    /**
     * Tag for the log
     */
    private static final String LOG_TAG = EditUserInfoActivity.class.getSimpleName();

    private static final int RC_PHOTO_PICKER = 6934;

    /**
     * UI Element
     */
    // User item
    @BindView(R.id.edit_user_image)
    ImageView editUserImageImageView;

    @BindView(R.id.edit_user_email)
    TextView editUserEmailTextView;

    @BindView(R.id.edit_user_number)
    TextView editUserPhoneTextView;

    @BindView(R.id.edit_user_name)
    TextView editUserNameTextView;

    @BindView(R.id.edit_user_location)
    TextView editUserLocationTextView;

    @BindView(R.id.loading_image_progress_bar)
    ProgressBar imageUploadingProgressBar;

    @BindView(R.id.upload_progress_text)
    TextView uploadingProgressTextView;

    private Toast toast = null;

    /**
     * Variables to update progress
     */
    private int totalBytes = 0;
    private int bytesTransferred = 0;
    private double downloadingPercentage = 0;

    /**
     * Firebase objects
     */
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mUsersPhotosStorageReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersImageDatabaseReference;
    private ValueEventListener mUserImageValueEventListener;

    private String userImagePath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_edit_user_info);

        ButterKnife.bind(this);
        showCurrentUser();

        //get current user
        User user = AuthenticationUtilities.getCurrentUser(this);

        //get instance for firebase objects
        mFirebaseStorage = FirebaseStorage.getInstance();
        mUsersPhotosStorageReference = mFirebaseStorage.getReference().child(Constant.FIREBASE_USER_IMAGE)
                .child(user.getUserUid());

        //set up the firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUsersImageDatabaseReference = mFirebaseDatabase.getReference().child(USERS)
                .child(user.getUserUid()).child(Constant.FIREBASE_USER_IMAGE);

        //display current user image
        Bitmap userImageBitmap = AdasUtils.loadUserImageFromStorage(
                AdasUtils.getCurrentUserImagePath(EditUserInfoActivity.this));
        if (userImageBitmap != null) {
            editUserImageImageView.setImageBitmap(userImageBitmap);
        } else {
            editUserImageImageView.setImageResource(R.drawable.app_logo);
        }

        if (AdasUtils.getCurrentUserImagePath(EditUserInfoActivity.this) == null) {
            getUserImageUrl();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            //get the choose photo as URI
            final Uri selectedImageUri = data.getData();

            //get reference to store user photo
            StorageReference photoRef =
                    mUsersPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            userImagePath = selectedImageUri.getLastPathSegment();

            photoRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //get the download url and display it
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    mUsersImageDatabaseReference.setValue(downloadUrl.toString());

                    //get the user bitmap and save it in internal storage to offline access
                    //and save bandwidth
                    new DownloadUserImageBitmap().execute(downloadUrl.toString());

                    imageUploadingProgressBar.setVisibility(View.INVISIBLE);
                    uploadingProgressTextView.setVisibility(View.INVISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageUploadingProgressBar.setVisibility(View.INVISIBLE);
                    uploadingProgressTextView.setVisibility(View.INVISIBLE);
                    showToast(getString(R.string.failed_to_upload_photo_try_again));
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    imageUploadingProgressBar.setVisibility(View.VISIBLE);
                    uploadingProgressTextView.setVisibility(View.VISIBLE);

                    //get the progress states and show it
                    totalBytes = (int) taskSnapshot.getTotalByteCount();
                    bytesTransferred = (int) taskSnapshot.getBytesTransferred();

                    downloadingPercentage = ((double) bytesTransferred / (double) totalBytes) * 100;

                    uploadingProgressTextView.setText((int) downloadingPercentage + "%");

                    imageUploadingProgressBar.setMax(totalBytes);
                    imageUploadingProgressBar.setProgress(bytesTransferred);
                }
            });

        }
    }

    @OnClick(R.id.edit_email_container)
    public void emailPressed() {
        showToast(getString(R.string.edit_email_not_change));
    }

    @OnClick(R.id.edit_name_container)
    public void namePressed() {
        Intent editUserNameIntent = new Intent(EditUserInfoActivity.this, EditUserNameActivity.class);
        startActivity(editUserNameIntent);
        overridePendingTransition(R.anim.enter_edit_activity, R.anim.exit_edit_activity);
    }

    @OnClick(R.id.edit_password_container)
    public void passwordPressed() {
        Intent editUserPasswordIntent = new Intent(EditUserInfoActivity.this, EditUserPasswordActivity.class);
        startActivity(editUserPasswordIntent);
        overridePendingTransition(R.anim.enter_edit_activity, R.anim.exit_edit_activity);
    }

    @OnClick(R.id.edit_phone_container)
    public void phonePressed() {
        Intent editUserPhoneIntent = new Intent(EditUserInfoActivity.this, EditUserPhoneActivity.class);
        startActivity(editUserPhoneIntent);
        overridePendingTransition(R.anim.enter_edit_activity, R.anim.exit_edit_activity);
    }

    @OnClick(R.id.edit_location_container)
    public void locationPressed() {
        Intent editUserLocationIntent = new Intent(EditUserInfoActivity.this, EditUserLocationActivity.class);
        startActivity(editUserLocationIntent);
        overridePendingTransition(R.anim.enter_edit_activity, R.anim.exit_edit_activity);
    }

    @OnClick(R.id.edit_user_image)
    public void imagePressed() {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.setType("image/*");
        getImageIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(getImageIntent, "Complete action using"), RC_PHOTO_PICKER);
    }

    /**
     * Method to set the current user information
     */
    private void showCurrentUser() {
        User currentUser = AuthenticationUtilities.getCurrentUser(EditUserInfoActivity.this);
        if (currentUser.getEmail() != null) {
            editUserEmailTextView.setText(currentUser.getEmail());
        }

        if (currentUser.getFullName() != null) {
            editUserNameTextView.setText(currentUser.getFullName());
        }

        if (currentUser.getPhoneNumber() != null) {
            editUserPhoneTextView.setText(currentUser.getPhoneNumber());
        }

        if (currentUser.getLocation() != null) {
            editUserLocationTextView.setText(currentUser.getLocation());
        }
    }

    /**
     * Fast way to call Toast
     */
    private void showToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(EditUserInfoActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * AsyncTask to download image
     */
    private class DownloadUserImageBitmap extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                bitmap = Glide.with(EditUserInfoActivity.this)
                        .load(params[0])
                        .asBitmap()
                        .into(-1, -1)
                        .get();

                if (bitmap != null) {
                    //save the current image
                    String savedPath = AdasUtils.saveImageIntoInternalStorage(bitmap,
                            EditUserInfoActivity.this, userImagePath);

                    AdasUtils.setCurrentUserImagePath(EditUserInfoActivity.this
                            , savedPath + "/" + userImagePath);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //display the image after save it
            editUserImageImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Method to get the user image url
     */
    private void getUserImageUrl() {
        mUserImageValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userImageUrl = dataSnapshot.getValue(String.class);
                if (userImageUrl != null) {
                    for (int i = 0; i < userImageUrl.length(); i++) {
                        if (userImageUrl.charAt(i) == '?') {
                            userImagePath = userImageUrl.substring(i - 6, i);
                        }
                    }
                    new DownloadUserImageBitmap().execute(userImageUrl);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUsersImageDatabaseReference.addValueEventListener(mUserImageValueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
        }
    }
}
