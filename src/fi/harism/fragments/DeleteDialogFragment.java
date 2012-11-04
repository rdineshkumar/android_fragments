package fi.harism.fragments;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Confirm deletetion");
		builder.setMessage("Are you sure you want to delete this item?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DetailsDataSource.getInstance(getActivity()).deleteDetails(
						getArguments().getLong(Constants.ARG_ID));
				new File(Constants.PHOTO_DIR, Constants.PHOTO_PREFIX
						+ getArguments().getLong(Constants.ARG_ID)).delete();

				Fragment itemsFragment = getFragmentManager()
						.findFragmentByTag("items");
				Fragment detailsFragment = getFragmentManager()
						.findFragmentByTag("details");

				if (itemsFragment != null) {
					DeleteDialogFragment.this.dismiss();
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.setCustomAnimations(R.animator.in, R.animator.out);
					ft.remove(detailsFragment);
					ft.commit();
				} else {
					getActivity().finish();
				}
			}
		});
		builder.setNegativeButton("NO", null);
		return builder.create();
	}

}
