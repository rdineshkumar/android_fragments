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

	private String[] allColumns = { Constants.DB_COLUMN_ID,
			Constants.DB_COLUMN_NAME, Constants.DB_COLUMN_ADDRESS,
			Constants.DB_COLUMN_CONDITION_OVERALL,
			Constants.DB_COLUMN_CONDITION_KITCHEN,
			Constants.DB_COLUMN_CONDITION_TOILET, Constants.DB_COLUMN_COMMENTS,
			Constants.DB_COLUMN_PHOTO };
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

	/*
	 * public Details createDetails() { ContentValues values = new
	 * ContentValues(); //values.put(DetailsSQLiteHelper.COLUMN_NAME, "");
	 * //values.put(DetailsSQLiteHelper.COLUMN_ADDRESS, ""); long insertId =
	 * database.insert(DetailsSQLiteHelper.TABLE_DETAILS, null, values);
	 * 
	 * notifyObservers();
	 * 
	 * return getDetails(insertId); }
	 */
	private Details cursorToDetails(Cursor cursor) {
		Details details = new Details();
		details.setId(cursor.getLong(0));
		details.setName(cursor.getString(1));
		details.setAddress(cursor.getString(2));
		details.setConditionOverall(cursor.getInt(3));
		details.setConditionKitchen(cursor.getInt(4));
		details.setConditionToilet(cursor.getInt(5));
		details.setComments(cursor.getString(6));
		details.setPhoto(cursor.getBlob(7));
		return details;
	}

	public void deleteDetails(long id) {
		database.delete(Constants.DB_TABLE_DETAILS, Constants.DB_COLUMN_ID
				+ " = " + id, null);
		notifyObservers();
	}

	public List<Details> getAllDetails() {
		List<Details> detailsList = new ArrayList<Details>();

		Cursor cursor = database.query(Constants.DB_TABLE_DETAILS, allColumns,
				null, null, null, null, Constants.DB_COLUMN_NAME);

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
		Cursor cursor = database.query(Constants.DB_TABLE_DETAILS, allColumns,
				Constants.DB_COLUMN_ID + " = " + id, null, null, null, null);
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

		if (details.getId() != -1) {
			values.put(Constants.DB_COLUMN_ID, details.getId());
		}

		values.put(Constants.DB_COLUMN_NAME, details.getName());
		values.put(Constants.DB_COLUMN_ADDRESS, details.getAddress());
		values.put(Constants.DB_COLUMN_CONDITION_OVERALL,
				details.getConditionOverall());
		values.put(Constants.DB_COLUMN_CONDITION_KITCHEN,
				details.getConditionKitchen());
		values.put(Constants.DB_COLUMN_CONDITION_TOILET,
				details.getConditionToilet());
		values.put(Constants.DB_COLUMN_COMMENTS, details.getComments());
		values.put(Constants.DB_COLUMN_PHOTO, details.getPhoto());
		long id = database.replace(Constants.DB_TABLE_DETAILS, null, values);

		details.setId(id);
		notifyObservers();
	}

	public interface Observer {
		public void onDetailsChanged();
	}

}
