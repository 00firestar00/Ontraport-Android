package com.ontraport.mobileapp.main.record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.MainActivity;

public class SectionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.record_fragment, container, false);

        Bundle bundle = getArguments();
        if (bundle == null || getActivity() == null) {
            return root_view;
        }
        RecordInfo record = bundle.getParcelable("record");
        int section_id = bundle.getInt("section_id");

        SectionAdapter adapter = new SectionAdapter((MainActivity) getActivity(), section_id);
        RecyclerView recyclerView = root_view.findViewById(R.id.record);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.updateInfo(record);

        return root_view;
    }
}
