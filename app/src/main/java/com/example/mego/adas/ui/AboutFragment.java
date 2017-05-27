package com.example.mego.adas.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.utils.constant;

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
        projectLeaderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookIntent(constant.FACEBOOK_URI_HUSSAM_MOSTAFA);
            }
        });

        androidDeveloperTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookIntent(constant.FACEBOOK_URI_AHMED_ABD_ELMEGED);
            }
        });

        computerVisionTextVeiw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookIntent(constant.FACEBOOK_URI_DOAA_ELSHAZLY);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * Link the layout element from XML to Java
     */
    private void initializeScreen(View view) {
        projectLeaderTextView = (TextView) view.findViewById(R.id.project_leader_textView);
        androidDeveloperTextView = (TextView) view.findViewById(R.id.android_developer_textView);
        computerVisionTextVeiw = (TextView) view.findViewById(R.id.computer_vision_textView);
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
