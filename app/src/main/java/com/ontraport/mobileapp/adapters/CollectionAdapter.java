package com.ontraport.mobileapp.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.adapters.views.CollectionViewHolder;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class CollectionAdapter extends SelectableItemAdapter
        implements AsyncAdapter<CollectionInfo> {

    private final FragmentManager fragment_manager;
    private final AppCompatActivity activity;
    private final OntraportApplication application;

    private RequestParams params;
    private ArrayList<Map<String, String>> data;
    private String[] list_fields;
    private int object_id;
    private int max_count;
    @ColorInt
    private int theme;

    public CollectionAdapter(int object_id, RequestParams params, AppCompatActivity activity, @ColorInt int theme) {
        this.fragment_manager = activity.getSupportFragmentManager();
        this.activity = activity;
        this.application = (OntraportApplication) activity.getApplication();
        this.params = params;
        this.object_id = object_id;
        this.theme = theme;
    }

    public int getMaxCount() {
        return max_count;
    }

    public int getMaxPages() {
        return (int) Math.ceil(getMaxCount() * 1.0 / 50);
    }

    public void setCountTitle(String string) {
        if (!string.toLowerCase().startsWith("count:")) {
            string = "Count: " + string;
        }
        activity.getSupportActionBar().setSubtitle(string);
    }

    @Override
    public void updateInfo(CollectionInfo info) {
        if (!info.isForced() && data != null && data.size() > 0) {
            System.out.println("Appending to adapter");
            this.data.addAll(Arrays.asList(info.getData()));
        }
        else {
            this.data = new ArrayList<>(Arrays.asList(info.getData()));
        }
        this.list_fields = sanitizeFields(info.getListFields());
        this.max_count = info.getCount();
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

        return new CollectionViewHolder(view, params, fragment_manager, list_fields.length, theme);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        if (data == null) {
            return;
        }

        Map<String, String> data = getDataAtPosition(position);
        Meta.Data meta = application.getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();

        for (int i = 0; i < holder.getCount(); i++) {
            Meta.Field field = fields.get(list_fields[i]);
            if (field == null) {
                continue;
            }
            String alias = field.getAlias();
            String key = list_fields[i];
            String value = data.get(key) == null ? "" : data.get(key);
            if (key.equals("fn")) {
                String first = data.get("firstname") == null ? "" : data.get("firstname");
                String last = data.get("lastname") == null ? "" : data.get("lastname");
                value = first + " " + last;
            }
            holder.setLabelText(i, alias);
            holder.setValueText(i, value);
        }

        holder.card.setBackgroundTintList(selected_items.get(position)
                ? application.getResources().getColorStateList(R.color.colorSelection)
                : ColorStateList.valueOf(Color.WHITE));

        LinearLayout layout_view = holder.view.findViewById(R.id.card_layout);
        layout_view.setTag(data.get("id"));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public Map<String, String> getDataAtPosition(int position) {
        return data.get(position);
    }

    private String[] sanitizeFields(String[] list_fields) {
        ArrayList<String> fields = new ArrayList<>(Arrays.asList(list_fields));
        fields.remove("");
        return fields.toArray(new String[fields.size()]);
    }
}
