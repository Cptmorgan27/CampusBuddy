package com.awfulawful.campusbuddy;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/** This class handles the device's request to get it's current location
 * from the Google Play service. Upon receiving the devices location,
 * it passes the Latitude & Longitude as String types back to the MapsActivity*/
public class LocationProvider implements GoogleApiClient.ConnectionCallbacks,
        LocationListener {

    /** Creates the Interface definitions */
    public interface LocationCallback {

        void setLatString(String string);
        void setLgnString(String string);
    }
    public static final String TAG = LocationProvider.class.getSimpleName();
    private LocationCallback mLocationCallback;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    /** This constructor contains the code for creating the GoogleAPIClient,
     * a LocationCallback object, and a LocationRequest object*/
    public LocationProvider(Context context, LocationCallback callback) {

       // Crates a Google API client object
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Creates the Location Call back object
        mLocationCallback = callback;

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 5 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

    }

    /** Establishes a connection to Google Play services */
    public void connect() {
        mGoogleApiClient.connect();
    }

    /** Disconnects from Google Play services */
    public void disconnect() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        // Retrieves the devices location from Google Play services, stores in a location variable
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Parses the value of the location's latitude and longitude into two string varibles respectfully
        String lat = String.valueOf(location.getLatitude());
        String lgn = String.valueOf(location.getLongitude());

        // If location did not get assigned try again. Else send the String values back to MapsActivity
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            mLocationCallback.setLatString(lat);
            mLocationCallback.setLgnString(lgn);
        }
    }


    /** Required method from Locationlistener. This would allow instructions if connection
     * to Google Play services was lost temporal. We did not include instructions
     * for this because we only need a connection once for the devices initial location. */
    @Override
    public void onConnectionSuspended(int i) {

    }

    /** Required method from Locationlistener. This would allow instructions upon the
     * location of the devices changes. We did not include instructions
     * for this because we only need the devices initial location. */
    @Override
    public void onLocationChanged(Location location) {

    }



}
