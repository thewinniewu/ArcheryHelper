package com.cs50.project.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper {

	// set constant strings for columns
	public static final String TABLE_RECORDS = "records";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SESSION = "session";
	public static final String COLUMN_ROUNDNO = "roundno";
	public static final String COLUMN_ARROWS = "arrows";
	public static final String COLUMN_ROUNDSUM = "roundsum";
	public static final String COLUMN_ROUNDAVE = "average";

	private static final String DATABASE_NAME = "records.db";
	private static final int DATABASE_VERSION = 1;

	// database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_RECORDS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_SESSION
			+ " integer, " + COLUMN_ROUNDNO + " integer, " + COLUMN_ARROWS
			+ " integer, " + COLUMN_ROUNDSUM + " integer, " + COLUMN_ROUNDAVE
			+ " double " + ");";

	// makes database accessible from all activities
	private DbHelper helper;
	private SQLiteDatabase database;
	private final Context context;

	private static class DbHelper extends SQLiteOpenHelper {

		DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(MySQLiteHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
			onCreate(db);
		}
	}

	// constructor
	public MySQLiteHelper(Context c) {
		context = c;
	}

	// open
	public MySQLiteHelper openDB() {
		helper = new DbHelper(context);
		database = helper.getWritableDatabase();
		return this;
	}

	// close
	public void closeDB() {
		helper.close();
	}

	// delete all rows
	public int deleteAll() {
		SQLiteDatabase database = helper.getWritableDatabase();
		return database.delete(TABLE_RECORDS, null, null);
	}

	// delete last row
	public boolean deleteLastRecord() {
		SQLiteDatabase database = helper.getWritableDatabase();
		String countQuery = "SELECT  * FROM " + TABLE_RECORDS;
		Cursor cursor = database.rawQuery(countQuery, null);
		Log.d("Count", cursor.getCount() + "");
		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			return database.delete(TABLE_RECORDS,
					COLUMN_ID + "=" + cursor.getInt(0), null) > 0;
		} else {
			return false;
		}

	}

	// add new record
	public void addRecord(Record record) {
		SQLiteDatabase database = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COLUMN_SESSION, record.getSession());
		values.put(COLUMN_ROUNDNO, record.getRoundno());
		values.put(COLUMN_ARROWS, record.getArrows());
		values.put(COLUMN_ROUNDSUM, record.getRoundsum());
		values.put(COLUMN_ROUNDAVE, record.getAverage());

		// inserting row
		database.insert(TABLE_RECORDS, null, values);
		database.close(); // closing database connection
	}

	// pull all records from database
	public ArrayList<Record> getAllRecords() {
		ArrayList<Record> recordList = new ArrayList<Record>();
		// select all query
		String selectQuery = "SELECT  * FROM " + TABLE_RECORDS + " ORDER BY "
				+ COLUMN_SESSION + " ASC";

		Cursor cursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Record record = new Record();
				record.setSession(cursor.getInt(1));
				record.setRoundno(cursor.getInt(2));
				record.setArrows(cursor.getInt(3));
				record.setRoundsum(cursor.getInt(4));
				record.setAverage(cursor.getDouble(5));

				// add to arraylist
				recordList.add(record);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return recordList;
	}

	// pull records according to session
	public ArrayList<Record> getSessionRecords(int session) {
		ArrayList<Record> recordList = new ArrayList<Record>();
		// select all query
		String selectQuery = "SELECT  * FROM " + TABLE_RECORDS + " WHERE "
				+ COLUMN_SESSION + "=" + session;

		Cursor cursor = database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Record record = new Record();
				record.setSession(cursor.getInt(1));
				record.setRoundno(cursor.getInt(2));
				record.setArrows(cursor.getInt(3));
				record.setRoundsum(cursor.getInt(4));
				record.setAverage(cursor.getDouble(5));

				// add record to arraylist
				recordList.add(record);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return recordList;
	}

	// get specified column from last record entry
	public int getLast(int element) {
		String countQuery = "SELECT  * FROM " + TABLE_RECORDS;
		Cursor cursor = database.rawQuery(countQuery, null);
		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			return cursor.getInt(element);
		} else {
			return 0;
		}
	}
}