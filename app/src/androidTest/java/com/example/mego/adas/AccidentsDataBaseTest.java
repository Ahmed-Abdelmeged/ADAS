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

package com.example.mego.adas;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.mego.adas.data.AccidentsContract;
import com.example.mego.adas.data.AccidentsDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static com.example.mego.adas.data.AccidentsContract.AccidentsEntry.TABLE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;


/**
 * Instrumentation test, which will execute on an Android device.
 * <p>
 * TO test accidents data base
 */
@RunWith(AndroidJUnit4.class)
public class AccidentsDataBaseTest {

    /**
     * Context used to preform operations on the database ans create AccidentDbHelper
     */
    private final Context mContext = InstrumentationRegistry.getTargetContext();

    /**
     * Class reference to help load the constructor on runtime
     */
    private final Class mDbHelperClass = AccidentsDbHelper.class;

    /**
     * Because we annotate this method with the @Before annotation, this method will be called
     * before every single method with an @Test annotation. We want to start each test clean, so we
     * delete the database to do so.
     */
    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain.
     *
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void create_database_test() throws Exception {

        //Use reflection to try to run the correct constructor whenever implemented
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        //User AccidentDbHelper to get access to a writable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        //Verify the database is open
        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals(databaseIsNotOpen, true, database.isOpen());

        //This Cursor will contain the names of each table in our database
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        TABLE_NAME + "'",
                null);

         /*
         * If tableNameCursor.moveToFirst returns false from this query, it means the database
         * wasn't created properly. In actuality, it means that your database contains no tables.
         */
        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst());

        //If this fails, it means that your database doesn't contain the expected table(s)
        assertEquals("Error: Your database was created without the expected tables.",
                TABLE_NAME, tableNameCursor.getString(0));

        //Always close a cursor when done with it
        tableNameCursor.close();
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * The purpose is to test that the database is working as expected
     *
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void insert_single_record_test() throws Exception {
        // Use reflection to try to run the correct constructor whenever implemented
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        //Use AccidentDbHelper to get access to a writable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // the test accident will be inserted
        ContentValues testValues = new ContentValues();
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LATITUDE, 31.0507734);
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE, 32.532546);
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE, "Accident");
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE, "Feb 29, 2017");
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME, "9:22:13 AM");
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID, "KdwNwrVFSPK2MgKVjKJ");

        //Insert ContentValues into database and get first row ID back
        long firstRowId = database.insert(
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                null,
                testValues);

        //If the insert fails, database.insert returns -1
        assertNotEquals("Unable to insert into the database", -1, firstRowId);

         /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                // Name of table on which to perform the query
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                // Columns; leaving this null returns every column in the table
                null,
                // Optional specification for columns in the "where" clause above
                null,
                // Values for "where" clause
                null,
                // Columns to group by
                null,
                // Columns to filter by row groups
                null,
                // Sort order to return in Cursor
                null);

        //Cursor.moveToFirst will return false if there are no records returned from your query
        String emptyQueryError = "Error: No Records returned from waitlist query";
        assertTrue(emptyQueryError,
                wCursor.moveToFirst());

        //Close cursor and database
        wCursor.close();
        dbHelper.close();
    }

    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     *
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void autoincrement_test() throws Exception {
        // First, let's ensure we have some values in our table initially
        insert_single_record_test();

        // Use reflection to try to run the correct constructor whenever implemented
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        // Use AccidentDbHelper to get access to a writable database
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // the test accident will be inserted
        ContentValues testValues = new ContentValues();
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LATITUDE, 31.0507734);
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE, 31.399434);
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE, "Accident");
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE, "Feb 26, 2017");
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME, "11:46:52 PM");
        testValues.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID, "KdwNwrSfzbAMEDPpn_G");

        //Insert ContentValues into database and get first row ID back
        long firstRowId = database.insert(
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                null,
                testValues);

        // Insert ContentValues into database and get another row ID back
        long secondRowId = database.insert(
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                null,
                testValues);

        assertEquals("ID Autoincrement test failed!",
                firstRowId + 1, secondRowId);
    }

    /**
     * Tests that onUpgrade works by inserting 2 rows then calling onUpgrade and verifies that the
     * database has been successfully dropped and recreated by checking that the database is there
     * but empty
     *
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    public void upgrade_database_test() throws Exception {
        // Insert 2 rows before we upgrade to check that we dropped the database correctly

        // Use reflection to try to run the correct constructor whenever implemented
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        /* Use AccidentDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // the test accidents will be inserted
        ContentValues testValue1 = new ContentValues();
        testValue1.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LATITUDE, 31.0507734);
        testValue1.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE, 31.399434);
        testValue1.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE, "Accident");
        testValue1.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE, "Feb 26, 2017");
        testValue1.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME, "11:46:52 PM");
        testValue1.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID, "KdwNwrSfzbAMEDPpn_G");

        ContentValues testValue2 = new ContentValues();
        testValue2.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LATITUDE, 31.0507734);
        testValue2.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_LONGITUDE, 32.532546);
        testValue2.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TITLE, "Accident");
        testValue2.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_DATE, "Feb 29, 2017");
        testValue2.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_TIME, "9:22:13 AM");
        testValue2.put(AccidentsContract.AccidentsEntry.COLUMN_ACCIDENT_ID, "KdwNwrVFSPK2MgKVjKJ");


        // Insert ContentValues into database and get first row ID back
        long firstRowId = database.insert(
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                null,
                testValue1);

        // Insert ContentValues into database and get another row ID back
        long secondRowId = database.insert(
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                null,
                testValue2);

        dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();

        // This Cursor will contain the names of each table in our database
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        AccidentsContract.AccidentsEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);

        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                // Name of table on which to perform the query
                AccidentsContract.AccidentsEntry.TABLE_NAME,
                // Columns; leaving this null returns every column in the table
                null,
                // Optional specification for columns in the "where" clause above
                null,
                // Values for "where" clause
                null,
                // Columns to group by
                null,
                // Columns to filter by row groups
                null,
                // Sort order to return in Cursor
                null);

        //Cursor.moveToFirst will return false if there are no records returned from your query

        assertFalse("Database doesn't seem to have been dropped successfully when upgrading",
                wCursor.moveToFirst());

        tableNameCursor.close();
        database.close();

    }

    /**
     * Deletes the entire database.
     */
    void deleteTheDatabase() {
        try {
            //Use reflection to get the database name from the db helper class
            Field field = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            field.setAccessible(true);
            mContext.deleteDatabase((String) field.get(null));
        } catch (NoSuchFieldException ex) {
            fail("Make sure you have a member called DATABASE_NAME in the WaitlistDbHelper");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

}
