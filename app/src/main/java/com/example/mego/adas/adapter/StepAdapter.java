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
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.api.directions.model.Step;
import com.example.mego.adas.utils.Constant;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private ArrayList<Step> mSteps;


    public StepAdapter() {
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.step_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        //get the current step to extract DataSend from it
        Step currentStep = mSteps.get(position);

        //set the current step instructions
        String instructions = Html.fromHtml(currentStep.getHtmlInstructions()).toString();
        holder.instructionsTextView.setText(instructions.trim());

        //set the step distance
        String stepDistance = convertDistance(currentStep.getDistance().getValue(),
                currentStep.getDistance().getText());
        holder.distanceTextView.setText(stepDistance.trim());

        //set the direction VideoPhoto
        holder.directionImage.setImageResource(getCarDirection(instructions));
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {
        TextView instructionsTextView;
        TextView distanceTextView;
        ImageView directionImage;

        private StepViewHolder(View view) {
            super(view);
            instructionsTextView = view.findViewById(R.id.instructions_textView);
            distanceTextView = view.findViewById(R.id.step_distance_textView);
            directionImage = view.findViewById(R.id.step_imageView_dir);
        }
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
     * to check the instructions and set  the directions  image based on it
     */
    private int getCarDirection(String instructions) {
        int directionDrawableResourceId;
        if (instructions.contains(Constant.DIRECTION_HEAD)) {
            directionDrawableResourceId = R.drawable.ic_dir_head;
        } else if (instructions.contains(Constant.DIRECTION_TURN_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_turn_right;
        } else if (instructions.contains(Constant.DIRECTION_TURN_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_turn_left;
        } else if (instructions.contains(Constant.DIRECTION_SLIGHT_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_slight_right;
        } else if (instructions.contains(Constant.DIRECTION_SLIGHT_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_slight_left;
        } else if (instructions.contains(Constant.DIRECTION_KEEP_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_keep_right;
        } else if (instructions.contains(Constant.DIRECTION_KEEP_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_keep_left;
        } else if (instructions.contains(Constant.DIRECTION_MAKE_U_TURN)) {
            directionDrawableResourceId = R.drawable.ic_dir_make_u_turn;
        } else if (instructions.contains(Constant.DIRECTION_SHARP_LEFT)) {
            directionDrawableResourceId = R.drawable.ic_dir_sharp_right;
        } else if (instructions.contains(Constant.DIRECTION_SHARP_RIGHT)) {
            directionDrawableResourceId = R.drawable.ic_dir_sharp_left;
        } else if (instructions.contains(Constant.DIRECTION_ROUNDABOUT)) {
            directionDrawableResourceId = R.drawable.ic_dir_roundabout;
        } else if (instructions.contains(Constant.DIRECTION_MERGE)) {
            directionDrawableResourceId = R.drawable.ic_dir_merge;
        } else {
            directionDrawableResourceId = R.drawable.ic_dir_head;
        }

        return directionDrawableResourceId;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.mSteps = steps;
        notifyDataSetChanged();
    }

    public void clear() {
        if (mSteps != null) {
            this.mSteps.clear();
            notifyDataSetChanged();
        }
    }

}
