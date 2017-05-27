package com.example.mego.adas.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.mego.adas.MainActivity;
import com.example.mego.adas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Splash activity and check if the user is authenticate or not
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Firebase Authentication
     */
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean isAuth = false;

    /**
     * Duration of wait
     */
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashNoActionBar);
        setContentView(R.layout.activity_splash);

        isUserAuth();
           /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isAuth) {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent authIntent = new Intent(SplashActivity.this, NotAuthEntryActivity.class);
                    startActivity(authIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    /**
     * Method to check if the user is authenticated or not
     */
    private void isUserAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is already signed in
                    //start the MainActivity
                    isAuth = true;
                } else {
                    //user is not signed in
                    //start the NotAuthEntryActivity
                    isAuth = false;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}
