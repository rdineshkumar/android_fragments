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

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Delete confirmation dialog.
 */
public class DeleteDialogFragment extends DialogFragment {

	/**
	 * Delete method.
	 */
	private void deleteDetails() {
		// Delete row from database.
		DetailsDataSource.getInstance(getActivity()).deleteDetails(
				getArguments().getLong(Constants.ARG_ID));
		// Delete photo file from file system.
		new File(Constants.PHOTO_DIR, Constants.PHOTO_PREFIX
				+ getArguments().getLong(Constants.ARG_ID)).delete();

		// Get fragments.
		Fragment itemsFragment = getFragmentManager()
				.findFragmentByTag("items");
		Fragment detailsFragment = getFragmentManager().findFragmentByTag(
				"details");

		// If items fragment != null we are in two pane mode.
		if (itemsFragment != null) {
			// Remove underlying container fragment.
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.animator.in, R.animator.out);
			ft.remove(detailsFragment);
			ft.commit();
		} else {
			// Finish activity.
			getActivity().finish();
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Create alert dialog.
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Confirm deletion");
		builder.setMessage("Are you sure you want to delete this item?");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteDetails();
			}
		});
		builder.setNegativeButton("NO", null);
		return builder.create();
	}

}
