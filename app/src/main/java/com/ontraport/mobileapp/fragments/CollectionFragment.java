package com.ontraport.mobileapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.MainActivity;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.mobileapp.listeners.EndlessScrollListener;
import com.ontraport.sdk.http.RequestParams;

import java.util.Map;

public class CollectionFragment extends SelectableListFragment<CollectionAdapter>
        implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ActionMode.Callback {

    private MainActivity activity;
    private SwipeRefreshLayout swipe_layout;
    private RequestParams params = new RequestParams();
    private RequestParams endless = new RequestParams();
    private int object_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.collection_fragment, container, false);

        Bundle bundle = getArguments();
        object_id = bundle != null ? bundle.getInt("objectID", 0) : 0;
        params.put("objectID", object_id);
        endless.putAll(params);
        activity = (MainActivity) getActivity();
        activity.onCollectionFragmentSetTitle(params.getAsInt("objectID"));

        FloatingActionButton fab = root_view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        swipe_layout = root_view.findViewById(R.id.swipeContainer);
        swipe_layout.setOnRefreshListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        RecyclerView recycler_view = setRecyclerView(root_view, new CollectionAdapter(object_id, params, activity), manager);
        recycler_view.addOnScrollListener(new EndlessScrollListener(manager) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                int next_count = --page * 50;
                if (next_count >= adapter.getMaxCount() + 50) {
                    return false;
                }
                System.out.println("getting more data");
                endless.put("start", next_count);
                OntraportApplication.getInstance().getCollection(adapter, endless);
                return true;
            }
        });

        OntraportApplication.getInstance().getCollection(adapter, params);
        return root_view;
    }

    @Override
    public void onClick(View view) {
        RecordFragment fragment = new RecordFragment();
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putInt("objectID", object_id);
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("record_" + object_id + "_" + "create")
                .commit();
    }

    @Override
    public void onRefresh() {
        OntraportApplication.getInstance().getCollection(adapter, params, true);
        swipe_layout.setRefreshing(false);
        if (action_mode != null) {
            action_mode.finish();
        }
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                SparseBooleanArray selected = adapter.getSelectedIds();

                for (int i = (selected.size() - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        Map<String, String> data = adapter.getDataAtPosition(selected.keyAt(i));
                        adapter.notifyDataSetChanged();
                    }
                }
                Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
            case R.id.action_copy:
                selected = adapter.getSelectedIds();
                int selectedMessageSize = selected.size();

                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        Map<String, String> data = adapter.getDataAtPosition(selected.keyAt(i));
                        Log.e("Selected Items", "Title - " + data.get("id") + "n" + "Sub Title - " + data.get("email"));
                    }
                }
                Toast.makeText(getContext(), "You selected Copy menu.", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
            case R.id.action_forward:
                Toast.makeText(getContext(), "You selected Forward menu.", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
        }
        return false;
    }
}
