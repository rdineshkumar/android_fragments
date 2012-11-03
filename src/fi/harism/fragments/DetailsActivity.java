package fi.harism.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class DetailsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(R.string.subtitle_activity_main);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		setContentView(R.layout.activity_details);

		Bundle args;
		if (getIntent().getSerializableExtra("details") != null) {
			args = getIntent().getExtras();
		} else {
			Details details = DetailsDataSource.getInstance(this).getDetails(
					getIntent().getLongExtra("detailsId", -1));

			args = new Bundle();
			args.putSerializable("details", details);

			getIntent().putExtras(args);
		}

		DetailsFragment detailsFragment = new DetailsFragment();
		detailsFragment.setArguments(args);

		getFragmentManager().beginTransaction()
				.replace(R.id.details_container, detailsFragment).commit();
	}

}
