package com.tudordev.androiddatacollector;

import java.util.ArrayList;
import java.util.List;

import com.tudordev.powerusageservice.PowerEvent;
import com.tudordev.powerusageservice.PowerUsageScanner;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class ApplicationPowerUsageList extends ListActivity {

	public ApplicationPowerUsageList() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String application = getIntent().getExtras().getString("application");
		Log.d("application", application);
		ArrayList<PowerEvent> data = PowerUsageScanner.getSingletonObject().getPowerData().get(application);
		Log.d("application", PowerUsageScanner.getSingletonObject().getPowerData().toString());
		setTitle(application);
		if (data != null) {
			ApplicationPowerUsageListAdapter adapter = new ApplicationPowerUsageListAdapter(getApplicationContext(),R.layout.application_data_cell, data);
			setListAdapter(adapter);		
		}
				
	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(parent, name, context, attrs);
	}

	
	
	
	

}
