package com.tudordev.powerusageservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PowerUsageScanner extends IntentService {

	public PowerUsageScanner(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent workIntent) {
		// Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        
        // Do work here, based on the contents of dataString
       Log.d("Service reply", dataString);
	}

}
