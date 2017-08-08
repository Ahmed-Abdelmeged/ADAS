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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Class define the accidents contract and date base scheme
 */
public class AccidentsContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private AccidentsContract() {
    }

    /**
     * Name for the content provider
     */
    public static final String CONTENT_AUTHORITY = "com.example.mego.adas";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ACCIDENTS = "accidents";

    public static final class AccidentsEntry implements BaseColumns {

        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ACCIDENTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of accidents.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCIDENTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single accidents.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ACCIDENTS;

        /**
         * Name of database table for accidents
         */
        public static final String TABLE_NAME = "accidents";

        /**
         * Tile of the accident
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_ACCIDENT_ID = "accident_id";

        /**
         * Latitude of the accident
         * <p>
         * Type: REAL
         */
        public static final String COLUMN_ACCIDENT_LATITUDE = "accident_latitude";

        /**
         * Longitude of the accident
         * <p>
         * Type: REAL
         */
        public static final String COLUMN_ACCIDENT_LONGITUDE = "accident_longitude";

        /**
         * Tile of the accident
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_ACCIDENT_TITLE = "accident_title";

        /**
         * Date of the accident
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_ACCIDENT_DATE = "accident_date";

        /**
         * Time of the accident
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_ACCIDENT_TIME = "accident_time";

    }

}
