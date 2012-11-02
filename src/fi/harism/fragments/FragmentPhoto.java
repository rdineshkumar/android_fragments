package fi.harism.fragments;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentPhoto extends Fragment implements View.OnClickListener {

	private static final int ACTION_CODE = 100;

	private boolean mPhotoTaken;
	private Uri mPhotoUri;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_CODE && resultCode == Activity.RESULT_OK) {
			ImageView imageView = (ImageView) getActivity().findViewById(
					R.id.imageview);
			imageView.setImageURI(null);
			imageView.setImageURI(mPhotoUri);
			mPhotoTaken = true;
		}
	}

	@Override
	public void onClick(View view) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
		startActivityForResult(intent, ACTION_CODE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		File photoFile = new File(Environment.getExternalStorageDirectory(),
				"fragment.jpg");
		mPhotoUri = Uri.fromFile(photoFile);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo, container, false);

		view.findViewById(R.id.button).setOnClickListener(this);

		if (mPhotoTaken) {
			ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
			imageView.setImageURI(mPhotoUri);
		}

		return view;
	}

}
