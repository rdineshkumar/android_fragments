package fi.harism.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DetailsDataSource {

	private static DetailsDataSource mInstance;
	public static DetailsDataSource getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DetailsDataSource(context);
			mInstance.open();
		}
		return mInstance;
	}
	private String[] allColumns = { DetailsSQLiteHelper.COLUMN_ID,
			DetailsSQLiteHelper.COLUMN_NAME, DetailsSQLiteHelper.COLUMN_ADDRESS };
	private SQLiteDatabase database;
	private DetailsSQLiteHelper dbHelper;

	private Vector<Observer> mObservers = new Vector<Observer>();

	public DetailsDataSource(Context context) {
		dbHelper = new DetailsSQLiteHelper(context);
	}

	public void addObserver(Observer observer) {
		mObservers.add(observer);
	}

	public void close() {
		dbHelper.close();
	}

	public Details createDetails(String name, String address) {
		ContentValues values = new ContentValues();
		values.put(DetailsSQLiteHelper.COLUMN_NAME, name);
		values.put(DetailsSQLiteHelper.COLUMN_ADDRESS, address);
		long insertId = database.insert(DetailsSQLiteHelper.TABLE_DETAILS,
				null, values);
		Cursor cursor = database.query(DetailsSQLiteHelper.TABLE_DETAILS,
				allColumns, DetailsSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		cursor.moveToFirst();
		Details newDetails = cursorToDetails(cursor);
		cursor.close();

		notifyObservers();

		return newDetails;
	}

	private Details cursorToDetails(Cursor cursor) {
		Details details = new Details();
		details.setId(cursor.getLong(0));
		details.setName(cursor.getString(1));
		details.setAddress(cursor.getString(2));
		return details;
	}

	public void deleteDetails(Details details) {
		long id = details.getId();
		database.delete(DetailsSQLiteHelper.TABLE_DETAILS,
				DetailsSQLiteHelper.COLUMN_ID + " = " + id, null);
		notifyObservers();
	}

	public List<Details> getAllDetails() {
		List<Details> detailsList = new ArrayList<Details>();

		Cursor cursor = database.query(DetailsSQLiteHelper.TABLE_DETAILS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Details details = cursorToDetails(cursor);
			detailsList.add(details);
			cursor.moveToNext();
		}
		cursor.close();

		return detailsList;
	}

	private void notifyObservers() {
		for (Observer observer : mObservers) {
			observer.onDetailsChanged();
		}
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void removeObserver(Observer observer) {
		mObservers.remove(observer);
	}

	public interface Observer {
		public void onDetailsChanged();
	}

}
