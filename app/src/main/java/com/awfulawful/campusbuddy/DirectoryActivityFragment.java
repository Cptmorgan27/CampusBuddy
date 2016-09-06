package com.awfulawful.campusbuddy;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/** This class generates a scrollable department menu for when the user
 * clicks the "Campus Directory" button.  Then, it sends all of the
 * selected department's fields to the department information screen (DeptInfoViewFragment),
 * which uses those fields to display the selected department's information.*/
public class DirectoryActivityFragment extends ListFragment {

    /** This method gets the list of department names from our internal resources
     * in order to generate a scrollable department menu.*/
   @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get the list of departments.
        String[] deptNameArray = getResources().getStringArray(R.array.department_name_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, deptNameArray);
        setListAdapter(adapter);
    }

    /** This method calls the private helper method written below
     * to get all the fields of the selected department.*/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        launchDeptInfoActivity(position);
    }

    /** This method is a helper method for the above method that gets the selected
     * department's name, the building it's located in, the department head, its
     * phone number and its office number.*/
    private void launchDeptInfoActivity(int position){
        // Construct an intent to get from the scrollable department menu
        // to the department information screen (DeptInfoViewFragment).
        Intent intent = new Intent(getActivity(), DeptInfoActivity.class);

        // Pull the selected department's fields from our internal resources.
        String[] deptNameArray = getResources().getStringArray(R.array.department_name_array);
        String[] deptBuildingArray = getResources().getStringArray(R.array.department_building_array);
        String[] deptHeadArray = getResources().getStringArray(R.array.department_head_name_array);
        String[] deptPhoneNumArray = getResources().getStringArray(R.array.department_phone_number_array);
        String[] deptOfficeArray = getResources().getStringArray(R.array.department_office_number_array);

        String deptName = deptNameArray[position];
        String buildingName = deptBuildingArray[position];
        String deptHead = deptHeadArray[position];
        String deptPhoneNum = deptPhoneNumArray[position];
        String deptOffice = deptOfficeArray[position];

        // Send all of the selected department's fields to the
        // department information screen (DeptInfoViewFragment).
        intent.putExtra(DirectoryActivity.EXTRA_DEPTNAME, deptName);
        intent.putExtra(DirectoryActivity.EXTRA_DEPTBUILDING, buildingName);
        intent.putExtra(DirectoryActivity.EXTRA_DEPTHEAD, deptHead);
        intent.putExtra(DirectoryActivity.EXTRA_DEPTPHONE, deptPhoneNum);
        intent.putExtra(DirectoryActivity.EXTRA_DEPTOFFICE, deptOffice);

        startActivity(intent);
    }



}
