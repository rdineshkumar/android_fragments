package fi.harism.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DetailsSQLiteHelper extends SQLiteOpenHelper {

	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PHOTO = "photo";
	public static final String TABLE_DETAILS = "details";

	public DetailsSQLiteHelper(Context context) {
		super(context, "fragments.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		StringBuffer sb = new StringBuffer();

		sb.append("create table ").append(TABLE_DETAILS).append(" (");
		sb.append(COLUMN_ID).append(" integer primary key autoincrement, ");
		sb.append(COLUMN_NAME).append(" text not null, ");
		sb.append(COLUMN_ADDRESS).append(" text not null, ");
		sb.append(COLUMN_PHOTO).append(" blob");
		sb.append(");");

		database.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
		onCreate(db);
	}

}
