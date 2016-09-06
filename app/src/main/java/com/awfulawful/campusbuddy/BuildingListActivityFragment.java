package com.awfulawful.campusbuddy;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/** This class generates a scrollable building menu for when the user
 * clicks the "GPS" button.  Then, it sends the selected building's coordinates
 * to MapsActivity, which uses those coordinates to obtain a navigational route.*/
public class BuildingListActivityFragment extends ListFragment {

    /** This method gets the list of buildings from our internal resources
     * in order to generate a scrollable building menu.*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the list of buildings from "array.xml" and construct an array of buildings.
        String[] buildingArray = getResources().getStringArray(R.array.building_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, buildingArray);
        setListAdapter(adapter);
    }

    /** This method gets the selected building's coordinates and name.
     * It then sends those values to MapsActivity.*/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Pull the selected building's coordinates from our internal resources.
        String[] latArray = getResources().getStringArray(R.array.coordinate_array_lat);
        String lat = latArray[position];
        String[] lngArray = getResources().getStringArray(R.array.coordinate_array_lng);
        String lng = lngArray[position];

        // Pull the selected building's name from our internal resources.
        String[] buildingArray = getResources().getStringArray(R.array.building_array);
        String buildingName = buildingArray[position];

        // Construct an intent to get from the scrollable building menu to MapsActivity.
        Intent mapIntent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);

        // Explicitly tell mapIntent where it's coming from.
        mapIntent.setClass(getActivity().getApplicationContext(), MapsActivity.class);

        // Assign a value to the "source" ID and send it to MapsActivity.
        mapIntent.putExtra(BuildingListActivity.EXTRA_SOURCE, "from BuildingListActivityFragment");

        // Send the values of latitude, longitude and buildingName to MapsActivity.
        mapIntent.putExtra(BuildingListActivity.EXTRA_LAT,lat);
        mapIntent.putExtra(BuildingListActivity.EXTRA_LNG,lng);
        mapIntent.putExtra(BuildingListActivity.EXTRA_BUILDING_NAME,buildingName);

        startActivity(mapIntent);
    }
}

