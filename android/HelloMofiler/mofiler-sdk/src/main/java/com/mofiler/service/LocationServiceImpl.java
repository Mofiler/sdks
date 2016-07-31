package com.mofiler.service;

import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Retrieves location of the user
 *
 * @author Ivan Corbalan
 */
public class LocationServiceImpl implements LocationService {

    private Location lastKnownLocation;
    private LocationManager locationManager;
    private LocationListener locationListenerNetwork = new LocationListener() {

        /*
         * (non-Javadoc)
         * @see android.location.LocationListener#onLocationChanged(android.location.Location)
         */
        @Override
        public void onLocationChanged(Location location) {
            lastKnownLocation = location;
            Log.d("LocattionServiceImpl", "Location changed!");
        }

        /*
         * (non-Javadoc)
         * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        /*
         * (non-Javadoc)
         * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /*
         * (non-Javadoc)
         * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    /**
     * Constructor.
     */
    public LocationServiceImpl(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /* (non-Javadoc)
     * @see com.mofiler.service.LocationService#startProvider()
     */
    @Override
    public void startProvider() {
        //20140630 encapsulating this in a try/catch block just because *SOMETIMES* it will throw this
        //http://stackoverflow.com/questions/20660561/illegalargumentexception-provider-doesnt-exisit-null-on-maps-v1
        try {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void stopProvider() {
        locationManager.removeUpdates(locationListenerNetwork);

    }

    /* (non-Javadoc)
     * @see com.mofiler.service.LocationService#getLastKnownLocation()
     */
    @Override
    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public JSONObject getLastKnownLocationJSON() {
        //build JSON object from latest location
        String strLat = null, strLon = null;

        if (lastKnownLocation != null) {
            strLat = lastKnownLocation.getLatitude() + "";
            strLon = lastKnownLocation.getLongitude() + "";
        } else {
            //default to 0.0
            strLat = "0.0";
            strLon = "0.0";
        }

        JSONObject json = new JSONObject();
        try {
            json.put("latitude", strLat);
            json.put("longitude", strLon);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return json;
    }

}
