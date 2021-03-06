package com.ontraport.mobileapp.main.collection;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.MainActivity;
import com.ontraport.mobileapp.main.collection.views.CollectionViewHolder;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.mobileapp.utils.SelectableItemAdapter;
import com.ontraport.sdk.http.RequestParams;

import java.util.Map;

public class CollectionAdapter extends SelectableItemAdapter<RecordInfo, CollectionViewHolder>
        implements AsyncAdapter<CollectionInfo> {

    private final FragmentManager fragment_manager;
    private final MainActivity activity;
    private final OntraportApplication application;
    private final boolean has_record;

    private RequestParams params;
    private CollectionInfo collection = new CollectionInfo();

    @ColorInt
    private int theme;

    CollectionAdapter(RequestParams params, MainActivity activity, @ColorInt int theme, boolean has_record) {
        this.activity = activity;
        this.fragment_manager = activity.getSupportFragmentManager();
        this.application = (OntraportApplication) activity.getApplication();
        this.params = params;
        this.theme = theme;
        this.has_record = has_record;
    }

    int getMaxPages() {
        return (int) Math.ceil(collection.getCount() * 1.0 / 50);
    }

    public void setTitle(String string) {
        ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            ab.setTitle(string);
            ab.setSubtitle("Group: All");
        }
    }

    @Override
    public void updateInfo(CollectionInfo collection) {
        if (!collection.isForced() && !this.collection.isEmpty()) {
            System.out.println("Appending to adapter");
            this.collection.addAll(collection.getData());
        }
        else {
            this.collection = collection;
        }
        setTitle(this.collection.getCount() + " " + collection.getLabel());
        notifyDataSetChanged();
    }

    @Override
    public void updateParentInfo(CollectionInfo collection) {
        for (RecordInfo record : this.collection.getData()) {
            for (Map.Entry<String, String> entry : record.getParentIds().entrySet()) {
                try {
                    int parent_id = Integer.parseInt(entry.getValue());
                    if (parent_id == collection.getObjectId()) {
                        int current_val = Integer.parseInt(record.get(entry.getKey()));
                        RecordInfo parent_record = collection.getById(current_val);
                        if (parent_record != null) {
                            record.setValue(entry.getKey(), parent_record.toString());
                        }
                    }
                }
                catch (NumberFormatException ignored) {
                    // Already formatted.
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void handleNullResponse() {
        Toast.makeText(application, "Could not retrieve information", Toast.LENGTH_SHORT).show();
        fragment_manager.popBackStack();
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.collection_card, parent, false);

        return new CollectionViewHolder(view, params, fragment_manager, collection.getListFieldCount(), theme, has_record);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {

        RecordInfo data = getDataAtPosition(position);

        for (int i = 0; i < holder.getCount(); i++) {
            holder.setLabelText(i, data.getAlias(i));
            holder.setValueText(i, data.getValue(i));
        }

        holder.setBackgroundTintList(selected_items.get(position)
                ? application.getResources().getColorStateList(R.color.colorSelection)
                : ColorStateList.valueOf(Color.WHITE));

        holder.setTag(Integer.toString(data.getId()));
    }

    @Override
    public int getItemCount() {
        return collection == null ? 0 : collection.size();
    }

    @Override
    public RecordInfo getDataAtPosition(int position) {
        return collection.get(position);
    }

    public void removeAt(int position) {
        collection.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, collection.size());
    }
}
