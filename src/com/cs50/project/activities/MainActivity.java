package com.cs50.project.activities;

import java.util.ArrayList;

import com.cs50.project.archeryhelper.R;
import com.cs50.project.adapters.HomeAdapter;
import com.cs50.project.database.MySQLiteHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// button toggle
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.shoot:
			Intent intentS = new Intent(MainActivity.this, ShootActivity.class);
			startActivity(intentS);
			// go to shoot
			break;
		case R.id.record:
			Intent intentR = new Intent(MainActivity.this, RecordActivity.class);
			startActivity(intentR);
			// go to records
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
