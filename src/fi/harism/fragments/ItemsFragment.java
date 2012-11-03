package fi.harism.fragments;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemsFragment extends ListFragment implements
		DetailsDataSource.Observer {

	private DetailsDataSource mDataSource;
	private Observer mObserver;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mObserver = (Observer) activity;
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
		onDetailsChanged();
		return view;
	}

	@Override
	public void onDestroy() {
		mDataSource.removeObserver(this);
	}

	@Override
	public void onDetailsChanged() {
		List<Details> values = mDataSource.getAllDetails();
		ArrayAdapter<Details> adapter = new ArrayAdapter<Details>(
				getActivity(), android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Details details = (Details) getListAdapter().getItem(position);
		mObserver.onItemSelected(details.getId());
	}

	public interface Observer {
		public void onItemSelected(long detailsId);
	}
}
