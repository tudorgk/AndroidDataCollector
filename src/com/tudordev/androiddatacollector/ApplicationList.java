package com.tudordev.androiddatacollector;

import java.util.List;

import com.tudordev.androiddatacollector.R;
import com.tudordev.powerusageservice.PowerUsageScanner;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ApplicationList extends ListFragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	public ApplicationList() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.application_list,
				container, false);
		 
		//getting the installed app list
		 List<ApplicationInfo> apps = getActivity().getPackageManager().getInstalledApplications(0);
		 //WORKS!
		 //Log.d("applications", apps.toString());
		 
		ApplicationArrayAdapter adapter = new ApplicationArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, apps);
		setListAdapter(adapter);
		
		return rootView;
	}
	
	@Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		ApplicationInfo item = (ApplicationInfo) getListAdapter().getItem(position);
		Log.d("Click", item.toString());
		
		Intent myIntent = new Intent(getActivity().getApplicationContext(), ApplicationPowerUsageList.class);
		myIntent.putExtra("application", item.packageName); //Optional parameters
		getActivity().startActivity(myIntent);
		
	  }

}
