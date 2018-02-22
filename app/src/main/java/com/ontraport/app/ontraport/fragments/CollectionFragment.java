package com.ontraport.app.ontraport.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class CollectionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.collection_fragment, container, false);

        Bundle bundle = getArguments();
        String object_id = bundle != null ? bundle.getString("objectID", "0") : "0";

        RequestParams params = new RequestParams();
        params.put("objectID", object_id);

        MainActivity activity = (MainActivity) getActivity();
        activity.onCollectionFragmentSetTitle(params.getAsInt("objectID"));
        FloatingActionButton fab = root_view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CollectionAdapter adapter = new CollectionAdapter(Integer.valueOf(object_id), params, getActivity());
        RecyclerView recyclerView = root_view.findViewById(R.id.collection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        OntraportApplication.getCollection(adapter, params);
        return root_view;
    }
}
