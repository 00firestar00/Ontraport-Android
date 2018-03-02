package com.ontraport.mobileapp.adapters.views;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.R;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
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
        populateDropdown(key, value);
    }

    public void populateDropdown(String field, String value) {
        int object_id = params.getAsInt("objectID");
        Meta.Data object_meta = OntraportApplication.getInstance().getMetaData(object_id);
        Meta.Field meta_field = object_meta.getFields().get(field);

        if (meta_field.hasOptions()) {
            // Drop or list
            if (meta_field.getType().equals("drop")) {
                Map<String, String> options = meta_field.getOptions();
                ArrayList<String> values = new ArrayList<>(options.values());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                        R.layout.record_spinner,
                        R.id.spinnerText,
                        values);
                drop_down.setAdapter(adapter);
                drop_down.setSelection(values.indexOf(value));
            }
        }
        if (meta_field.hasParent()) {
            int parent_id = Integer.parseInt(meta_field.getParent());
        }
    }
}
