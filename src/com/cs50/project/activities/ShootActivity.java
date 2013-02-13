package com.cs50.project.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs50.project.archeryhelper.R;
import com.cs50.project.database.MySQLiteHelper;
import com.cs50.project.database.Record;

public class ShootActivity extends Activity {

	// global variables
	private MySQLiteHelper db;
	int sessionNo;
	int roundNo;
	Record record;
	TextView recordInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoot_activity_zoom);

		// initializing database
		db = new MySQLiteHelper(this);
		db.openDB();

		// getting the session number
		sessionNo = db.getLast(1) + 1;

		// setting the initial round no
		roundNo = 1;

		// create new record
		record = new Record(sessionNo, roundNo, 0, 0, 0);

		// set info box on top left of screen
		recordInfo = (TextView) findViewById(R.id.round_info);
		recordInfo.setText("Session " + sessionNo + "\nRound " + roundNo
				+ "\nScore: 0");

	}

	// button toggle
	public void onClick(View view) {
		switch (view.getId()) {

		// instructions
		case R.id.help:
			final AlertDialog instructions = new AlertDialog.Builder(this)
					.create();
			instructions.setTitle("Instructions");
			instructions
					.setMessage("Press the numbers on the target to add points\n\nPress 'Next Round' for a new round of arrows\n\nIf you make a mistake, 'Undo Last' will undo the last round recorded.\n\nPress 'End Session' when you are done shooting!");
			instructions.setButton("Close",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							instructions.cancel();
						}
					});
			instructions.show();

			// scoring
		case R.id.zero:
			setRecord(0);
			break;
		case R.id.one:
			setRecord(1);
			break;
		case R.id.onef:
			setRecord(1);
			break;
		case R.id.two:
			setRecord(2);
			break;

		case R.id.three:
			setRecord(3);
			break;

		case R.id.four:
			setRecord(4);
			break;

		case R.id.five:
			setRecord(5);
			break;

		case R.id.six:
			setRecord(6);
			break;

		case R.id.seven:
			setRecord(7);
			break;

		case R.id.eight:
			setRecord(8);
			break;

		case R.id.nine:
			setRecord(9);
			break;
		case R.id.ten:
			setRecord(10);
			break;

		// get ready for next round
		case R.id.next_round:

			// add to database
			db.addRecord(record);

			// begin new round and reset info box
			roundNo++;
			record = new Record(sessionNo, roundNo, 0, 0, 0);
			recordInfo.setText("Session " + sessionNo + "\nRound " + roundNo
					+ "\nScore: " + record.getRoundsum());
			break;

		// end of shooting session
		case R.id.end_session:

			// ask user if sure
			AlertDialog.Builder sessionEnd = new AlertDialog.Builder(this);
			sessionEnd.setTitle("End Session");
			sessionEnd.setMessage("Are you sure?");
			sessionEnd.setCancelable(false);

			// if selected yes
			sessionEnd.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							// add round to database if arrows shot
							if (record.getRoundsum() > 0) {
								db.addRecord(record);
							}

							// close database and return to main screen
							db.closeDB();
							Intent intent = new Intent(ShootActivity.this,
									MainActivity.class);
							startActivity(intent);
						}
					});

			// if selected no, return to shooting
			sessionEnd.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			AlertDialog endSess = sessionEnd.create();
			endSess.show();

			break;

		// undo last round
		case R.id.undo_record:
			if (roundNo >= 1) {

				// ask user if sure
				AlertDialog.Builder undoDialog = new AlertDialog.Builder(this);
				undoDialog.setTitle("Undo Last Round");
				undoDialog.setMessage("Are you sure?");
				undoDialog.setCancelable(false);

				// if sure, delete from database and restart round
				undoDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								db.deleteLastRecord();
								Log.d("last record", "deleted");
								roundNo--;
								record = new Record(sessionNo, roundNo, 0, 0, 0);
								recordInfo.setText("Session " + sessionNo
										+ "\nRound " + roundNo + "\nScore: "
										+ record.getRoundsum());
							}

						});

				// if no, return to shooting
				undoDialog.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				AlertDialog undo = undoDialog.create();
				undo.show();
			}
			break;
		}

	}

	// method to set current record as points are added
	void setRecord(int point) {
		record.setArrows(record.getArrows() + 1);
		record.setRoundsum(record.getRoundsum() + point);
		record.setAverage((record.getRoundsum() * 1.0) / record.getArrows());
		recordInfo.setText("Session " + sessionNo + "\nRound " + roundNo
				+ "\nScore: " + record.getRoundsum());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
