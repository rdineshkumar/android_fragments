/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package fi.harism.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Details data source helper class.
 */
public class DetailsDataSource {

	// Singleton instance.
	private static DetailsDataSource mInstance;

	/**
	 * Returns singleton instance data source.
	 */
	public static DetailsDataSource getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DetailsDataSource(context);
			mInstance.open();
		}
		return mInstance;
	}

	private String[] allColumns = { Constants.DB_COLUMN_ID,
			Constants.DB_COLUMN_NAME, Constants.DB_COLUMN_ADDRESS,
			Constants.DB_COLUMN_COND_OVERALL, Constants.DB_COLUMN_COND_KITCHEN,
			Constants.DB_COLUMN_COND_TOILET, Constants.DB_COLUMN_COMMENTS };
	private SQLiteDatabase database;
	private DetailsSQLiteHelper dbHelper;
	private Vector<Observer> mObservers = new Vector<Observer>();

	/**
	 * Default constructor.
	 */
	public DetailsDataSource(Context context) {
		dbHelper = new DetailsSQLiteHelper(context);
	}

	/**
	 * Add data base change observer.
	 */
	public void addObserver(Observer observer) {
		mObservers.add(observer);
	}

	/**
	 * Close database.
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Converts cursor to Details container.
	 */
	private Details cursorToDetails(Cursor cursor) {
		Details details = new Details();
		details.setId(cursor.getLong(0));
		details.setName(cursor.getString(1));
		details.setAddress(cursor.getString(2));
		details.setCondOverall(cursor.getInt(3));
		details.setCondKitchen(cursor.getInt(4));
		details.setCondToilet(cursor.getInt(5));
		details.setComments(cursor.getString(6));
		return details;
	}

	/**
	 * Delete row with given id from database.
	 */
	public void deleteDetails(long id) {
		database.delete(Constants.DB_TABLE_DETAILS, Constants.DB_COLUMN_ID
				+ " = " + id, null);
		notifyObservers();
	}

	/**
	 * Get all details array.
	 */
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

	/**
	 * Get details with given id.
	 */
	public Details getDetails(long id) {
		Cursor cursor = database.query(Constants.DB_TABLE_DETAILS, allColumns,
				Constants.DB_COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		Details details = cursorToDetails(cursor);
		cursor.close();
		return details;
	}

	/**
	 * Inserts new details row.
	 */
	public long insertDetails(Details details) {
		ContentValues values = new ContentValues();

		values.put(Constants.DB_COLUMN_NAME, details.getName());
		values.put(Constants.DB_COLUMN_ADDRESS, details.getAddress());
		values.put(Constants.DB_COLUMN_COND_OVERALL, details.getCondOverall());
		values.put(Constants.DB_COLUMN_COND_KITCHEN, details.getCondKitchen());
		values.put(Constants.DB_COLUMN_COND_TOILET, details.getCondToilet());
		values.put(Constants.DB_COLUMN_COMMENTS, details.getComments());
		long id = database.insert(Constants.DB_TABLE_DETAILS, null, values);

		notifyObservers();
		return id;
	}

	/**
	 * Private notify observers method.
	 */
	private void notifyObservers() {
		for (Observer observer : mObservers) {
			observer.onDetailsChanged();
		}
	}

	/**
	 * Open database.
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Remove database change observer.
	 */
	public void removeObserver(Observer observer) {
		mObservers.remove(observer);
	}

	/**
	 * Update existing details.
	 */
	public void updateDetails(Details details) {
		ContentValues values = new ContentValues();

		values.put(Constants.DB_COLUMN_ID, details.getId());
		values.put(Constants.DB_COLUMN_NAME, details.getName());
		values.put(Constants.DB_COLUMN_ADDRESS, details.getAddress());
		values.put(Constants.DB_COLUMN_COND_OVERALL, details.getCondOverall());
		values.put(Constants.DB_COLUMN_COND_KITCHEN, details.getCondKitchen());
		values.put(Constants.DB_COLUMN_COND_TOILET, details.getCondToilet());
		values.put(Constants.DB_COLUMN_COMMENTS, details.getComments());
		database.replace(Constants.DB_TABLE_DETAILS, null, values);

		notifyObservers();
	}

	/**
	 * Observer interface for database changes.
	 */
	public interface Observer {
		public void onDetailsChanged();
	}

}
