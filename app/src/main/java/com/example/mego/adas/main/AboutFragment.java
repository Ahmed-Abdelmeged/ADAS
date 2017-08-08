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


package com.example.mego.adas.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    /**
     * UI Element
     */
    private TextView projectLeaderTextView, androidDeveloperTextView, computerVisionTextVeiw;


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        initializeScreen(rootView);


        //open the facebook account for the team member if the text view is pressed
        projectLeaderTextView.setOnClickListener(v ->
                openFacebookIntent(Constant.FACEBOOK_URI_HUSSAM_MOSTAFA));

        androidDeveloperTextView.setOnClickListener(v ->
                openFacebookIntent(Constant.FACEBOOK_URI_AHMED_ABD_ELMEGED));

        computerVisionTextVeiw.setOnClickListener(v ->
                openFacebookIntent(Constant.FACEBOOK_URI_DOAA_ELSHAZLY));


        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen(View view) {
        projectLeaderTextView = view.findViewById(R.id.project_leader_textView);
        androidDeveloperTextView = view.findViewById(R.id.android_developer_textView);
        computerVisionTextVeiw = view.findViewById(R.id.computer_vision_textView);
    }

    /**
     * open  a web view with giver Uri
     */
    private void openFacebookIntent(String Uri) {
        Intent openFacebookIntent = new Intent(Intent.ACTION_VIEW);
        openFacebookIntent.setData(android.net.Uri.parse(Uri));

        //check for the web intent
        if (openFacebookIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(openFacebookIntent);
        }
    }

}
