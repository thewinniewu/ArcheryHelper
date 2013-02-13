package com.cs50.project.activities;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.cs50.project.adapters.HomeAdapter;
import com.cs50.project.archeryhelper.R;
import com.cs50.project.database.MySQLiteHelper;
import com.cs50.project.database.Record;

public class RecordActivity extends Activity {

	// global variables
	private MySQLiteHelper db;
	int[] xSession;
	double[] yAverage;
	int session;
	double maxAverage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_activity);

		// initializing database
		db = new MySQLiteHelper(this);
		db.openDB();

		// get last session (= number of sessions)
		session = db.getLast(1);

		// set listview of sessions
		ListView listview = (ListView) findViewById(R.id.session_list);
		ArrayList<Record> records = db.getAllRecords();

		// x and y arrays for dataset for graph
		xSession = new int[session];
		yAverage = new double[session];

		// loop through records pulled from database and find the total arrows,
		// roundsum and average for each session
		int currentSession = 1;
		int totalArrows = 0;
		int sum = 0;
		maxAverage = 0; // to set y-axis on graph later

		for (int i = 0; i < records.size(); i++) {
			if (records.get(i).getSession() > currentSession
					|| i == records.size() - 1) {
				xSession[currentSession - 1] = currentSession;
				yAverage[currentSession - 1] = (sum * 1.0) / totalArrows;
				if (yAverage[currentSession - 1] > maxAverage) {
					maxAverage = yAverage[currentSession - 1];
				}
				currentSession++;
				sum = 0;
				totalArrows = 0;
			}
			sum += records.get(i).getRoundsum();
			totalArrows += records.get(i).getArrows();
		}

		// set text for listview
		ArrayList<String> sessionStrings = new ArrayList<String>();
		for (int i = 1; i <= session; i++) {
			sessionStrings.add("Session " + i);
		}
		listview.setAdapter(new HomeAdapter(this, sessionStrings));

		// set each row on listview clickable to lead to individual session
		// screens
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int sessionNo = position + 1;

				// includes all records corresponding to that session
				ArrayList<Record> sessionRecords = db
						.getSessionRecords(sessionNo);
				Intent intent = new Intent(view.getContext(),
						SessionActivity.class);
				intent.putParcelableArrayListExtra("rounds", sessionRecords);
				startActivity(intent);

			}
		});

		// insert graph
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart_record_activity);
		GraphicalView gView = ChartFactory.getLineChartView(this,
				getDemoDataset(), getDemoRenderer());
		layout.addView(gView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

	}

	// to wipe database
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.delete_records:
			// as user if sure
			AlertDialog.Builder deleteRecs = new AlertDialog.Builder(this);
			deleteRecs.setTitle("Delete Records");
			deleteRecs.setMessage("Are you sure?");
			deleteRecs.setCancelable(false);

			// if yes, clear database and return to main screen
			deleteRecs.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							db.closeDB();
							db.deleteAll();
							Intent intent = new Intent(RecordActivity.this,
									MainActivity.class);
							startActivity(intent);
						}
					});

			// if no, return to records
			deleteRecs.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			AlertDialog delete = deleteRecs.create();
			delete.show();

			break;
		}
	}

	// set x and y arrays for achartengine graph (taken from demo included in
	// library)
	private XYMultipleSeriesDataset getDemoDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		for (int i = 0; i < session; i++) {
			series.add(xSession[i], yAverage[i]);
		}
		dataset.addSeries(series);
		return dataset;
	}

	// sets visuals of the graph (taken from demo included in library)
	private XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(18);
		renderer.setChartTitleTextSize(18);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(0);
		renderer.setMarginsColor(Color.BLACK);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setPointSize(1f);
		renderer.setMargins(new int[] { 30, 30, 20, 20 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.rgb(0, 102, 204));
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillBelowLine(false);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		renderer.setZoomEnabled(false);
		setChartSettings(renderer);
		return renderer;
	}

	// sets visuals of the graph (taken from demo included in library)
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Average Performance by Session");
		renderer.setXTitle("Session");
		renderer.setYTitle("Average");
		renderer.setGridColor(Color.DKGRAY);
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, -70, 40 });
		renderer.setFitLegend(false);
		renderer.setShowGrid(true);
		renderer.setXAxisMin(1);
		renderer.setXAxisMax(session);
		renderer.setYAxisMin(0);
		renderer.setZoomEnabled(false);
		renderer.setYAxisMax(maxAverage + 1);
		renderer.setYLabelsColor(0, Color.DKGRAY);
		renderer.setXLabelsColor(Color.DKGRAY);
		renderer.setLabelsColor(Color.DKGRAY);
		renderer.setAxesColor(Color.rgb(220, 220, 220));
		renderer.setGridColor(Color.rgb(220, 220, 220));
		renderer.setMarginsColor(Color.WHITE);
	}

}
