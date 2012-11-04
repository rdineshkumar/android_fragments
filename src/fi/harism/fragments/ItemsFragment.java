package fi.harism.fragments;

import java.util.List;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ItemsFragment extends ListFragment implements
		View.OnClickListener, DetailsDataSource.Observer {

	private DetailsDataSource mDataSource;

	private boolean isDualPane() {
		View details = getActivity().findViewById(R.id.details_container);
		return details != null && details.getVisibility() == View.VISIBLE;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (isDualPane()) {
			getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (isDualPane()) {
			Bundle args = new Bundle();
			args.putLong(Constants.ARG_ID, -1);

			DetailsFragment detailsFragment = new DetailsFragment();
			detailsFragment.setArguments(args);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.details_container, detailsFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		} else {
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);
			intent.putExtra(Constants.ARG_ID, -1l);
			startActivity(intent);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDataSource = DetailsDataSource.getInstance(getActivity());
		mDataSource.addObserver(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);

		view.findViewById(R.id.button_add).setOnClickListener(this);

		onDetailsChanged();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDataSource.removeObserver(this);
	}

	@Override
	public void onDetailsChanged() {
		List<Details> values = mDataSource.getAllDetails();
		DetailsAdapter adapter = new DetailsAdapter(getActivity(), values);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Details details = (Details) getListAdapter().getItem(position);

		if (isDualPane()) {
			getListView().setItemChecked(position, true);

			Bundle args = new Bundle();
			args.putLong(Constants.ARG_ID, details.getId());
			DetailsFragment detailsFragment = new DetailsFragment();
			detailsFragment.setArguments(args);

			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.details_container, detailsFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		} else {
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);
			intent.putExtra(Constants.ARG_ID, details.getId());
			startActivity(intent);
		}
	}

	private class DetailsAdapter extends ArrayAdapter<Details> {

		public DetailsAdapter(Context context, List<Details> objects) {
			super(context, R.layout.fragment_list_item, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Details details = getItem(position);

			View view = getActivity().getLayoutInflater().inflate(
					R.layout.fragment_list_item, null, false);

			((TextView) view.findViewById(R.id.textview_name)).setText(details
					.getName());
			((TextView) view.findViewById(R.id.textview_address))
					.setText(details.getAddress());

			return view;
		}

	}

}
