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
