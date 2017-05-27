package com.example.mego.adas.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mego.adas.R;

/**
 * Entry activity for authentication
 */
public class NotAuthEntryActivity extends AppCompatActivity {

    /**
     * UI Element
     */
    private TextView signInTextView;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.entryActivityStyle);
        setContentView(R.layout.activity_not_auth_entry);

        //link the Ui element to the java
        signInTextView = (TextView) findViewById(R.id.sign_in_textView);
        signUpButton = (Button) findViewById(R.id.sign_up_button);

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the sign in activity
                Intent signInIntent = new Intent(NotAuthEntryActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the sign up activity
                Intent signInIntent = new Intent(NotAuthEntryActivity.this, SignUpActivity.class);
                startActivity(signInIntent);
            }
        });
    }
}
