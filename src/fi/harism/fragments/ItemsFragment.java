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

/**
 * List fragment containing list of details in database.
 */
public class ItemsFragment extends ListFragment implements
		View.OnClickListener, DetailsDataSource.Observer {

	// Local instance of data source.
	private DetailsDataSource mDataSource;

	/**
	 * Helper method to determine whether we are in dual pane mode.
	 */
	private boolean isDualPane() {
		View container = getActivity().findViewById(R.id.details_container);
		return container != null && container.getVisibility() == View.VISIBLE;
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
			// Open new empty details fragment.
			DetailsFragment detailsFragment = new DetailsFragment();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.animator.in, R.animator.out);
			ft.replace(R.id.details_container, detailsFragment, "details");
			ft.commit();
		} else {
			// Start new empty details activity.
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);
			intent.putExtra(Constants.ARG_ID, -1l);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in, R.anim.keep);
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
		View view = inflater.inflate(R.layout.fragment_items, container, false);

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
			Bundle args = new Bundle();
			args.putLong(Constants.ARG_ID, details.getId());
			DetailsFragment detailsFragment = new DetailsFragment();
			detailsFragment.setArguments(args);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.setCustomAnimations(R.animator.in, R.animator.out);
			ft.replace(R.id.details_container, detailsFragment, "details");
			ft.commit();
		} else {
			Intent intent = new Intent();
			intent.setClass(getActivity(), DetailsActivity.class);
			intent.putExtra(Constants.ARG_ID, details.getId());
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.in, R.anim.keep);
		}
	}

	/**
	 * Private adapter class for ListView.
	 */
	private class DetailsAdapter extends ArrayAdapter<Details> {

		public DetailsAdapter(Context context, List<Details> objects) {
			super(context, R.layout.fragment_items_item, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Details details = getItem(position);

			View view = getActivity().getLayoutInflater().inflate(
					R.layout.fragment_items_item, null, false);

			((TextView) view.findViewById(R.id.textview_name)).setText(details
					.getName());
			((TextView) view.findViewById(R.id.textview_address))
					.setText(details.getAddress());

			return view;
		}
	}

}
