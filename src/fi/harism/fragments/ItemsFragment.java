package fi.harism.fragments;

import java.util.List;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemsFragment extends ListFragment implements
		DetailsDataSource.Observer {

	private DetailsDataSource mDataSource;
	private boolean mDualPane;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View details = getActivity().findViewById(R.id.details_container);
		mDualPane = details != null && details.getVisibility() == View.VISIBLE;
		if (mDualPane) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
		ArrayAdapter<Details> adapter = new ArrayAdapter<Details>(
				getActivity(), android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Details details = (Details) getListAdapter().getItem(position);

		if (mDualPane) {
			getListView().setItemChecked(position, true);
			
			Bundle bundle = new Bundle();
			bundle.putLong("detailsId", details.getId());
			DetailsFragment detailsFragment = new DetailsFragment();
			detailsFragment.setArguments(bundle);			
			
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.details_container, detailsFragment);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		} else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra("detailsId", details.getId());
            startActivity(intent);			
		}
	}

}
