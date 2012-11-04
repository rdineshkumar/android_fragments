package fi.harism.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.keep, R.anim.out);
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

}
