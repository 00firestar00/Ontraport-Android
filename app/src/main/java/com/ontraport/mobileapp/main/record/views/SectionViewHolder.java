package com.ontraport.mobileapp.main.record.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.mobileapp.main.record.SectionFragment;
import com.ontraport.sdk.models.fieldeditor.ObjectSection;

public class SectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final FragmentManager manager;
    private final TextView title;
    private final TextView description;

    private RecordInfo record;
    private ObjectSection section;
    private int index;

    public SectionViewHolder(View view, FragmentManager manager) {
        super(view);
        this.manager = manager;
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        LinearLayout layout_view = view.findViewById(R.id.card_layout);
        layout_view.setOnClickListener(this);
    }

    public void bind(int index, ObjectSection section, RecordInfo record) {
        this.index = index;
        this.section = section;
        this.record = record;
        title.setText(section.getName());
        description.setText(section.getDescription());
    }

    @Override
    public void onClick(View v) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("record", record);
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        manager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("record_section_" + section.getName())
                .commit();
    }
}
