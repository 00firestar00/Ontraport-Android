package com.ontraport.mobileapp.main.record.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.ontraport.mobileapp.R;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListViewHolder extends DropDownViewHolder {

    private ListViewAdapter adapter;
    private final ListView list_selection;

    public ListViewHolder(View view) {
        super(view);
        setFirstOptionText("Select...");
        list_selection = view.findViewById(R.id.list_selection);
        adapter = new ListViewAdapter(view.getContext(), R.layout.record_list, R.id.listText);
        list_selection.setAdapter(adapter);
    }

    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasOptions()) {
            // Drop or list
            if (meta_field.getType().equals("list")) {
                Map<String, String> options = meta_field.getOptions();
                List<String> values = new ArrayList<>(options.values());
                populateDropdown(values);
                populateList();
            }
        }
    }

    private void populateList() {
//id: 1000
//subActions[f1554][action]: add
//subActions[f1554][id]: 52
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
