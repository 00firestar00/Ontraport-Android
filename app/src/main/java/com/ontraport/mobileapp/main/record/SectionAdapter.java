package com.ontraport.mobileapp.main.record;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.record.views.CountryViewHolder;
import com.ontraport.mobileapp.main.record.views.DisabledViewHolder;
import com.ontraport.mobileapp.main.record.views.DropDownViewHolder;
import com.ontraport.mobileapp.main.record.views.EmailViewHolder;
import com.ontraport.mobileapp.main.record.views.ListViewHolder;
import com.ontraport.mobileapp.main.record.views.NumericViewHolder;
import com.ontraport.mobileapp.main.record.views.ParentViewHolder;
import com.ontraport.mobileapp.main.record.views.PhoneViewHolder;
import com.ontraport.mobileapp.main.record.views.RecordViewHolder;
import com.ontraport.mobileapp.main.record.views.StateViewHolder;
import com.ontraport.mobileapp.main.record.views.TextViewHolder;
import com.ontraport.mobileapp.main.record.views.TimestampViewHolder;
import com.ontraport.mobileapp.main.record.views.UrlViewHolder;
import com.ontraport.mobileapp.utils.FieldType;

public class SectionAdapter extends RecyclerView.Adapter<RecordViewHolder>
        implements AsyncAdapter<RecordInfo> {

    private final AppCompatActivity activity;
    private final FragmentManager fragment_manager;
    private final OntraportApplication application;
    private final int section_id;
    private RecordInfo record;

    SectionAdapter(AppCompatActivity activity, int section_id) {
        this.activity = activity;
        this.section_id = section_id;
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
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @FieldType int type) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card, parent, false);

        View drop = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card_drop, parent, false);

        View list = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card_list, parent, false);

        switch (type) {
            case FieldType.DISABLED:
                return new DisabledViewHolder(view);
            case FieldType.PHONE:
                return new PhoneViewHolder(view);
            case FieldType.TIMESTAMP:
            case FieldType.FULLDATE:
                return new TimestampViewHolder(view);
            case FieldType.URL:
                return new UrlViewHolder(view);
            case FieldType.PRICE:
            case FieldType.NUMERIC:
                return new NumericViewHolder(view);
            case FieldType.EMAIL:
                return new EmailViewHolder(view);
            case FieldType.COUNTRY:
                return new CountryViewHolder(drop);
            case FieldType.STATE:
                return new StateViewHolder(drop);
            case FieldType.DROP:
                return new DropDownViewHolder(drop);
            case FieldType.SUBSCRIPTION:
            case FieldType.LIST:
                return new ListViewHolder(list);
            case FieldType.PARENT:
                return new ParentViewHolder(drop);
            case FieldType.MERGEFIELD:
            case FieldType.TEXT:
            case FieldType.ADDRESS:
            case FieldType.CHECK:
            case FieldType.LONGTEXT:
            case FieldType.SMS:
            case FieldType.TIMEZONE:
            default:
                return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        if (record == null) {
            return;
        }

        RecordInfo.RecordSection record_section = record.getSection(section_id);
        RecordInfo.RecordField record_field = record_section.getField(position);

        String key = record_field.getField();
        String alias = record_field.getAlias();
        String value = record_field.getValue();

        holder.setParams(record.getObjectId(), record.getId());
        holder.setText(key, value, alias);
    }

    @Override
    public @FieldType
    int getItemViewType(int position) {
        RecordInfo.RecordSection record_section = record.getSection(section_id);
        RecordInfo.RecordField record_field = record_section.getField(position);
        return record_field.getFieldType();
    }

    @Override
    public int getItemCount() {
        return record == null ? 0 : record.getSection(section_id).size();
    }
}
