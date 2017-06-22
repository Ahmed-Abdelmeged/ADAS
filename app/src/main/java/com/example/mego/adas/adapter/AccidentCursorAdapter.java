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
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.mego.adas.R;
import com.example.mego.adas.data.AccidentsContract.AccidentsEntry;

/**
 * Cursor adapter to load the accident data from SQLite database when the there is no internet connection
 */
public class AccidentCursorAdapter extends CursorAdapter {


    /**
     * Constructs a new {@link AccidentCursorAdapter}.
     *
     * @param context the context
     * @param c       The cursor from which to get the data.
     */
    public AccidentCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /*flag*/);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in accident_list.xml
        return LayoutInflater.from(context).inflate(R.layout.accident_list, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView dateTextView = (TextView) view.findViewById(R.id.accident_date_text_view);
        TextView timeTextView = (TextView) view.findViewById(R.id.accident_time_text_view);
        TextView accidentTitleTextView = (TextView) view.findViewById(R.id.accident_name_textView);
        TextView accidentPositionTextView = (TextView) view.findViewById(R.id.accident_position_textView);

        // Find the columns of accident attributes
        int titleColumnIndex = cursor.getColumnIndex(AccidentsEntry.COLUMN_ACCIDENT_TITLE);
        int longitudeColumnIndex = cursor.getColumnIndex(AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE);
        int latitudeColumnIndex = cursor.getColumnIndex(AccidentsEntry.COLUMN_ACCIDENT_LATITUDE);
        int dateColumnIndex = cursor.getColumnIndex(AccidentsEntry.COLUMN_ACCIDENT_DATE);
        int timeColumnIndex = cursor.getColumnIndex(AccidentsEntry.COLUMN_ACCIDENT_TIME);

        // Read the pet attributes from the Cursor for the current accident
        String accidentTitle = cursor.getString(titleColumnIndex);
        double accidentLongitude = cursor.getDouble(longitudeColumnIndex);
        double accidentLatitude = cursor.getDouble(latitudeColumnIndex);
        String accidentDate = cursor.getString(dateColumnIndex);
        String accidentTime = cursor.getString(timeColumnIndex);


        // Update the TextViews with the attributes for the current accident
        accidentTitleTextView.setText(accidentTitle + " " + (cursor.getPosition() + 1));

        String accidentPosition = "lng: " + accidentLongitude + " ,lat: " + accidentLatitude;
        accidentPositionTextView.setText(accidentPosition);

        dateTextView.setText(accidentDate);
        timeTextView.setText(accidentTime);
    }
}
