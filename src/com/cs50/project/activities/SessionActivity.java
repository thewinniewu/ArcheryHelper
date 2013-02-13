package com.cs50.project.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.cs50.project.archeryhelper.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cs50.project.database.MySQLiteHelper;
import com.cs50.project.database.Record;

public class SessionActivity extends Activity {

	// global variables
	int[] xRound;
	double[] yAverage;
	ArrayList<Record> records;
	double maxAverage;
	private MySQLiteHelper db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.session_screen);

		// initializing database
		db = new MySQLiteHelper(this);
		db.openDB();

		// getting intent data
		Intent in = getIntent();

		// get values from previous intent
		records = in.getParcelableArrayListExtra("rounds");

		// displaying all values on the screen
		TextView title = (TextView) findViewById(R.id.session_title);
		TextView rounds = (TextView) findViewById(R.id.round_info);
		TextView detail = (TextView) findViewById(R.id.arrows_average);

		title.setText("Session " + records.get(0).getSession());
		rounds.setText(records.size() + " Rounds Shot");

		// x and y arrays for dataset for graph
		xRound = new int[records.size()];
		yAverage = new double[records.size()];

		// loop through records and get total arrows shot, average, and
		// roundsum.
		int sum = 0;
		int totalArrows = 0;
		for (int i = 0; i < records.size(); i++) {
			totalArrows += records.get(i).getArrows();
			sum += records.get(i).getRoundsum();
			xRound[i] = i + 1;
			yAverage[i] = records.get(i).getAverage();
			if (yAverage[i] > maxAverage) {
				maxAverage = yAverage[i];
			}
		}

		// format average to 2dp
		DecimalFormat df = new DecimalFormat("#.##");
		String average = df.format(sum * 1.0 / totalArrows);

		// display round information
		detail.setText(totalArrows + " Arrows Shot\nAverage: " + average
				+ "\nTotal: " + sum + "/" + totalArrows * 10);

		// insert graph
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		GraphicalView gView = ChartFactory.getLineChartView(this,
				getDemoDataset(), getDemoRenderer());
		layout.addView(gView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

	}

	// set x and y arrays for achartengine graph (taken from demo included in
	// library)
	private XYMultipleSeriesDataset getDemoDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");
		for (int i = 0; i < records.size(); i++) {
			series.add(xRound[i], yAverage[i]);
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
		renderer.setPointSize(1f);
		renderer.setMargins(new int[] { 30, 30, 20, 20 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		setChartSettings(renderer);
		r.setColor(Color.rgb(0, 102, 204));
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillBelowLine(false);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		renderer.setZoomEnabled(false);
		return renderer;
	}

	// sets visuals of the graph (taken from demo included in library)
	private void setChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Average Performance by Round");
		renderer.setXTitle("Round");
		renderer.setYTitle("Average");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, -70, 40 });
		renderer.setFitLegend(false);
		renderer.setShowGrid(true);
		renderer.setXAxisMin(1);
		renderer.setXAxisMax(records.size());
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
