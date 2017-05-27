package com.example.mego.adas.adapter;

/**
 * Created by Mego on 2/21/2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.model.Steps;
import com.example.mego.adas.utils.constant;

import java.util.ArrayList;

/**
 * custom array adapter to view the list of steps
 */
public class StepAdapter extends ArrayAdapter<Steps> {

    /**
     * Required public constructor
     */
    public StepAdapter(Context context, ArrayList<Steps> stepses) {
        super(context, 0, stepses);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listView = convertView;

        //check if the view is created or not if not inflate new one
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.step_list, parent, false);
        }

        //get a instance from the viewHolder class
        ViewHolder holder = new ViewHolder();
        holder.instructionsTextView = (TextView) listView.findViewById(R.id.instructions_textView);
        holder.distanceTextView = (TextView) listView.findViewById(R.id.step_distance_textView);
        holder.directionImage = (ImageView) listView.findViewById(R.id.step_imageView_dir);
        listView.setTag(holder);


        //get the current step to extract DataSend from it
        Steps currentStep = getItem(position);

        //set the current step instructions
        String instructions = Html.fromHtml(currentStep.getHtml_instructions()).toString();
        holder.instructionsTextView.setText(instructions);

        //set the step distance
        String stepDistance = convertDistance(currentStep.getStepDistanceValue(), currentStep.getStepDistanceText());
        holder.distanceTextView.setText(stepDistance);

        //set the direction Photo
        holder.directionImage.setImageResource(getCarDirection(instructions));


        return listView;
    }

    /**
     * convert the distance from meter value to string to show
     */
    private String convertDistance(int distanceValue, String distanceText) {
        String distance;
        if (distanceValue < 1000) {
            distance = distanceValue + " meters";
        } else {
            distance = distanceText.substring(0, distanceText.length() - 2) + "kilometer";
        }

        return distance;
    }

    /**
     * View holder stores each of the component views inside the tag field of the Layout
     */
    static class ViewHolder {
        TextView instructionsTextView;
        TextView distanceTextView;
        ImageView directionImage;
    }

    /**
     * to check the instructions and set  the directions  image based on it
     */
    private int getCarDirection(String instructions) {
        int directionDrawableResourceId;
        if (instructions.contains(constant.DIRECTION_HEAD)) {
            directionDrawableResourceId = R.drawable.ic_dir_head;
        } else if (instructions.contains(constant.DIRECTION_TURN_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_turn_right;
        } else if (instructions.contains(constant.DIRECTION_TURN_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_turn_left;
        } else if (instructions.contains(constant.DIRECTION_SLIGHT_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_slight_right;
        } else if (instructions.contains(constant.DIRECTION_SLIGHT_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_slight_left;
        } else if (instructions.contains(constant.DIRECTION_KEEP_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_keep_right;
        } else if (instructions.contains(constant.DIRECTION_KEEP_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_keep_left;
        } else if (instructions.contains(constant.DIRECTION_MAKE_U_TURN)) {
            directionDrawableResourceId = R.drawable.ic_dir_make_u_turn;
        } else if (instructions.contains(constant.DIRECTION_SHARP_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_sharp_right;
        } else if (instructions.contains(constant.DIRECTION_SHARP_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_sharp_left;
        } else if (instructions.contains(constant.DIRECTION_ROUNDABOUT)) {
            directionDrawableResourceId = R.drawable.ic_dir_roundabout;
        } else if (instructions.contains(constant.DIRECTION_MERGE)) {
            directionDrawableResourceId = R.drawable.ic_dir_merge;
        } else {
            directionDrawableResourceId = R.drawable.ic_dir_head;
        }

        return directionDrawableResourceId;
    }
}
