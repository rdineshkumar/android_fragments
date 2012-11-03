package fi.harism.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {

	private boolean isDualPane() {
		View details = findViewById(R.id.details_container);
		return details != null && details.getVisibility() == View.VISIBLE;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(R.string.subtitle_activity_main);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add:
			Details details = DetailsDataSource.getInstance(this)
					.createDetails();

			if (isDualPane()) {
				Bundle bundle = new Bundle();
				bundle.putLong("detailsId", details.getId());
				DetailsFragment detailsFragment = new DetailsFragment();
				detailsFragment.setArguments(bundle);

				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.replace(R.id.details_container, detailsFragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			} else {
				Intent intent = new Intent();
				intent.setClass(this, DetailsActivity.class);
				intent.putExtra("detailsId", details.getId());
				startActivity(intent);

			}
			return true;
		}
		return false;
	}

}
