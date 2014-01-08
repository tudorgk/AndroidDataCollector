package com.example.androiddatacollector;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplicationArrayAdapter extends ArrayAdapter<ApplicationInfo> {

	private  Context context;
	private  ApplicationInfo[] values;
	private List<ApplicationInfo> appList;

	
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
			ApplicationInfo[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	public ApplicationArrayAdapter(Context context, int textViewResourceId,
			List<ApplicationInfo> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.appList = objects;
		
	}

	public ApplicationArrayAdapter(Context context, int resource,
			int textViewResourceId, ApplicationInfo[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}

	public ApplicationArrayAdapter(Context context, int resource,
			int textViewResourceId, List<ApplicationInfo> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.app_list_cell, parent, false);
	    TextView appNameLabel = (TextView) rowView.findViewById(R.id.label);
	    TextView descriptionLabel = (TextView) rowView.findViewById(R.id.secondLine);
	    TextView uidLabel = (TextView) rowView.findViewById(R.id.uid_label);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    
	    
	    appNameLabel.setText(appList.get(position).packageName);
	    descriptionLabel.setText(appList.get(position).loadLabel(this.context.getPackageManager()));
	    uidLabel.setText(Integer.toString(appList.get(position).uid));
	    
	    Drawable icon = this.context.getPackageManager().getApplicationIcon(appList.get(position));
	    imageView.setImageDrawable(icon);
	    // Change the icon for Windows and iPhone
	    //String s = values[position];
//	    if (s.startsWith("iPhone")) {	
//	    	imageView.setImageResource(R.drawable.ic_launcher);
//	    } else {
//	//      imageView.setImageResource(R.drawable.ok);
//	    }

	    return rowView;
	  }
	
	

}
