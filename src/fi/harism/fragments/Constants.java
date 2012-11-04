package fi.harism.fragments;

import java.io.File;

import android.os.Environment;

public class Constants {

	public static final String DB_COLUMN_ADDRESS = "address";
	public static final String DB_COLUMN_COMMENTS = "comments";
	public static final String DB_COLUMN_CONDITION_KITCHEN = "condition_kitchen";
	public static final String DB_COLUMN_CONDITION_OVERALL = "condition_overall";
	public static final String DB_COLUMN_CONDITION_TOILET = "condition_toilet";
	public static final String DB_COLUMN_ID = "_id";
	public static final String DB_COLUMN_NAME = "name";
	public static final String DB_COLUMN_PHOTO = "photo";
	public static final String DB_FILE = "fragment.db";
	public static final String DB_TABLE_DETAILS = "details";
	public static final int DB_VERSION = 1;

	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory(), "Fragments");

}
