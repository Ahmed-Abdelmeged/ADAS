package com.example.mego.adas.utils;

/**
 * Created by Mego on 2/25/2017.
 */

/**
 * interface to send the connection state from the fragment to the activity ro disconnect
 */
public interface Communicator {

    public void disconnectListener(long connectionState);
}
