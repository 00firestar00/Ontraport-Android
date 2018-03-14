package com.ontraport.mobileapp.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.adapters.views.DisabledViewHolder;
import com.ontraport.mobileapp.adapters.views.DropDownViewHolder;
import com.ontraport.mobileapp.adapters.views.EmailViewHolder;
import com.ontraport.mobileapp.adapters.views.NumericViewHolder;
import com.ontraport.mobileapp.adapters.views.PhoneViewHolder;
import com.ontraport.mobileapp.adapters.views.RecordViewHolder;
import com.ontraport.mobileapp.adapters.views.TextViewHolder;
import com.ontraport.mobileapp.adapters.views.TimestampViewHolder;
import com.ontraport.mobileapp.adapters.views.UrlViewHolder;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder>
        implements AsyncAdapter<RecordInfo> {

    private List<String> keys = new ArrayList<>();
    private List<String> aliases = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private FragmentManager fragment_manager;
    private OntraportApplication application;
    private int object_id;
    private int id;

    public RecordAdapter(int object_id, FragmentActivity activity) {
        this.fragment_manager = activity.getSupportFragmentManager();
        this.application = (OntraportApplication) activity.getApplication();
        this.object_id = object_id;
    }

    @Override
    public void updateInfo(RecordInfo info) {
        Map<String, String> data = info.getData();
        ArrayList<String> old_keys = new ArrayList<>(data.keySet());

        Meta.Data meta = application.getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();

        id = Integer.parseInt(data.get("id"));
        for (String key : old_keys) {
            String alias;
            try {
                alias = fields.get(key).getAlias();
            }
            catch (NullPointerException e) {
                System.out.println("Missing: " + key);
                data.remove(key);
                continue;
            }
            if (alias == null || alias.isEmpty()) {
                data.remove(key);
                continue;
            }

            if (key.equals("fn")) {
                String first = data.get("firstname") == null ? "" : data.get("firstname");
                String last = data.get("lastname") == null ? "" : data.get("lastname");
                data.put("fn", first + " " + last);
            }

            keys.add(key);
            aliases.add(alias);
            values.add(data.get(key) == null ? "" : data.get(key));
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
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @FieldType int type) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_card, parent, false);

        switch (type) {
            case FieldType.DISABLED:
                return new DisabledViewHolder(view);
            case FieldType.PHONE:
                return new PhoneViewHolder(view);
            case FieldType.TIMESTAMP:
                return new TimestampViewHolder(view);
            case FieldType.URL:
                return new UrlViewHolder(view);
            case FieldType.NUMERIC:
                return new NumericViewHolder(view);
            case FieldType.EMAIL:
                return new EmailViewHolder(view);
            case FieldType.DROP:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.record_card_drop, parent, false);
                return new DropDownViewHolder(view);
            case FieldType.LIST:
                return new TextViewHolder(view);
            case FieldType.PARENT:
                return new TextViewHolder(view);
            case FieldType.TEXT:
            default:
                return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        if (values == null) {
            return;
        }

        String key = keys.get(position);
        String alias = aliases.get(position);
        String value = values.get(position);

        holder.setParams(object_id, id);
        holder.setText(key, value, alias);
    }

    @FieldType
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
            return FieldType.TEXT;
        }

        switch (key) {
            case "id":
            case "dla":
            case "date":
            case "spent":
            case "dlm":
            case "ip_addy":
                return FieldType.DISABLED;
        }

        switch (type) {
            case "phone":
                return FieldType.PHONE;
            case "timestamp":
                return FieldType.TIMESTAMP;
            case "url":
                return FieldType.URL;
            case "numeric":
            case "price":
                return FieldType.NUMERIC;
            case "email":
                return FieldType.EMAIL;
            case "drop":
                return FieldType.DROP;
            case "list":
                return FieldType.LIST;
            case "parent":
                return FieldType.PARENT;
            case "text":
            default:
                return FieldType.TEXT;
        }
    }

    @Override
    public int getItemCount() {
        return keys == null ? 0 : keys.size();
    }
}
