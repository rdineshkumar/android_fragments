package fi.harism.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddDialogFragment extends DialogFragment {

	private MainActivity mMainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add, container, false);

		view.findViewById(R.id.button).requestFocus();
		view.findViewById(R.id.button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						EditText editName = (EditText) getView().findViewById(
								R.id.edittext_name);
						EditText editAddress = (EditText) getView()
								.findViewById(R.id.edittext_address);

						String name = editName.getText().toString();
						String address = editAddress.getText().toString();

						DetailsDataSource.getInstance(getActivity())
								.createDetails(name, address);
					}
				});

		Dialog dialog = getDialog();
		dialog.setTitle("Add new item:");

		return view;
	}

}
