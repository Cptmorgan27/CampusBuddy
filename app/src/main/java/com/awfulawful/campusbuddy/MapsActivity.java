package com.awfulawful.campusbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

/** This MapsActivity handles the displaying of the map and the polyline used to show walking route.
 * This MapsActivity implements two helper classes LocationProvider, to get devices current location.
 * Also, DirectionFinder to generate a walking route to selected destination.*/
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        DirectionFinderListener,
        LocationProvider.LocationCallback{

    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private static String latString;
    private static String lgnString;
    private LocationProvider mLocationProvider;
    private static String building, destString;

    /** This sets the Map UI */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Creates LocationProvider Object
        mLocationProvider = new LocationProvider(this, this);

        // Call to receive destination location
        getDestination();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Synchronize with Google Maps
        mapFragment.getMapAsync(this);
    }

    /** On activity start, calls the connect() method on the LocationProvider class*/
    @Override
    protected void onStart() {
        super.onStart();
        mLocationProvider.connect();
    }

    /** if the activity is stopper, calls the disconnect() method on the LocationProvider class*/
    @Override
    protected void onStop() {
        super.onStop();
        mLocationProvider.disconnect();
    }

    /** Receives a string for the devices Latitude from the LocationProvider class,
     * and sets it to latString*/
    public void setLatString(String string){
        latString = string;
    }

    /** Receives a string for the devices Longitude from the LocationProvider class,
     * and sets it to lngString*/
    public void setLgnString(String string){
        lgnString = string;
    }

    /** Based on the source of where the activity has been started from.
     *  If the source is BuildingListActivityFragment, assign the values of
     *  destString and building to those sent from BuildingListActivityFragment.
     *  Else, the source is DeptInfoActivity, assign the values of
     *  destString and building to those sent from DeptInfoActivity.*/
    protected void getDestination(){

        // Construct an intent to get value of source from the BuildingListActivity.
        Intent intent = getIntent();
        String source = " "; // To prevent NullPointerException

        // Assign the value from the intent to the String source
        source = intent.getStringExtra(BuildingListActivity.EXTRA_SOURCE)+ source;

        // If MapActivity was started from the BuildingListActivity then this passes
        if (source.equals("from BuildingListActivityFragment ")) {

            // Receive the values from BuildingListActivity, for building name,
            // Latitude and longitude
            building = intent.getStringExtra(BuildingListActivity.EXTRA_BUILDING_NAME);
            destString = intent.getStringExtra(BuildingListActivity.EXTRA_LAT)
                    + ", "
                    + intent.getStringExtra(BuildingListActivity.EXTRA_LNG);
        }

        else// source.equals("from DepartmentInformationActivity")
        {
            // Receive the values from DeptInfoActivity, for building name,
            // Latitude and longitude
            building = intent.getStringExtra(DeptInfoActivity.EXTRA_BUILDING_NAME);

            destString = intent.getStringExtra(BuildingListActivity.EXTRA_LAT)
                    + ", "
                    + intent.getStringExtra(BuildingListActivity.EXTRA_LNG);
        }
    }

    /** Once device has been synchronized with Google Maps API the map is displayed*/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //Flag to so a Hybrid Satellite map
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Flag to display devices location on map
        mMap.setMyLocationEnabled(true);

        /** Creates a one second delay to allow time for the
         * Google PLay service location request to be received and implemented.*/
        new CountDownTimer(1000, 1000){
           public void onFinish(){
               //Call to send Google Directions service request
               sendRequest(destString);
           }
           public void onTick(long millisUntilFinished){}
        }.start();


    }

   /** Parses latString and lngString to format needed for request. If latString and lngString are
    * not set, will set origin to a default, which is from Adam's Library*/
    private void sendRequest(String dest) {
        String origin;

        // Checks to see if location was set
        if(latString!= null && lgnString!=null) {


             origin = latString + ", " + lgnString;
        }
        //sets origin for adams library
        else origin= "41.843122, -71.464682";


        try {
            /** Create and execute the DirectionFinder object that handles
             * logic of requesting direction from Google Directions services.
             * Also, logic of parsing directions into a usable format to draw polyline.*/
            new DirectionFinder(this, origin, dest).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    /** Clears any previous polylines or markers on the map before drawing new polylines or markers*/
    @Override
    public void onDirectionFinderStart() {

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    /** Once DirectionFinder has received and parsed directions, that data is retrieved here.
     * This method will add the start and end markers, and draw the polyline with received data.*/
    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            // Moves camera to startLocation, with given zoom
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 18));

            // Places marker at startLocation
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title("My Location")
                    .position(route.startLocation)));

            // Places marker ar endLocation
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(building)
                    .position(route.endLocation)));

            // Creates a PolylineOptions object
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            // Assgins PolylineOptions to each point of polyline
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            // Draws polyline on map
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}

