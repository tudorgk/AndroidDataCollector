package com.example.androiddatacollector;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationArrayAdapter extends ArrayAdapter<String> {

	private  Context context;
	private  String[] values;

	
	public ApplicationArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
	}

	public ApplicationArrayAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		this.context = context;
	}

	public ApplicationArrayAdapter(Context context, int textViewResourceId,
			String[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.values = objects;
	}

	public ApplicationArrayAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	public ApplicationArrayAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}

	public ApplicationArrayAdapter(Context context, int resource,
			int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.app_list_cell, parent, false);
	    //TextView textView = (TextView) rowView.findViewById(R.id.label);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	//    textView.setText(values[position]);
	    // Change the icon for Windows and iPhone
	    String s = values[position];
	    if (s.startsWith("iPhone")) {	
	    	imageView.setImageResource(R.drawable.ic_launcher);
	    } else {
	//      imageView.setImageResource(R.drawable.ok);
	    }

	    return rowView;
	  }
}
