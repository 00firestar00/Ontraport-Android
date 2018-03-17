package com.ontraport.mobileapp.main.record.views;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DropDownViewHolder extends RecordViewHolder {

    protected final TextView label;
    protected final Spinner drop_down;

    public DropDownViewHolder(View view) {
        super(view);
        label = view.findViewById(R.id.label);
        drop_down = view.findViewById(R.id.dropDown1);
    }

    @Override
    public void setText(String key, String value, String alias) {
        super.setText(key, value, alias);
        label.setText(alias);
        drop_down.setTag(key);
        fetchData(key, value);
    }

    public Meta.Field getMetaForField(String field) {
        int object_id = params.getAsInt(Constants.OBJECT_TYPE_ID);
        Meta.Data object_meta = OntraportApplication.getInstance().getMetaData(object_id);
        return object_meta.getFields().get(field);
    }

    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasOptions()) {
            // Drop or list
            if (meta_field.getType().equals("drop")) {
                Map<String, String> options = meta_field.getOptions();
                List<String> values = new ArrayList<>(options.values());
                populateDropdown(values);
                setDefaultValue(values.indexOf(field_value));
            }
        }
    }

    public ArrayAdapter<String> getAdapter() {
        return new ArrayAdapter<>(view.getContext(),
                R.layout.record_spinner,
                R.id.spinnerText);
    }

    public void populateDropdown(List<String> values) {
        ArrayAdapter<String> adapter = getAdapter();
        adapter.addAll(values);
        drop_down.setAdapter(adapter);
    }

    public void setDefaultValue(int pos) {
        drop_down.setSelection(pos);
    }
}
