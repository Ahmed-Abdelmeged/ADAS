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


package com.example.mego.adas.utils;

/**
 * hold all the program Constant
 */
public class Constant {

    /**
     * key for get the video id key bundle extra
     */
    public static final String VIDEO_KEY = "video";

    /**
     * key for get the video published date key bundle extra
     */
    public static final String PUBLISHED_AT_KEY = "publishedAt";

    /**
     * key for get the video title key bundle extra
     */
    public static final String TITLE_KEY = "title";

    /**
     * key for get the accident  title key bundle extra
     */
    public static final String ACCIDENT_TITLE_KEY = "accident_title";

    /**
     * key for get the accident  longitude key bundle extra
     */
    public static final String ACCIDENT_LONGITUDE_KEY = "accident_longitude";

    /**
     * key for get the accident  latitude key bundle extra
     */
    public static final String ACCIDENT_LATITUDE_KEY = "accident_latitude";

    /**
     * key for get the accident  time key bundle extra
     */
    public static final String ACCIDENT_TIME_KEY = "accident_time";

    /**
     * key for get the accident  date key bundle extra
     */
    public static final String ACCIDENT_DATE_KEY = "accident_date";

    /**
     * Key for get the accident Uri key bundle extra
     */
    public static final String ACCIDENT_URI_KEY = "accident_uri";

    /**
     * Key for get th accident started in online of offline
     */
    public static final String ACCIDENT_START_MODE_KEY = "accident_start_mode";

    /**
     * Constants to determine the starting mode
     */
    public static final String ACCIDENT_MODE_OFFLINE = "accident_offline";
    public static final String ACCIDENT_MODE_ONLINE = "accident_online";

    /**
     * Google Client API Key
     */
    public static final String API_KEY = "AIzaSyBKyxvJIHYfhjsvINFgF3fwvCiViQ5Ie7c";

    /**
     * directions Constant that get from the html instructions
     * to determine the photo that will show in the list
     * and send the car direction vay bluetooth
     */
    public static final String DIRECTION_TURN_RIGHT = "Turn right";
    public static final String DIRECTION_TURN_LEFT = "Turn left";
    public static final String DIRECTION_HEAD = "Head";
    public static final String DIRECTION_SLIGHT_RIGHT = "Slight right";
    public static final String DIRECTION_SLIGHT_LEFT = "Slight right";
    public static final String DIRECTION_KEEP_LEFT = "Keep left";
    public static final String DIRECTION_KEEP_RIGHT = "Keep right";
    public static final String DIRECTION_MAKE_U_TURN = "Make a U-turn";
    public static final String DIRECTION_MERGE = "Merge";
    public static final String DIRECTION_ROUNDABOUT = "roundabout";
    public static final String DIRECTION_SHARP_RIGHT = "Sharp right";
    public static final String DIRECTION_SHARP_LEFT = "Sharp left";

    /**
     * Firebase database reference Constant for the directions
     */
    public static final String FIREBASE_DIRECTIONS = "directions";

    public static final String FIREBASE_START_LOCATION = "startLocation";
    public static final String FIREBASE_GOING_LOCATION = "goingLocation";
    public static final String FIREBASE_CURRENT_LOCATION = "currentLocation";

    public static final String FIREBASE_LEG_DISTANCE_TEXT = "legDistance";
    public static final String FIREBASE_LEG_DURATION_TEXT = "legDuration";

    public static final String FIREBASE_LEG_OVERVIEW_POLYLINE = "legOverViewPolyline";

    public static final String FIREBASE_STEPS = "steps";

    /**
     * Firebase database reference Constant for the videos
     */
    public static final String FIREBASE_LIVE_STREAMING_VIDEO_ID = "liveStreamingVideoID";


    /**
     * Constant to determine if there is a current streaming or not
     */
    public static final String LIVE_STREAMING_NO_LIVE_VIDEO = "no live video";

    /**
     * Firebase database reference Constant for the car
     */
    public static final String FIREBASE_CAR = "car";

    public static final String FIREBASE_CONNECTION_STATE = "connectionState";
    public static final String FIREBASE_ACCIDENT_STATE = "accidentState";
    public static final String FIREBASE_START_STATE = "startState";
    public static final String FIREBASE_LOCK_STATE = "lockState";
    public static final String FIREBASE_LIGHTS_STATE = "lightsState";

    public static final String FIREBASE_MAPPING_SERVICES = "mappingServices";

    public static final String FIREBASE_ACCIDENTS = "accidents";

    public static final String FIREBASE_SENSORES_VALUES = "sensorsValues";

    public static final String FIREBASE_USERS = "users";
    public static final String FIREBASE_USER_INFO = "userInfo";


    /**
     * Facebook Uri for the team
     */
    public static final String FACEBOOK_URI_AHMED_ABD_ELMEGED = "https://www.facebook.com/ven.rto";
    public static final String FACEBOOK_URI_HUSSAM_MOSTAFA = "https://www.facebook.com/hussam.mostafa.1994";
    public static final String FACEBOOK_URI_DOAA_ELSHAZLY = "https://www.facebook.com/doaa.elshazly.12";


    /**
     * Constant for no live video
     */
    public static final String NO_LIVE_VIDEO = "no live video";

    /**
     * Constant for verify phone number
     */
    public static final String FIREBASE_IS_VERIFIED_PHONE = "phoneVerified";
    public static final String FIREBASE_USER_PHONE = "phoneNumber";

    /**
     * Constant for firebase storage user photo
     */
    public static final String FIREBASE_USER_IMAGE = "userImage";

    /**
     * Constants for update current user
     */
    public static final String FIREBASE_USER_LOCATION = "location";
    public static final String FIREBASE_USER_NAME = "fullName";

    /**
     * Key for get the phone number extra when sign up
     */
    public static final String VERIFY_NUMBER_INTENT_EXTRA_KEY = "verify_number_intent_extra";

}
