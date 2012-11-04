package fi.harism.fragments;

import java.io.File;

import android.os.Environment;

public class Constants {

	public static final String ARG_ID = "arg_id";
	public static final String ARG_KEEP = "arg_keep";
	public static final String ARG_PHOTO = "arg_photo";

	public static final String DB_COLUMN_ADDRESS = "address";
	public static final String DB_COLUMN_COMMENTS = "comments";
	public static final String DB_COLUMN_COND_KITCHEN = "cond_kitchen";
	public static final String DB_COLUMN_COND_OVERALL = "cond_overall";
	public static final String DB_COLUMN_COND_TOILET = "cond_toilet";
	public static final String DB_COLUMN_ID = "_id";
	public static final String DB_COLUMN_NAME = "name";
	public static final String DB_FILE = "fragment.db";
	public static final String DB_TABLE_DETAILS = "details";
	public static final int DB_VERSION = 1;

	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory(), "Fragments");
	public static final String PHOTO_PREFIX = "photo_";
	public static final File PHOTO_TEMP = new File(Constants.PHOTO_DIR, "temp");

}
