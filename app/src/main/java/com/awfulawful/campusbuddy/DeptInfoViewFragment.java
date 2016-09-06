package com.awfulawful.campusbuddy;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/** A simple {@link Fragment} subclass. This class sets the text for each field
 * of the department that was selected in the scrollable department menu
 * (DirectoryActivityFragment).Optionally, it can also generate a navigational
 * route to the building of the selected departmentif the "GPS" button is clicked.
 * Optionally, it can also access the user device's phone-call capability and present
 * the option tocall the selected department if the "Call" button is clicked.*/
public class DeptInfoViewFragment extends Fragment {

    // Required empty public constructor
    public DeptInfoViewFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View fragmentLayout = inflater.inflate(R.layout.fragment_dept_info_view, container, false);

        /* Access the UI text-boxes within our XML layout for the
           department information screen in order to facilitate the
           ability to append information to them.*/
        TextView deptText = (TextView) fragmentLayout.findViewById(R.id.deptName);
        TextView buildingText = (TextView) fragmentLayout.findViewById(R.id.buildingText);
        TextView headText = (TextView) fragmentLayout.findViewById(R.id.headText);
        TextView phoneText = (TextView) fragmentLayout.findViewById(R.id.phoneText);
        TextView officeText = (TextView) fragmentLayout.findViewById(R.id.officeText);
        final TextView noNumberText = (TextView) fragmentLayout.findViewById(R.id.noNumberText);

        // "noNumberText" will only be appended to if no phone number for the selected department is found.
        noNumberText.setText("");

        /** Construct a "GPS" button on the department information screen
         * and generate a navigational route to the selected department
         * if the user clicks the button.*/
        Button buttonImage = (Button) fragmentLayout.findViewById(R.id.gpsButton2);

        assert buttonImage != null;
        buttonImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Construct an intent to get from the department information screen to MapsActivity.
                Intent mapIntent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);

                // Pull the list of buildings from our internal resources.
                String buildingName = getActivity().getIntent().getStringExtra(DirectoryActivity.EXTRA_DEPTBUILDING);
                String[] buildingArray = getResources().getStringArray(R.array.building_array);

                // Copy the contents of buildingArray into an ArrayList in order to access
                // the indexOf method.
                ArrayList<String> buildingList = new ArrayList<String>(Arrays.asList(buildingArray));

                // Get the position in buildingArray that matches the name of the
                // selected building. This building houses the selected department.
                int position = buildingList.indexOf(buildingName);

                // Get the coordinates associated with the building in this position.
                String[] latArray = getResources().getStringArray(R.array.coordinate_array_lat);
                String lat = latArray[position];
                String[] lngArray = getResources().getStringArray(R.array.coordinate_array_lng);
                String lng = lngArray[position];

                // Send the values of latitude, longitude and the name of the building to MapsActivity.
                mapIntent.putExtra(DeptInfoActivity.EXTRA_LAT,lat);
                mapIntent.putExtra(DeptInfoActivity.EXTRA_LNG,lng);
                mapIntent.putExtra(DeptInfoActivity.EXTRA_BUILDING_NAME,buildingName);
                startActivity(mapIntent);
            }
        });

        /** Construct a "Call" button on the department information screen
         * and access the user device's phone-call capability in order to
         * present the option to call the selected department if the button is clicked.*/
        Button callButton = (Button) fragmentLayout.findViewById(R.id.phoneButton);

        assert callButton != null;
        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get the selected department's phone number.
                String phoneNumber = getActivity().getIntent().getExtras().getString(DirectoryActivity.EXTRA_DEPTPHONE);

                /* Determine whether the department has a phone number or not.
                   If the department has a phone number, access the user-device's
                   phone-call capability and present the option to call that department.
                   If the department does not have a phone number, generate an appropriate
                   error message.*/
                if (!Objects.equals(phoneNumber, "N/A")){
                try {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber));
                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(phoneIntent);
                } catch (Exception e) {}
                }
                else {

                    noNumberText.setText("Sorry, We don't have a number for this Department.");
                    int colors[] = {0xff940c0c, 0xffffffff};
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
                    noNumberText.setBackgroundDrawable(gradientDrawable);
                }
            }});

        // Append text to the fields of the selected department to each text-box.
        Intent intent = getActivity().getIntent();

        assert deptText != null;
        deptText.append(" " + intent.getExtras().getString(DirectoryActivity.EXTRA_DEPTNAME));

        assert buildingText != null;
        buildingText.append(" " + intent.getExtras().getString(DirectoryActivity.EXTRA_DEPTBUILDING));


        assert headText != null;
        headText.append(" " + intent.getExtras().getString(DirectoryActivity.EXTRA_DEPTHEAD));


        assert phoneText != null;
        phoneText.append(" " + intent.getExtras().getString(DirectoryActivity.EXTRA_DEPTPHONE));


        assert officeText != null;
        officeText.append(" " + intent.getExtras().getString(DirectoryActivity.EXTRA_DEPTOFFICE));

        // Inflate the layout for this fragment
        return fragmentLayout;


    }

}
