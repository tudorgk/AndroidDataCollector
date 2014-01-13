package com.tudordev.androiddatacollector;

import java.util.List;

import com.tudordev.powerusageservice.PowerEvent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationPowerUsageListAdapter extends ArrayAdapter<PowerEvent> {

	Context context;
	List<PowerEvent> powerUsageData;

	public ApplicationPowerUsageListAdapter(Context context, int resource,
			int textViewResourceId, List<PowerEvent> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public ApplicationPowerUsageListAdapter(Context context, int resource,
			List<PowerEvent> objects) {
		super(context, resource, objects);
		this.context = context;
		this.powerUsageData = objects;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d("Adapter", "getView");
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.application_data_cell, parent, false);
		TextView cpuValuelabel = (TextView) rowView.findViewById(R.id.CPUTimeValue);
		TextView gpsValueLabel = (TextView) rowView.findViewById(R.id.GPSTimeValue);
		TextView powerUsageLabel= (TextView) rowView.findViewById(R.id.PowerValue);
		TextView receivedBytesLabel= (TextView) rowView.findViewById(R.id.BytesReceivedValue);
		TextView sentBytesLabel= (TextView) rowView.findViewById(R.id.BytesSentValue);
		
		
		cpuValuelabel.setText(Long.toString(powerUsageData.get(position).getCpuTime()));
		gpsValueLabel.setText(Long.toString(powerUsageData.get(position).getGpsTime()));
		powerUsageLabel.setText(Double.toString(powerUsageData.get(position).getPowerDrain()));
		receivedBytesLabel.setText(Long.toString(powerUsageData.get(position).getTcpBytesReceived()));
		sentBytesLabel.setText(Long.toString(powerUsageData.get(position).getTcpBytesSent()));				
		
		return rowView;
	}



}
