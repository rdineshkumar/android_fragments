package fi.harism.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FragmentSpinner extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_spinner, container,
				false);

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

		return view;
	}

}
