package com.example.mego.adas.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mego.adas.R;
import com.example.mego.adas.adapter.HelpPageViewerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Fragment to show how to use the program and help instructions
 */
public class HelpFragment extends Fragment {


    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        //setup the page viewer and adapter
        HelpPageViewerAdapter adapter = new HelpPageViewerAdapter(getContext());
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.helpViewpager);
        viewPager.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

}
