package com.ontraport.mobileapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.adapters.RecordAdapter;
import com.ontraport.mobileapp.http.NullResponseException;
import com.ontraport.sdk.http.RequestParams;

public class RecordFragments extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.record_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return root_view;
        }
        int object_id = bundle.getInt("objectID", 0);
        String id = bundle.getString("id", null);
        RequestParams params = new RequestParams();
        params.put("objectID", object_id);

        RecordAdapter adapter = new RecordAdapter(object_id, getActivity());
        RecyclerView recyclerView = root_view.findViewById(R.id.record);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        try {
            if (id == null) {
                OntraportApplication.getInstance().createRecord(adapter, params);
            }
            else {
                params.put("id", id);
                OntraportApplication.getInstance().getRecord(adapter, params);
            }
        }
        catch (NullResponseException e) {
            Toast.makeText(getContext(), "Could not load record.", Toast.LENGTH_SHORT).show();
        }

        return root_view;
    }
}
