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
 * hold all the program Constants
 */
public final class Constants {

    /**
     * Key for get teh video item bundle extra
     */
    public static final String KEY_ITEM_VIDEO = "item_video";

    /**
     * key for get the accident id key bundle extra
     */
    public static final String ACCIDENT_ID_KEY = "accident_id";

    /**
     * Google Client API Key
     */
    public static final String API_KEY = "AIzaSyBKyxvJIHYfhjsvINFgF3fwvCiViQ5Ie7c";

    /**
     * directions Constants that get from the html instructions
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
     * Firebase database reference Constants for the directions
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
     * Firebase database reference Constants for the videos
     */
    public static final String FIREBASE_LIVE_STREAMING_VIDEO_ID = "liveStreamingVideoID";


    /**
     * Constants to determine if there is a current streaming or not
     */
    public static final String LIVE_STREAMING_NO_LIVE_VIDEO = "no live video";

    /**
     * Firebase database reference Constants for the car
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

    public static final String FIREBASE_USER_PLAYLIST_ID = "playlistId";


    /**
     * Facebook Uri for the team
     */
    public static final String FACEBOOK_URI_AHMED_ABD_ELMEGED = "https://www.facebook.com/ven.rto";
    public static final String FACEBOOK_URI_HUSSAM_MOSTAFA = "https://www.facebook.com/hussam.mostafa.1994";
    public static final String FACEBOOK_URI_DOAA_ELSHAZLY = "https://www.facebook.com/doaa.elshazly.12";


    /**
     * Constants for no live video
     */
    public static final String NO_LIVE_VIDEO = "no live video";

    /**
     * Constants for verify phone number
     */
    public static final String FIREBASE_IS_VERIFIED_PHONE = "phoneVerified";
    public static final String FIREBASE_USER_PHONE = "phoneNumber";

    /**
     * Constants for firebase storage user photo
     */
    public static final String FIREBASE_USER_IMAGE = "userImage";

    /**
     * Constants for update current user
     */
    public static final String FIREBASE_USER_LOCATION = "location";
    public static final String FIREBASE_USER_NAME = "fullName";

    /**
     * Constants for user device token to use it for FCM
     */
    public static final String FIREBASE_DEVICE_TOKEN = "devicePushToken";

    /**
     * Key for get the phone number extra when sign up
     */
    public static final String VERIFY_NUMBER_INTENT_EXTRA_KEY = "verify_number_intent_extra";

    /**
     * Keys for get the JSON objects form FCM
     */
    public static final String FCM_LONGITUDE = "longitude";
    public static final String FCM_LATITIDE = "latitude";
    public static final String FCM_TITLE = "title";
    public static final String FCM_SOUND = "sound";
    public static final String FCM_STATE = "state";

    /**
     * Keys for get the longitude and latitude for the accident fcm notification
     */
    public static final String FCM_LONGITUDE_EXTRA = "fcm_longitude_extra";
    public static final String FCM_LATITUDE_EXTRA = "fcm_latitude_extra";

}
