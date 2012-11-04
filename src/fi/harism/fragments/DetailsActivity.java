package fi.harism.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class DetailsActivity extends Activity {

	private DetailsFragment mDetailsFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(R.string.subtitle_activity_main);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);

		mDetailsFragment = new DetailsFragment();
		if (savedInstanceState != null) {
			mDetailsFragment.setArguments(savedInstanceState);
		} else {
			mDetailsFragment.setArguments(getIntent().getExtras());
		}

		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, mDetailsFragment).commit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putAll(mDetailsFragment.getArguments());
	}

}
