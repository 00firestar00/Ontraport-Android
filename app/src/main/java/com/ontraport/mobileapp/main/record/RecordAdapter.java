package com.ontraport.mobileapp.main.record;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.record.views.SectionViewHolder;
import com.ontraport.mobileapp.utils.FieldType;
import com.ontraport.mobileapp.utils.SelectableItemAdapter;
import com.ontraport.sdk.models.fieldeditor.ObjectSection;

public class RecordAdapter extends SelectableItemAdapter<RecordInfo, SectionViewHolder>
        implements AsyncAdapter<RecordInfo> {

    private final AppCompatActivity activity;
    private final FragmentManager fragment_manager;
    private final OntraportApplication application;
    private RecordInfo record;

    RecordAdapter(AppCompatActivity activity) {
        this.activity = activity;
        this.fragment_manager = activity.getSupportFragmentManager();
        this.application = (OntraportApplication) activity.getApplication();
    }

    public void setTitle(RecordInfo record) {
        ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            String label = record.toString();
            int pos = label.lastIndexOf("(");
            if (pos < 0) {
                pos = 0;
            }
            ab.setTitle(label.substring(0, pos));
            ab.setSubtitle(label.substring(pos));
        }
    }

    @Override
    public void updateInfo(RecordInfo record) {
        this.record = record;
        setTitle(record);
        notifyDataSetChanged();
    }

    @Override
    public void updateParentInfo(RecordInfo record) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleNullResponse() {
        Toast.makeText(application, "Could not retrieve information", Toast.LENGTH_SHORT).show();
        fragment_manager.popBackStack();
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @FieldType int type) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_section_card, parent, false);

        return new SectionViewHolder(view, fragment_manager);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        if (record == null) {
            return;
        }
        ObjectSection section = OntraportApplication.getInstance()
                .getFieldSectionAtPosition(record.getObjectId(), position);
        holder.bind(position, section, record);
    }

    @Override
    public int getItemCount() {
        return record == null ? 0 : OntraportApplication.getInstance().getFieldSections(record.getObjectId()).size();
    }

    @Override
    public RecordInfo getDataAtPosition(int position) {
        return null;
    }
}
