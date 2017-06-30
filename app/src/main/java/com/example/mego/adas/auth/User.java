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

package com.example.mego.adas.auth;

import java.util.HashMap;

/**
 * Class show the data structure for a user
 */
public class User {

    private String email;
    private String phoneNumber;
    private String location;
    private String fullName;
    private HashMap<String, Object> timestampJoined;
    private String userUid;
    private boolean isPhoneVerified;
    private String devicePushToken;


    /**
     * User constructor for now user
     *
     * @param email
     * @param phoneNumber
     * @param location
     * @param fullName
     */
    public User(String email, String phoneNumber, String location, String fullName
            , HashMap<String, Object> timestampJoined, boolean isPhoneVerified, String devicePushToken) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.fullName = fullName;
        this.timestampJoined = timestampJoined;
        this.isPhoneVerified = isPhoneVerified;
        this.devicePushToken = devicePushToken;
    }

    /**
     * User constructor for verify user
     *
     * @param email
     * @param phoneNumber
     * @param location
     * @param fullName
     * @param userUid
     */
    public User(String email, String phoneNumber, String location, String fullName, String userUid) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.fullName = fullName;
        this.userUid = userUid;
    }

    /**
     * empty constructor
     */
    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public String getFullName() {
        return fullName;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public String getUserUid() {
        return userUid;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getDevicePushToken() {
        return devicePushToken;
    }

    public void setDevicePushToken(String devicePushToken) {
        this.devicePushToken = devicePushToken;
    }

    @Override
    public String toString() {
        return "User: " + userUid + " Name: " + fullName + " Email: " + email +
                " Phone: " + phoneNumber + " Location: " + location;
    }
}
