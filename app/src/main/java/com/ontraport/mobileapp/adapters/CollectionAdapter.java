package com.ontraport.mobileapp.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.SparseBooleanArray;
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

import java.util.Map;

public class CollectionAdapter extends SelectableItemAdapter
        implements AsyncAdapter<CollectionInfo> {

    private RequestParams params;
    private FragmentManager fragment_manager;
    private OntraportApplication application;
    private SparseBooleanArray selected_items;
    private Map<String, String>[] data;
    private String[] list_fields;
    private int object_id;

    public CollectionAdapter(int object_id, RequestParams params, FragmentActivity activity) {
        this.fragment_manager = activity.getSupportFragmentManager();
        this.application = (OntraportApplication) activity.getApplication();
        this.params = params;
        this.object_id = object_id;
        this.selected_items = new SparseBooleanArray();
    }

    @Override
    public void updateInfo(CollectionInfo info) {
        this.data = info.getData();
        this.list_fields = info.getListFields();
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

        return new CollectionViewHolder(view, params, fragment_manager, list_fields.length);
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
            String alias = fields.get(list_fields[i]).getAlias();
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
        return data == null ? 0 : data.length;
    }

    public Map<String, String> getDataAtPosition(int position) {
        return data[position];
    }
}
