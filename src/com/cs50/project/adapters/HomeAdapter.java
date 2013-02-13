package com.cs50.project.adapters;

import java.util.ArrayList;

import com.cs50.project.archeryhelper.R;
import android.app.Activity;
import android.widget.ArrayAdapter;

// adapter for records listview
public class HomeAdapter extends ArrayAdapter<String> {
	ArrayList<String> menu;
	
	public HomeAdapter(Activity activity, ArrayList<String> menu) {
		super(activity, R.layout.list_home, R.id.listtext, menu);
		this.menu = menu;
	}
}
