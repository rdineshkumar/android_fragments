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
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsFragment extends Fragment implements View.OnClickListener {

	private static final int ACTION_CODE = 100;

	private Details mDetails;
	private File mPhotoFile = new File(
			Environment.getExternalStorageDirectory(), "fragment.jpg");

	private boolean isDualPane() {
		return getFragmentManager().findFragmentById(R.id.fragment_items) != null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_CODE && resultCode == Activity.RESULT_OK) {
			mDetails.setPhoto(null);

			try {
				int read;
				byte[] buffer = new byte[8192];
				FileInputStream fis = new FileInputStream(mPhotoFile);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				while ((read = fis.read(buffer)) != -1) {
					bos.write(buffer, 0, read);
				}
				fis.close();
				mDetails.setPhoto(bos.toByteArray());
			} catch (Exception ex) {
				Toast.makeText(getActivity(), "Unable to read photo.",
						Toast.LENGTH_LONG).show();
			}

			getArguments().putSerializable("details", mDetails);

			updatePhoto();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_take_photo: {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
			startActivityForResult(intent, ACTION_CODE);
			break;
		}
		case R.id.button_save: {
			mDetails.setName(((EditText) getView().findViewById(
					R.id.edittext_name)).getText().toString());
			mDetails.setAddress(((EditText) getView().findViewById(
					R.id.edittext_address)).getText().toString());
			mDetails.setPlanet1(((Spinner) getView()
					.findViewById(R.id.spinner1)).getSelectedItemPosition());
			mDetails.setPlanet2(((Spinner) getView()
					.findViewById(R.id.spinner2)).getSelectedItemPosition());
			mDetails.setPlanet3(((Spinner) getView()
					.findViewById(R.id.spinner3)).getSelectedItemPosition());
			DetailsDataSource.getInstance(getActivity())
					.updateDetails(mDetails);
			break;
		}
		case R.id.button_delete: {
			DetailsDataSource.getInstance(getActivity()).deleteDetails(
					mDetails.getId());
			if (isDualPane()) {
				getFragmentManager().beginTransaction().remove(this).commit();
			} else {
				getActivity().finish();
			}
			break;
		}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mDetails = (Details) getArguments().getSerializable("details");
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
		spinner1.setSelection(mDetails.getPlanet1());

		Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner2);
		spinner2.setAdapter(adapter);
		spinner2.setSelection(mDetails.getPlanet2());

		Spinner spinner3 = (Spinner) view.findViewById(R.id.spinner3);
		spinner3.setAdapter(adapter);
		spinner3.setSelection(mDetails.getPlanet3());

		((TextView) view.findViewById(R.id.edittext_name)).setText(mDetails
				.getName());
		((TextView) view.findViewById(R.id.edittext_address)).setText(mDetails
				.getAddress());

		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						getView().getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						updatePhoto();
					}
				});

		return view;
	}

	private void updatePhoto() {
		ImageView imageView = (ImageView) getView()
				.findViewById(R.id.imageview);
		imageView.setImageBitmap(null);
		int viewWidth = getView().getWidth();
		byte[] imageData = mDetails.getPhoto();

		if (imageData != null && viewWidth != 0) {
			BitmapFactory.Options bounds = new BitmapFactory.Options();
			bounds.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(imageData, 0, imageData.length,
					bounds);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = bounds.outWidth / viewWidth;

			imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageData,
					0, imageData.length, options));
		}
	}

}
