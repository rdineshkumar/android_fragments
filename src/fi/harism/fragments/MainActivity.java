package fi.harism.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;

public class MainActivity extends Activity implements
		FragmentPhoto.PhotoObserver {

	private static final int ACTION_CODE = 100;
	private Uri mPictureUri;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_CODE && resultCode == RESULT_OK) {
			FragmentPhoto fragmentPhoto = (FragmentPhoto) getFragmentManager()
					.findFragmentById(R.id.fragment_photo);
			fragmentPhoto.onPhotoTaken();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle(R.string.subtitle_activity_main);

		MainActivity self = (MainActivity) getLastNonConfigurationInstance();
		if (self != null) {
			mPictureUri = self.mPictureUri;
		}

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED);
		setContentView(R.layout.activity_main);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this;
	}

	@Override
	public void onTakePhoto(Uri photoUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent, ACTION_CODE);
	}

}
