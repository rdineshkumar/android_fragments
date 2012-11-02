package fi.harism.fragments;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentPhoto extends Fragment implements View.OnClickListener {

	private Drawable mPhoto;
	private PhotoObserver mPhotoObserver;
	private Uri mPhotoUri;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mPhotoObserver = (PhotoObserver) activity;

	}

	@Override
	public void onClick(View view) {
		mPhotoObserver.onTakePhoto(mPhotoUri);
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

		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageDrawable(mPhoto);

		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		ImageView imageView = (ImageView) getActivity().findViewById(
				R.id.imageview);
		mPhoto = imageView.getDrawable();
	}

	public void onPhotoTaken() {
		ImageView imageView = (ImageView) getActivity().findViewById(
				R.id.imageview);
		imageView.setImageURI(mPhotoUri);
	}

	public interface PhotoObserver {
		public void onTakePhoto(Uri photoUri);
	}

}
