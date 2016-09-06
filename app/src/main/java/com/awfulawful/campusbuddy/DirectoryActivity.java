package com.awfulawful.campusbuddy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/** This class sets the user-interface layout for the scrollable department menu
 * (DirectoryActivityFragment).Additionally, it assigns identifiers to the values
 * passed from the scrollable department menu tothe department information screen
 * (DeptInfoViewFragment).The identifiers serve to better organize and encapsulate
 * the values being passed.*/
public class DirectoryActivity extends AppCompatActivity {

    public final static String EXTRA_DEPTNAME = "com.awfulawful.campusbuddy.deptname";
    public final static String EXTRA_DEPTBUILDING = "com.awfulawful.campusbuddy.deptbuilding";
    public final static String EXTRA_DEPTHEAD = "com.awfulawful.campusbuddy.depthead";
    public final static String EXTRA_DEPTPHONE = "com.awfulawful.campusbuddy.deptphone";
    public final static String EXTRA_DEPTOFFICE = "com.awfulawful.campusbuddy.deptoffice";

    /** This method sets the user-interface layout for the scrollable department menu.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
