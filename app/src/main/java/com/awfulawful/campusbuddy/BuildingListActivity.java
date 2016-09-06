package com.awfulawful.campusbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/** This class sets the user-interface layout for the scrollable building menu
 * (BuildingListActivityFragment). Additionally, it assigns identifiers to the
 * values passed from the scrollable building menu to MapsActivity. The
 * identifiers serve to better organize and encapsulate the values being passed. */
public class BuildingListActivity extends AppCompatActivity {

    public final static String EXTRA_LAT = "com.awfulawful.campusbuddy.LAT";
    public final static String EXTRA_LNG = "com.awfulawful.campusbuddy.LNG";
    public final static String EXTRA_BUILDING_NAME = "com.awfulawful.campusbuddy.BUILDING_NAME";
    public final static String EXTRA_SOURCE = "com.awfulawful.campusbuddy.SOURCE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

}
