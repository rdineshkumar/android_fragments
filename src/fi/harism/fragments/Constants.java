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

import android.os.Environment;

/**
 * Application wide constant values.
 */
public class Constants {

	// Ids used in Bundles.
	public static final String ARG_ID = "arg_id";
	public static final String ARG_KEEP = "arg_keep";
	public static final String ARG_PHOTO = "arg_photo";

	// Database related values.
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

	// Photo storing related values.
	public static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory(), "Fragments");
	public static final String PHOTO_PREFIX = "photo_";
	public static final File PHOTO_TEMP = new File(Constants.PHOTO_DIR, "temp");

}
