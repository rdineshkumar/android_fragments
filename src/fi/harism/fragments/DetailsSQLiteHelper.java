package fi.harism.fragments;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DetailsSQLiteHelper extends SQLiteOpenHelper {

	public DetailsSQLiteHelper(Context context) {
		super(context, Constants.DB_FILE, null, Constants.DB_VERSION);
	}

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
		StringBuffer sb = new StringBuffer();

		sb.append("create table ").append(Constants.DB_TABLE_DETAILS)
				.append(" (");
		sb.append(Constants.DB_COLUMN_ID).append(
				" integer primary key autoincrement, ");
		sb.append(Constants.DB_COLUMN_NAME).append(" text, ");
		sb.append(Constants.DB_COLUMN_ADDRESS).append(" text, ");
		sb.append(Constants.DB_COLUMN_CONDITION_OVERALL).append(" integer, ");
		sb.append(Constants.DB_COLUMN_CONDITION_KITCHEN).append(" integer, ");
		sb.append(Constants.DB_COLUMN_CONDITION_TOILET).append(" integer, ");
		sb.append(Constants.DB_COLUMN_COMMENTS).append(" text, ");
		sb.append(Constants.DB_COLUMN_PHOTO).append(" blob");
		sb.append(");");

		database.execSQL(sb.toString());

		deleteFile(Constants.PHOTO_DIR);
		Constants.PHOTO_DIR.mkdirs();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.DB_TABLE_DETAILS);
		onCreate(db);
	}

}
