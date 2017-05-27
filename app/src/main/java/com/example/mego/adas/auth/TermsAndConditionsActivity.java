package com.example.mego.adas.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mego.adas.R;

/**
 * Activity used to show the app terms and conditions of usage
 */
public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_terms_and_conditions);
    }
}
