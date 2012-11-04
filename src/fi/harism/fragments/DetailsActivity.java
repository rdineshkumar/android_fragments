package fi.harism.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

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
