package com.ontraport.mobileapp.main.record.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListViewHolder extends DropDownViewHolder {

    private ArrayAdapter<String> adapter;
    private ListViewAdapter list_adapter;
    private ListView list_selection;

    public ListViewHolder(View view) {
        super(view);
        list_selection = view.findViewById(R.id.list_selection);
        list_adapter = new ListViewAdapter(view.getContext(), R.layout.record_list, R.id.listText);
        list_selection.setAdapter(list_adapter);

        adapter = getNewAdapter();
        getDropDown().setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1) {
            return;
        }
        getDropDown().setSelection(0);
        String new_value = getDropDown().getItemAtPosition(position).toString();
        list_adapter.add(new_value);
        adapter.remove(new_value);

        String field = (String) getDropDown().getTag();
        if (!new_value.equals(old_val)) {
            doUpdate(field, new_value);
        }
    }

    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasOptions()) {
            // Drop or list
            if (meta_field.getType().equals("list")) {
                Map<String, String> options = meta_field.getOptions();
                List<String> values = new ArrayList<>(options.values());
                adapter.clear();
                list_adapter.clear();
                adapter.add("Select...");
                adapter.addAll(values);
                populateList(FieldUtils.legacyListToArray(field_value), options);
            }
        }
    }

    private void populateList(String[] values, Map<String, String> options) {
        for (String val : values) {
            String str_val = options.get(val);
            if (str_val != null) {
                list_adapter.add(str_val);
                adapter.remove(str_val);
            }
        }
    }

    class ListViewAdapter extends ArrayAdapter<String> implements View.OnClickListener {

        ListViewAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @NonNull
        @Override
        public View getView(final int position, View convert_view, @NonNull ViewGroup parent) {
            View row = super.getView(position, convert_view, parent);
            Button remove = row.findViewById(R.id.remove);
            remove.setOnClickListener(this);
            return row;
        }


        @Override
        public void onClick(View v) {
            TextView text = ((View) v.getParent()).findViewById(R.id.listText);
            String to_remove = text.getText().toString();
            remove(to_remove);
            adapter.add(to_remove);
        }
    }

}
