package com.ontraport.app.ontraport.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ontraport.app.ontraport.MainActivity;
import com.ontraport.app.ontraport.OntraportApplication;
import com.ontraport.app.ontraport.R;
import com.ontraport.app.ontraport.adapters.CollectionAdapter;
import com.ontraport.sdk.http.RequestParams;

public class CollectionFragment extends Fragment implements View.OnClickListener {

    private MainActivity activity;
    private int object_id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.collection_fragment, container, false);

        Bundle bundle = getArguments();
        object_id = bundle != null ? bundle.getInt("objectID", 0) : 0;

        RequestParams params = new RequestParams();
        params.put("objectID", object_id);

        activity = (MainActivity) getActivity();
        activity.onCollectionFragmentSetTitle(params.getAsInt("objectID"));
        FloatingActionButton fab = root_view.findViewById(R.id.fab);
        fab.setOnClickListener(this);

        CollectionAdapter adapter = new CollectionAdapter(object_id, params, getActivity());
        RecyclerView recyclerView = root_view.findViewById(R.id.collection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        OntraportApplication.getCollection(adapter, params);
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
}
