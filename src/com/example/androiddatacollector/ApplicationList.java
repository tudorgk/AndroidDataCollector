package com.example.androiddatacollector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ApplicationList extends ListFragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	public ApplicationList() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.aplication_list,
				container, false);
		 String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
			        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
			        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
			        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2" };

		 ApplicationArrayAdapter adapter = new ApplicationArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, values);
		 setListAdapter(adapter);
		Log.d("List", "pula");
//		TextView dummyTextView = (TextView) rootView
//				.findViewById(R.id.section_label);
//		dummyTextView.setText("pula");
//		dummyTextView.setText(collector.toString());
//		collector.getRAMInfo();
//		collector.getTotalMemory();
//		collector.getFreeMemory();
//		collector.getBusyMemory();
//		Log.d("battery", collector.BatteryInfo);
//		dummyTextView.setText(collector.BatteryInfo);
		return rootView;
	}

}
