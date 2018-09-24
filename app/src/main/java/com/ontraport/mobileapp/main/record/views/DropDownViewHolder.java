package com.ontraport.mobileapp.main.record.views;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.record.asynctasks.UpdateAsyncTask;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropDownViewHolder extends RecordViewHolder implements AdapterView.OnItemSelectedListener {

    protected final Spinner drop_down;
    protected final TextView label;

    public DropDownViewHolder(View view) {
        super(view);
        label = view.findViewById(R.id.label);
        drop_down = view.findViewById(R.id.drop_down);
        drop_down.setOnItemSelectedListener(this);
    }

    @Override
    public void setText(String key, String value, String alias) {
        super.setText(key, value, alias);
        label.setText(alias);
        drop_down.setTag(key);
        fetchData(key, value);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String new_value = drop_down.getItemAtPosition(position).toString();
        if (old_val.equals("0") && new_value.isEmpty()) {
            new_value = "0";
        }
        String field = (String) drop_down.getTag();
        if (!new_value.equals(old_val)) {
            doUpdate(field, new_value);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // We do nothing.
    }

    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasOptions()) {
            // Drop or list
            if (meta_field.getType().equals("drop")) {
                Map<String, String> options = meta_field.getOptions();
                List<String> values = new ArrayList<>(options.values());
                populateDropdown(values);
                setDefaultValue(values.indexOf(options.get(field_value)) + 1);
            }
        }
    }

    public ArrayAdapter<String> getNewAdapter() {
        return new ArrayAdapter<>(view.getContext(),
                R.layout.record_spinner,
                R.id.spinnerText);
    }

    Spinner getDropDown() {
        return drop_down;
    }

    Meta.Field getMetaForField(String field) {
        int object_id = params.getAsInt(FieldUtils.OBJECT_ID);
        Meta.Data object_meta = OntraportApplication.getInstance().getMetaData(object_id);
        return object_meta.getFields().get(field);
    }

    void populateDropdown(List<String> values) {
        ArrayAdapter<String> adapter = getNewAdapter();
        adapter.add("");
        adapter.addAll(values);
        drop_down.setAdapter(adapter);
    }

    protected void setDefaultValue(int pos) {
        drop_down.setSelection(pos);
    }

    @Override
    protected void doUpdate(String field, String new_val) {
        Meta.Field meta_field = getMetaForField(field);
        if (!meta_field.hasOptions()) {
            return;
        }
        Map<String, String> options = meta_field.getOptions();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getValue().equals(new_val)) {
                new_val = entry.getKey();
                break;
            }
        }

        if (!new_val.equals(old_val)) {
            super.doUpdate(field, new_val);
            old_val = new_val;
            params.put(field, new_val);
            new UpdateAsyncTask(view.getContext()).execute(params);
        }
    }
}
