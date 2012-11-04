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

	private String getEditText(int id) {
		return ((EditText) getView().findViewById(id)).getText().toString();
	}

	private int getSpinner(int id) {
		return ((Spinner) getView().findViewById(id)).getSelectedItemPosition();
	}

	private boolean isDualPane() {
		return getFragmentManager().findFragmentById(R.id.fragment_items) != null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_CODE && resultCode == Activity.RESULT_OK) {
			getArguments().putByteArray(Constants.ARG_PHOTO,
					readFile(Constants.PHOTO_TEMP));
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

			long id = getArguments().getLong(Constants.ARG_ID, -1);
			if (id == -1) {
				id = DetailsDataSource.getInstance(getActivity())
						.insertDetails(details);
				getArguments().putLong(Constants.ARG_ID, id);
			} else {
				details.setId(id);
				DetailsDataSource.getInstance(getActivity()).updateDetails(
						details);
			}

			try {
				FileOutputStream fos = new FileOutputStream(new File(
						Constants.PHOTO_DIR, Constants.PHOTO_PREFIX + id));
				fos.write(getArguments().getByteArray(Constants.ARG_PHOTO));
				fos.flush();
				fos.close();
			} catch (Exception ex) {
			}

			break;
		}
		case R.id.button_delete: {
			long id = getArguments().getLong(Constants.ARG_ID);
			DetailsDataSource.getInstance(getActivity()).deleteDetails(id);
			new File(Constants.PHOTO_DIR, Constants.PHOTO_PREFIX + id).delete();

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
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);

		Bundle args = getArguments();
		if (!args.getBoolean(Constants.ARG_KEEP)) {
			Details details;
			long id = getArguments().getLong(Constants.ARG_ID, -1);
			if (id == -1) {
				details = new Details();
			} else {
				details = DetailsDataSource.getInstance(getActivity())
						.getDetails(id);
				args.putByteArray(Constants.ARG_PHOTO, readFile(new File(
						Constants.PHOTO_DIR, Constants.PHOTO_PREFIX + id)));
			}

			args.putString(Constants.ARG_NAME, details.getName());
			args.putString(Constants.ARG_ADDRESS, details.getAddress());
			args.putInt(Constants.ARG_COND_OVERALL, details.getCondOverall());
			args.putInt(Constants.ARG_COND_KITCHEN, details.getCondKitchen());
			args.putInt(Constants.ARG_COND_TOILET, details.getCondToilet());
			args.putString(Constants.ARG_COMMENTS, details.getComments());
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

		Bundle args = getArguments();
		setEditText(view, R.id.edittext_name,
				args.getString(Constants.ARG_NAME));
		setEditText(view, R.id.edittext_address,
				args.getString(Constants.ARG_ADDRESS));
		setSpinner(view, R.id.spinner_overall,
				args.getInt(Constants.ARG_COND_OVERALL));
		setSpinner(view, R.id.spinner_kitchen,
				args.getInt(Constants.ARG_COND_KITCHEN));
		setSpinner(view, R.id.spinner_toilet,
				args.getInt(Constants.ARG_COND_TOILET));
		setEditText(view, R.id.edittext_comments,
				args.getString(Constants.ARG_COMMENTS));

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
	public void onPause() {
		super.onPause();

		Bundle args = getArguments();
		args.putString(Constants.ARG_NAME, getEditText(R.id.edittext_name));
		args.putString(Constants.ARG_ADDRESS,
				getEditText(R.id.edittext_address));
		args.putInt(Constants.ARG_COND_OVERALL,
				getSpinner(R.id.spinner_overall));
		args.putInt(Constants.ARG_COND_KITCHEN,
				getSpinner(R.id.spinner_kitchen));
		args.putInt(Constants.ARG_COND_TOILET, getSpinner(R.id.spinner_toilet));
		args.putString(Constants.ARG_COMMENTS,
				getEditText(R.id.edittext_comments));
		args.putByteArray(Constants.ARG_PHOTO,
				getArguments().getByteArray(Constants.ARG_PHOTO));
		args.putBoolean(Constants.ARG_KEEP, true);
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

	private void setEditText(View view, int id, CharSequence text) {
		((EditText) view.findViewById(id)).setText(text);
	}

	private void setSpinner(View view, int id, int pos) {
		((Spinner) view.findViewById(id)).setSelection(pos);
	}

	private void updatePhoto() {
		ImageView imageView = (ImageView) getView()
				.findViewById(R.id.imageview);
		imageView.setImageBitmap(null);
		int viewWidth = getView().getWidth();
		byte[] imageData = getArguments().getByteArray(Constants.ARG_PHOTO);

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
