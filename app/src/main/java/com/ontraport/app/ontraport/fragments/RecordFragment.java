package com.ontraport.app.ontraport.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ontraport.app.ontraport.OntraportApplication;
import com.ontraport.app.ontraport.R;
import com.ontraport.app.ontraport.adapters.RecordAdapter;
import com.ontraport.sdk.http.RequestParams;

public class RecordFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.record_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return root_view;
        }
        String object_id = bundle.getString("objectID", "0");
        RequestParams params = new RequestParams();
        params.put("objectID", object_id);
        params.put("id", bundle.getString("id"));

        RecordAdapter adapter = new RecordAdapter(Integer.valueOf(object_id), getActivity());
        RecyclerView recyclerView = root_view.findViewById(R.id.record);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        OntraportApplication.getRecord(adapter, params);

        return root_view;
    }
}
