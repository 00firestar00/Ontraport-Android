package com.ontraport.app.ontraport.adapters;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ontraport.app.ontraport.OntraportApplication;
import com.ontraport.app.ontraport.R;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {

    private List<String> keys;
    private Map<String, String> data;
    private FragmentManager fragment_manager;
    private OntraportApplication application;
    private int object_id;

    public RecordAdapter(int object_id, FragmentActivity activity) {
        this.fragment_manager = activity.getSupportFragmentManager();
        this.application = (OntraportApplication) activity.getApplication();
        this.object_id = object_id;
    }

    public void updateInfo(Map<String, String> data) {
        this.data = data;
        keys = new ArrayList<>(data.keySet());
        notifyDataSetChanged();
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card, parent, false);

        switch (type) {
            case 1:
                return new DisabledViewHolder(view);
            case 2:
                return new PhoneViewHolder(view);
            case 3:
                return new TimestampViewHolder(view);
            case 4:
                return new UrlViewHolder(view);
            case 5:
                return new NumericViewHolder(view);
            case 6:
                return new EmailViewHolder(view);
            case 0:
            default:
                return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        if (data == null) {
            return;
        }

        Meta.Data meta = application.getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();
        String key = keys.get(position);
        String alias;
        try {
            alias = fields.get(key).getAlias();
        }
        catch (NullPointerException e) {
            System.out.println("Missing: " + key);
            return;
        }
        if (alias == null || alias.isEmpty()) {
            return;
        }

        String value = data.get(key) == null ? "" : data.get(key);
        if (key.equals("fn")) {
            String first = data.get("firstname") == null ? "" : data.get("firstname");
            String last = data.get("lastname") == null ? "" : data.get("lastname");
            value = first + " " + last;
        }

        holder.setText(key, value, alias);
    }

    @Override
    public int getItemViewType(int position) {
        Meta.Data meta = application.getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();
        String key = keys.get(position);
        String type;
        try {
            type = fields.get(key).getType();
        }
        catch (NullPointerException e) {
            System.out.println("Missing: " + key);
            return 0;
        }

        switch (key) {
            case "id":
            case "dla":
            case "date":
            case "spent":
            case "dlm":
            case "ip_addy":
                return 1;
        }

        switch (type) {
            case "phone":
                return 2;
            case "timestamp":
                return 3;
            case "url":
                return 4;
            case "numeric":
            case "price":
                return 5;
            case "email":
                return 6;
            case "text":
            default:
                return 0;
        }
    }

    @Override
    public int getItemCount() {
        return keys == null ? 0 : keys.size();
    }
}
