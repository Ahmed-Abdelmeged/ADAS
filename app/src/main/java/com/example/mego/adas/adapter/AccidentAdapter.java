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

package com.example.mego.adas.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.db.entity.Accident;

import java.util.ArrayList;

/**
 * custom array adapter to view the list of accidents
 */
public class AccidentAdapter extends ArrayAdapter<Accident> {

    /**
     * Required public constructor for the the internet connected
     */
    public AccidentAdapter(Context context, ArrayList<Accident> accidents) {
        super(context, 0, accidents);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        //check if the view is created or not if not inflate new one
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.accident_list, parent, false);
        }

        //get a instance from the viewHolder class
        ViewHolder holder = new ViewHolder();
        holder.dateTextView = listView.findViewById(R.id.accident_date_text_view);
        holder.timeTextView = listView.findViewById(R.id.accident_time_text_view);
        holder.accidentTitleTextView = listView.findViewById(R.id.accident_name_textView);
        holder.accidentPositionTextView = listView.findViewById(R.id.accident_position_textView);
        listView.setTag(holder);


        //get the current accident to extract DataSend from it
        Accident currentAccident = getItem(position);

        //set the current accident date
        assert currentAccident != null;
        String date = currentAccident.getDate();
        holder.dateTextView.setText(date);

        //set the current step time
        String time = currentAccident.getTime();
        holder.timeTextView.setText(time);

        //setup the accident title
        String accidentTitle = currentAccident.getAccidentTitle();
        holder.accidentTitleTextView.setText(accidentTitle + " " + (position + 1));

        //setup the accident position
        double longitude = currentAccident.getAccidentLongitude();
        double latitude = currentAccident.getAccidentLatitude();
        String accidentPosition = "lng: " + longitude + " ,lat: " + latitude;
        holder.accidentPositionTextView.setText(accidentPosition);

        return listView;
    }

    /**
     * View holder stores each of the component views inside the tag field of the Layout
     */
    private static class ViewHolder {
        TextView dateTextView;
        TextView timeTextView;
        TextView accidentTitleTextView;
        TextView accidentPositionTextView;
    }

}
