package fi.harism.fragments;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteDialogFragment extends DialogFragment {

	private long mDetailsId = -1;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Confirm deletetion");
		builder.setMessage("Are you sure you want to delete this item?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DetailsDataSource.getInstance(getActivity()).deleteDetails(
						mDetailsId);
				new File(Constants.PHOTO_DIR, Constants.PHOTO_PREFIX
						+ mDetailsId).delete();

				Fragment itemsFragment = getFragmentManager().findFragmentById(
						R.id.fragment_items);
				Fragment detailsFragment = getFragmentManager()
						.findFragmentById(R.id.fragment_details);

				if (itemsFragment != null) {
					// TODO: Implement me
					// getFragmentManager().beginTransaction().remove(detailsFragment).commit();
				} else {
					getActivity().finish();
				}
			}
		});
		builder.setNegativeButton("NO", null);
		return builder.create();
	}

	public void setDetailsId(long id) {
		mDetailsId = id;
	}

}
