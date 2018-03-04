package com.ontraport.mobileapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ontraport.mobileapp.MainActivity;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.sdk.http.RequestParams;

public class CollectionFragment extends Fragment
        implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private MainActivity activity;
    private CollectionAdapter adapter;
    private SwipeRefreshLayout swipe_layout;
    private RequestParams params = new RequestParams();
    private int object_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.collection_fragment, container, false);

        Bundle bundle = getArguments();
        object_id = bundle != null ? bundle.getInt("objectID", 0) : 0;
        params.put("objectID", object_id);

        activity = (MainActivity) getActivity();
        activity.onCollectionFragmentSetTitle(params.getAsInt("objectID"));

        FloatingActionButton fab = root_view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        swipe_layout = root_view.findViewById(R.id.swipeContainer);
        swipe_layout.setOnRefreshListener(this);

        adapter = new CollectionAdapter(object_id, params, activity);
        RecyclerView recyclerView = root_view.findViewById(R.id.collection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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
    }
}
