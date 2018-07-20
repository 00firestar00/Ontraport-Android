package com.ontraport.mobileapp.main.record.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.record.RecordFragment;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.sdk.models.fieldeditor.ObjectSection;

public class SectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final RecordInfo record;
    private final FragmentManager manager;
    private final TextView title;
    private final TextView description;

    private ObjectSection section;

    public SectionViewHolder(View view, RecordInfo record, FragmentManager manager) {
        super(view);
        this.record = record;
        this.manager = manager;
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
    }

    public void bind(ObjectSection section) {
        this.section = section;
        title.setText(section.getName());
        description.setText(section.getDescription());
    }

    @Override
    public void onClick(View v) {

        RecordFragment fragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("record", record);
        fragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("record_section_" + section.getName())
                .commit();
    }
}
