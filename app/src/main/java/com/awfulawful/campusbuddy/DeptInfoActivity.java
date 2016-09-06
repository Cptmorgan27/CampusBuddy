package com.awfulawful.campusbuddy;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**This class sets the user-interface layout for the department information screen.
 * Additionally, it assigns identifiers to the values passed from
 * DeptInfoViewFragment to MapsActivity upon clicking the "GPS" button on the
 * department information screen. The identifiers serve to better organize and
 * encapsulate the values being passed.*/
public class DeptInfoActivity extends AppCompatActivity {

    public final static String EXTRA_BUILDING_NAME = "com.awfulawful.campusbuddy.BUILDING_NAME";
    public final static String EXTRA_LAT = "com.awfulawful.campusbuddy.LAT";
    public final static String EXTRA_LNG = "com.awfulawful.campusbuddy.LNG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dept_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

                createAndAddFragment();
    }
    /** This method helps the above method set the user-interface layout
     * for the department information screen.*/
    private void createAndAddFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DeptInfoViewFragment deptInfoViewFragment = new DeptInfoViewFragment();
        setTitle(R.string.viewDepartment);
        fragmentTransaction.add(R.id.dept_info_container, deptInfoViewFragment, "DEPT_VIEW_FRAGMENT");

        fragmentTransaction.commit();

    }






}
