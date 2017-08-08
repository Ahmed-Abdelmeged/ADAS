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


package com.example.mego.adas.accidents.db;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import timber.log.Timber;

/**
 * {@link ContentProvider} for ADAS app
 */
public class AccidentProvider extends ContentProvider {

    /**
     * URI matcher code for the content URI for the accidents table
     */
    private static final int ACCIDENTS = 100;

    /**
     * URI matcher for the content URI for a single accident
     */
    private static final int ACCIDENT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.mego.adas/accidents" will map to the
        // integer code {@link #ACCIDENTS}. This URI is used to provide access to MULTIPLE rows
        // of the accidents table.
        sUriMatcher.addURI(AccidentsContract.CONTENT_AUTHORITY, AccidentsContract.PATH_ACCIDENTS, ACCIDENTS);

        // The content URI of the form "content://com.example.mego.adas/accidents/#" will map to the
        // integer code {@link #ACCIDENT_ID}. This URI is used to provide access to ONE single row
        // of the accidents table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.mego.adas/accidents/3" matches, but
        // "content://com.example.mego.adas/accidents" (without a number at the end) doesn't match.
        sUriMatcher.addURI(AccidentsContract.CONTENT_AUTHORITY, AccidentsContract.PATH_ACCIDENTS + "/#", ACCIDENT_ID);

    }

    /**
     * Database helper object
     */
    private AccidentsDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AccidentsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //the cursor that will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCIDENTS:
                // For the ACCIDENTS code, query the accidents table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the accidents table.
                cursor = database.query(AccidentsContract.AccidentsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case ACCIDENT_ID:
                // For the ACCIDENT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.mego.adas/accidents/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = AccidentsContract.AccidentsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(AccidentsContract.AccidentsEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCIDENTS:
                return AccidentsContract.AccidentsEntry.CONTENT_LIST_TYPE;
            case ACCIDENT_ID:
                return AccidentsContract.AccidentsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCIDENTS:
                return insertAccident(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a accident into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertAccident(Uri uri, ContentValues values) {
        //Check if the title is not null
        String title = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE);
        if (title == null) {
            throw new IllegalArgumentException("Accident requires a title");
        }

        //Check the time is not null
        String time = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME);
        if (time == null) {
            throw new IllegalArgumentException("Accident requires a time");
        }

        //Check if the date is not null
        String date = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE);
        if (date == null) {
            throw new IllegalArgumentException("Accident requires a date");
        }

        //Check if the accident id is not null
        String accidentId = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID);
        if (accidentId == null) {
            throw new IllegalArgumentException("Accident requires an ID");
        }

        //No need check the longitude and latitude because the default is zero

        //Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Insert a new accident with the given values
        long id = database.insert(AccidentsContract.AccidentsEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Timber.e("Failed to insert row for " + uri);
            return null;
        }

        //Notify the listeners that the data has changed for the accident content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCIDENTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(AccidentsContract.AccidentsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ACCIDENT_ID:
                //Delete a single row given by the ID in the URI
                selection = AccidentsContract.AccidentsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(AccidentsContract.AccidentsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCIDENTS:
                return updateAccident(uri, contentValues, selection, selectionArgs);
            case ACCIDENT_ID:
                // For the ACCIDENT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = AccidentsContract.AccidentsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateAccident(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update accidents in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more accidents).
     * Return the number of rows that were successfully updated.
     */
    private int updateAccident(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //If the {@link AccidentEntry#COLUMN_ACCIDENT_TITLE} key is present
        // check that the title name value is not null
        if (values.containsKey(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE)) {
            String title = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE);
            if (title == null) {
                throw new IllegalArgumentException("Accident requires a title");
            }
        }

        //If the {@link AccidentEntry#COLUMN_ACCIDENT_TIME} key is present
        //Check the time is not null
        if (values.containsKey(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME)) {
            String time = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME);
            if (time == null) {
                throw new IllegalArgumentException("Accident requires a time");
            }
        }

        //If the {@link AccidentEntry#COLUMN_ACCIDENT_DATE} key is present
        if (values.containsKey(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE)) {
            String date = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE);
            if (date == null) {
                throw new IllegalArgumentException("Accident requires a date");
            }
        }

        //If the {@link AccidentEntry#COLUMN_ACCIDENT_ID} key is present
        //Check if the accident id is not null
        if (values.containsKey(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID)) {
            String accidentId = values.getAsString(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID);
            if (accidentId == null) {
                throw new IllegalArgumentException("Accident requires an ID");
            }
        }

        //No need check the longitude and latitude because the default is zero

        //If there are vo values to update , then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(AccidentsContract.AccidentsEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }
}
