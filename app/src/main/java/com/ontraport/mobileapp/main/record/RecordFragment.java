package com.ontraport.mobileapp.main.record;

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
import com.ontraport.mobileapp.main.MainActivity;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.exceptions.NullResponseException;
import com.ontraport.sdk.http.RequestParams;

public class RecordFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.record_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle == null || getActivity() == null) {
            return root_view;
        }
        int object_id = bundle.getInt(FieldUtils.OBJECT_ID, 0);
        String id = bundle.getString("id", null);
        RequestParams params = new RequestParams();
        params.put(FieldUtils.OBJECT_ID, object_id);

        RecordAdapter adapter = new RecordAdapter((MainActivity) getActivity());
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
