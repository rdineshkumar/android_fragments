package fi.harism.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsFragment extends Fragment implements View.OnClickListener {

	private static final int ACTION_CODE = 100;

	private DetailsDataSource mDataSource;
	private byte[] mPhotoData;
	private File mPhotoFile = new File(
			Environment.getExternalStorageDirectory(), "fragment.jpg");;

	private boolean isDualPane() {
		return getFragmentManager().findFragmentById(R.id.fragment_items) != null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Details details = mDataSource.getDetails(getArguments().getLong(
				"detailsId", -1));
		mPhotoData = details.getPhoto();
		updatePhoto();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_CODE && resultCode == Activity.RESULT_OK) {
			mPhotoData = null;

			try {
				int read;
				byte[] buffer = new byte[8192];
				FileInputStream fis = new FileInputStream(mPhotoFile);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while ((read = fis.read(buffer)) != -1) {
					bos.write(buffer, 0, read);
				}
				fis.close();
				mPhotoData = bos.toByteArray();
			} catch (Exception ex) {
				Toast.makeText(getActivity(), "Unable to read photo.",
						Toast.LENGTH_LONG).show();
			}

			updatePhoto();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_take_photo:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
			startActivityForResult(intent, ACTION_CODE);
			break;
		case R.id.button_save:
			Details details = new Details();
			details.setId(getArguments().getLong("detailsId", -1));
			details.setName(((EditText) getView().findViewById(
					R.id.edittext_name)).getText().toString());
			details.setAddress(((EditText) getView().findViewById(
					R.id.edittext_address)).getText().toString());
			details.setPhoto(mPhotoData);
			mDataSource.updateDetails(details);
			break;
		case R.id.button_delete:
			mDataSource.deleteDetails(getArguments().getLong("detailsId", -1));
			if (isDualPane()) {
				getFragmentManager().beginTransaction().remove(this).commit();
			} else {
				getActivity().finish();
			}
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mDataSource = DetailsDataSource.getInstance(getActivity());

		mPhotoFile = new File(Environment.getExternalStorageDirectory(),
				"fragment.jpg");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_details, container,
				false);

		view.findViewById(R.id.button_take_photo).setOnClickListener(this);
		view.findViewById(R.id.button_save).setOnClickListener(this);
		view.findViewById(R.id.button_delete).setOnClickListener(this);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.planets_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
		spinner1.setAdapter(adapter);

		Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
		spinner2.setAdapter(adapter);

		Spinner spinner3 = (Spinner) view.findViewById(R.id.spinner3);
		spinner3.setAdapter(adapter);

		Details details = mDataSource.getDetails(getArguments().getLong(
				"detailsId", -1));
		((TextView) view.findViewById(R.id.edittext_name)).setText(details
				.getName());
		((TextView) view.findViewById(R.id.edittext_address)).setText(details
				.getAddress());

		return view;
	}

	private void updatePhoto() {
		if (mPhotoData != null) {
			ImageView imageView = (ImageView) getView().findViewById(
					R.id.imageview);
			imageView.setImageBitmap(null);

			BitmapFactory.Options bounds = new BitmapFactory.Options();
			bounds.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(mPhotoData, 0, mPhotoData.length,
					bounds);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = getView().getWidth() / bounds.outWidth;

			imageView.setImageBitmap(BitmapFactory.decodeByteArray(mPhotoData,
					0, mPhotoData.length, options));
		}
	}

}
