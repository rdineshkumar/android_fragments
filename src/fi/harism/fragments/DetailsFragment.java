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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class DetailsFragment extends Fragment implements View.OnClickListener {

	private static final int ACTION_CODE = 100;
	private long mDetailsId = -1;
	private byte[] mPhotoData;

	private String getEditText(int id) {
		return ((EditText) getView().findViewById(id)).getText().toString();
	}

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
			updatePhoto();
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
			DeleteDialogFragment fragment = new DeleteDialogFragment();
			fragment.setDetailsId(mDetailsId);
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
							updatePhoto();
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

	private byte[] readFile(File file) {
		try {
			int read;
			byte[] buffer = new byte[8192];
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

	public void setDetailsId(long id) {
		mDetailsId = id;

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

	private void setEditText(int id, CharSequence text) {
		((EditText) getView().findViewById(id)).setText(text);
	}

	private void setSpinner(int id, int pos) {
		((Spinner) getView().findViewById(id)).setSelection(pos);
	}

	private void updatePhoto() {
		ImageView imageView = (ImageView) getView()
				.findViewById(R.id.imageview);
		imageView.setImageBitmap(null);
		int viewWidth = getView().getWidth();

		if (mPhotoData != null && viewWidth != 0) {
			BitmapFactory.Options bounds = new BitmapFactory.Options();
			bounds.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(mPhotoData, 0, mPhotoData.length,
					bounds);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = bounds.outWidth / viewWidth;

			imageView.setImageBitmap(BitmapFactory.decodeByteArray(mPhotoData,
					0, mPhotoData.length, options));
		}
	}

}
