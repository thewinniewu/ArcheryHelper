package com.cs50.project.database;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Record implements Parcelable {

	private long id; // id in sql database
	private int session; // session number
	private int roundno; // round number within session
	private int arrows; // number of arrows shot in round
	private int roundsum; // total points of round
	double average; // average points per round

	public Record() {

	}

	// constructor
	public Record(int session, int roundno, int arrows, int roundsum,
			double average) {
		this.session = session;
		this.roundno = roundno;
		this.arrows = arrows;
		this.roundsum = roundsum;
		this.average = average;
	}

	// setters and getters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

	public int getRoundno() {
		return roundno;
	}

	public void setRoundno(int roundno) {
		this.roundno = roundno;
	}

	public int getArrows() {
		return arrows;
	}

	public void setArrows(int arrows) {
		this.arrows = arrows;
	}

	public int getRoundsum() {
		return roundsum;
	}

	public void setRoundsum(int roundsum) {
		this.roundsum = roundsum;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	// parcelable methods to carry object through intent
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(session);
		dest.writeInt(roundno);
		dest.writeInt(arrows);
		dest.writeInt(roundsum);
		dest.writeDouble(average);

	}

	private void readFromParcel(Parcel in) {
		session = in.readInt();
		roundno = in.readInt();
		arrows = in.readInt();
		roundsum = in.readInt();
		average = in.readDouble();

	}

	public Record(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Record createFromParcel(Parcel in) {
			return new Record(in);
		}

		public Record[] newArray(int size) {
			return new Record[size];
		}
	};

}