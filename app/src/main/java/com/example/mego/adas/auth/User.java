package com.example.mego.adas.auth;

/**
 * Created by Mego on 4/14/2017.
 */

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


    /**
     * show the user constructor
     *
     * @param email
     * @param phoneNumber
     * @param location
     * @param fullName
     */
    public User(String email, String phoneNumber, String location, String fullName
            , HashMap<String, Object> timestampJoined) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.fullName = fullName;
        this.timestampJoined = timestampJoined;

    }

    /**
     * Required empty constructor
     */
    public User( ) {
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
}
