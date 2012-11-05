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

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Details view activity.
 */
public class DetailsActivity extends Activity {

	@Override
	public void finish() {
		super.finish();
		super.overridePendingTransition(R.anim.keep, R.anim.out);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(R.string.subtitle_activity_main);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		setContentView(R.layout.activity_details);

		if (savedInstanceState == null
				|| !savedInstanceState.getBoolean(Constants.ARG_KEEP)) {
			DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager()
					.findFragmentByTag("details");
			detailsFragment.setDetailsId(getIntent().getLongExtra(
					Constants.ARG_ID, -1));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(Constants.ARG_KEEP, true);
	}

}
