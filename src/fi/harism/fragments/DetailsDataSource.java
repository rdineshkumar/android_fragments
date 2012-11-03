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
			DetailsSQLiteHelper.COLUMN_NAME,
			DetailsSQLiteHelper.COLUMN_ADDRESS,
			DetailsSQLiteHelper.COLUMN_PHOTO };
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

	public Details createDetails() {
		ContentValues values = new ContentValues();
		values.put(DetailsSQLiteHelper.COLUMN_NAME, "");
		values.put(DetailsSQLiteHelper.COLUMN_ADDRESS, "");
		long insertId = database.insert(DetailsSQLiteHelper.TABLE_DETAILS,
				null, values);

		notifyObservers();

		return getDetails(insertId);
	}

	private Details cursorToDetails(Cursor cursor) {
		Details details = new Details();
		details.setId(cursor.getLong(0));
		details.setName(cursor.getString(1));
		details.setAddress(cursor.getString(2));
		details.setPhoto(cursor.getBlob(3));
		return details;
	}

	public void deleteDetails(long id) {
		database.delete(DetailsSQLiteHelper.TABLE_DETAILS,
				DetailsSQLiteHelper.COLUMN_ID + " = " + id, null);
		notifyObservers();
	}

	public List<Details> getAllDetails() {
		List<Details> detailsList = new ArrayList<Details>();

		Cursor cursor = database.query(DetailsSQLiteHelper.TABLE_DETAILS,
				allColumns, null, null, null, null,
				DetailsSQLiteHelper.COLUMN_NAME);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Details details = cursorToDetails(cursor);
			detailsList.add(details);
			cursor.moveToNext();
		}
		cursor.close();

		return detailsList;
	}

	public Details getDetails(long id) {
		Cursor cursor = database.query(DetailsSQLiteHelper.TABLE_DETAILS,
				allColumns, DetailsSQLiteHelper.COLUMN_ID + " = " + id, null,
				null, null, null);
		cursor.moveToFirst();
		Details details = cursorToDetails(cursor);
		cursor.close();
		return details;
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

	public void updateDetails(Details details) {
		ContentValues values = new ContentValues();
		values.put(DetailsSQLiteHelper.COLUMN_ID, details.getId());
		values.put(DetailsSQLiteHelper.COLUMN_NAME, details.getName());
		values.put(DetailsSQLiteHelper.COLUMN_ADDRESS, details.getAddress());
		values.put(DetailsSQLiteHelper.COLUMN_PHOTO, details.getPhoto());
		database.replace(DetailsSQLiteHelper.TABLE_DETAILS, null, values);

		notifyObservers();
	}

	public interface Observer {
		public void onDetailsChanged();
	}

}
