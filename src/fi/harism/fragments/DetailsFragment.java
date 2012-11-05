/*
   Copyright 2012 Harri Smatt

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package fi.harism.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

/**
 * Details fragment class.
 */
public class DetailsFragment extends Fragment implements View.OnClickListener {

	// Camera activity code id.
	private static final int ACTION_CODE = 100;
	// Current details id.
	private long mDetailsId = -1;
	// Photo jpeg or png data.
	private byte[] mPhotoData;

	/**
	 * Helper method for getting edit text content.
	 */
	private String getEditText(int id) {
		return ((EditText) getView().findViewById(id)).getText().toString();
	}

	/**
	 * Helper method for getting spinner value.
	 */
	private int getSpinner(int id) {
		return ((Spinner) getView().findViewById(id)).getSelectedItemPosition();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setDetailsId(mDetailsId);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_CODE && resultCode == Activity.RESULT_OK) {
			mPhotoData = readFile(Constants.PHOTO_TEMP);
			Constants.PHOTO_TEMP.delete();
			updatePhoto(mPhotoData);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_take_photo: {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(Constants.PHOTO_TEMP));
			startActivityForResult(intent, ACTION_CODE);
			break;
		}
		case R.id.button_save: {
			Details details = new Details();
			details.setName(getEditText(R.id.edittext_name));
			details.setAddress(getEditText(R.id.edittext_address));
			details.setCondOverall(getSpinner(R.id.spinner_overall));
			details.setCondKitchen(getSpinner(R.id.spinner_kitchen));
			details.setCondToilet(getSpinner(R.id.spinner_toilet));
			details.setComments(getEditText(R.id.edittext_comments));

			if (mDetailsId == -1) {
				mDetailsId = DetailsDataSource.getInstance(getActivity())
						.insertDetails(details);
			} else {
				details.setId(mDetailsId);
				DetailsDataSource.getInstance(getActivity()).updateDetails(
						details);
			}

			try {
				FileOutputStream fos = new FileOutputStream(new File(
						Constants.PHOTO_DIR, Constants.PHOTO_PREFIX
								+ mDetailsId));
				fos.write(mPhotoData);
				fos.flush();
				fos.close();
			} catch (Exception ex) {
			}

			break;
		}
		case R.id.button_delete: {
			Bundle args = new Bundle();
			args.putLong(Constants.ARG_ID, mDetailsId);
			DeleteDialogFragment fragment = new DeleteDialogFragment();
			fragment.setArguments(args);
			fragment.show(getFragmentManager(), "dialog");
			break;
		}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (savedInstanceState != null) {
			mDetailsId = savedInstanceState.getLong(Constants.ARG_ID);
			mPhotoData = savedInstanceState.getByteArray(Constants.ARG_PHOTO);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_details, container,
				false);

		view.findViewById(R.id.button_take_photo).setOnClickListener(this);
		view.findViewById(R.id.button_save).setOnClickListener(this);
		view.findViewById(R.id.button_delete).setOnClickListener(this);

		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (getView() != null) {
							getView().getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
							setSpinnerStyle(R.id.spinner_overall);
							setSpinnerStyle(R.id.spinner_kitchen);
							setSpinnerStyle(R.id.spinner_toilet);
							updatePhoto(mPhotoData);
						}
					}
				});

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putByteArray(Constants.ARG_PHOTO, mPhotoData);
		outState.putLong(Constants.ARG_ID, mDetailsId);
	}

	/**
	 * Helper method for reading file contents into byte array.
	 */
	private byte[] readFile(File file) {
		try {
			int read;
			byte[] buffer = new byte[65536];
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			while ((read = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, read);
			}
			fis.close();
			return bos.toByteArray();
		} catch (Exception ex) {
			Toast.makeText(getActivity(), "Unable to read photo.",
					Toast.LENGTH_LONG).show();
			return null;
		}
	}

	/**
	 * Updates this fragment's details id.
	 */
	public void setDetailsId(long id) {
		mDetailsId = id;

		// TODO: If getView() returns null this method will be called second
		// time from onActivityCreated method.
		if (getView() == null) {
			return;
		}

		Details details;
		if (id == -1) {
			details = new Details();
		} else {
			details = DetailsDataSource.getInstance(getActivity()).getDetails(
					id);
			mPhotoData = readFile(new File(Constants.PHOTO_DIR,
					Constants.PHOTO_PREFIX + id));
		}

		setEditText(R.id.edittext_name, details.getName());
		setEditText(R.id.edittext_address, details.getAddress());
		setSpinner(R.id.spinner_overall, details.getCondOverall());
		setSpinner(R.id.spinner_kitchen, details.getCondKitchen());
		setSpinner(R.id.spinner_toilet, details.getCondToilet());
		setEditText(R.id.edittext_comments, details.getComments());
	}

	/**
	 * Helper method for setting EditText text.
	 */
	private void setEditText(int id, CharSequence text) {
		((EditText) getView().findViewById(id)).setText(text);
	}

	/**
	 * Helper method for setting spinner position.
	 */
	private void setSpinner(int id, int pos) {
		((Spinner) getView().findViewById(id)).setSelection(pos);
	}

	/**
	 * Updates spinner style for given spinner.
	 */
	private void setSpinnerStyle(int id) {
		Spinner spinner = (Spinner) getView().findViewById(id);
		((TextView) spinner.findViewById(android.R.id.text1)).setTextSize(20f);
		((ArrayAdapter<?>) spinner.getAdapter())
				.setDropDownViewResource(R.layout.spinner_item);
	}

	/**
	 * Updates photo ImageView with given data.
	 */
	private void updatePhoto(byte[] data) {
		ImageView imageView = (ImageView) getView()
				.findViewById(R.id.imageview);
		imageView.setImageBitmap(null);
		int viewWidth = getView().getWidth();

		if (data != null && viewWidth != 0) {
			BitmapFactory.Options bounds = new BitmapFactory.Options();
			bounds.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, bounds);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = bounds.outWidth / viewWidth;

			imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0,
					data.length, options));
		}
	}

}
