package com.example.mego.adas.adapter;

/**
 * Created by Mego on 2/26/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.model.Accident;

import java.util.ArrayList;

/**
 * custom array adapter to view the list of accidents
 */
public class AccidentAdapter extends ArrayAdapter<Accident> {

    /**
     * Required public constructor
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
        holder.dateTextView = (TextView) listView.findViewById(R.id.accident_date_text_view);
        holder.timeTextView = (TextView) listView.findViewById(R.id.accident_time_text_view);
        holder.accidentTitleTextView = (TextView) listView.findViewById(R.id.accident_name_textView);
        holder.accidentPositionTextView = (TextView) listView.findViewById(R.id.accident_position_textView);
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
    static class ViewHolder {
        TextView dateTextView;
        TextView timeTextView;
        TextView accidentTitleTextView;
        TextView accidentPositionTextView;
    }

}
