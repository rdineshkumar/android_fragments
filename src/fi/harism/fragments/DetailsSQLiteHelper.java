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

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite helper class.
 */
public class DetailsSQLiteHelper extends SQLiteOpenHelper {

	/**
	 * Default constructor.
	 */
	public DetailsSQLiteHelper(Context context) {
		super(context, Constants.DB_FILE, null, Constants.DB_VERSION);
	}

	/**
	 * Deletes file or directory from file system.
	 */
	private void deleteFile(File root) {
		if (root.isDirectory()) {
			for (File file : root.listFiles()) {
				deleteFile(file);
			}
		}
		root.delete();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// Generate SQL create string.
		StringBuffer sb = new StringBuffer();
		sb.append("create table ").append(Constants.DB_TABLE_DETAILS)
				.append(" (");
		sb.append(Constants.DB_COLUMN_ID).append(
				" integer primary key autoincrement, ");
		sb.append(Constants.DB_COLUMN_NAME).append(" text, ");
		sb.append(Constants.DB_COLUMN_ADDRESS).append(" text, ");
		sb.append(Constants.DB_COLUMN_COND_OVERALL).append(" integer, ");
		sb.append(Constants.DB_COLUMN_COND_KITCHEN).append(" integer, ");
		sb.append(Constants.DB_COLUMN_COND_TOILET).append(" integer, ");
		sb.append(Constants.DB_COLUMN_COMMENTS).append(" text");
		sb.append(");");
		database.execSQL(sb.toString());

		// Recreate photos directory.
		deleteFile(Constants.PHOTO_DIR);
		Constants.PHOTO_DIR.mkdirs();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_DETAILS);
		onCreate(db);
	}

}
