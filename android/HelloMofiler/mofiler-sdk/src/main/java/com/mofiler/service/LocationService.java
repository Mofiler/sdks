package com.mofiler.service;

import org.json.JSONObject;

import android.location.Location;

/**
 * Retrieves location of the user
 *
 * @author icorbalan
 */
public interface LocationService {

    /**
     * Start network provider.
     */
    void startProvider();

    void stopProvider();

    /**
     * Retrieves the last known location.
     *
     * @return The last known location
     */
    Location getLastKnownLocation();

    /**
     * Retrieves the last known location.
     *
     * @return The last known location
     */
    JSONObject getLastKnownLocationJSON();

}
