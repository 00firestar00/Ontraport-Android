package com.ontraport.mobileapp.main.record.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.collection.CollectionInfo;
import com.ontraport.mobileapp.main.collection.asynctasks.GetListAsyncTask;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.mobileapp.main.record.asynctasks.UpdateAsyncTask;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.objects.ObjectType;

import java.util.HashMap;
import java.util.Map;

public class ParentViewHolder extends DropDownViewHolder {

    private ParentCollectionAdapter adapter;
    private String first_value;
    private int parent_id;
    private Map<String, Integer> true_values = new HashMap<>();

    public ParentViewHolder(View view) {
        super(view);
        adapter = getNewAdapter();
        getDropDown().setAdapter(adapter);
    }

    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        first_value = field_value;
        if (meta_field.hasParent()) {
            adapter.add(first_value);
            setDefaultValue(0);

            parent_id = Integer.parseInt(meta_field.getParent());

            String[] list_fields = FieldUtils.getParentLabelListFields(ObjectType.valueOf(parent_id));

            RequestParams parent_params = new RequestParams();
            parent_params.put(FieldUtils.OBJECT_ID, parent_id);
            parent_params.put("listFields", TextUtils.join(",", list_fields));
            new GetListAsyncTask<>(adapter,
                    list_fields,
                    parent_id).execute(parent_params);
        }
    }

    @Override
    public ParentCollectionAdapter getNewAdapter() {
        return new ParentCollectionAdapter(view.getContext(),
                R.layout.record_spinner,
                R.id.spinnerText);
    }

    @Override
    protected void doUpdate(String field, String new_val) {
        if (!new_val.equals(old_val)) {

            int set_val = true_values.get(new_val);
            if (!Integer.toString(set_val).equals(old_val)) {
                super.doUpdate(field, Integer.toString(set_val));
                old_val = new_val;
                params.put(field, set_val);
                new UpdateAsyncTask(view.getContext()).execute(params);
            }
        }
    }

    class ParentCollectionAdapter extends ArrayAdapter<String> implements AsyncAdapter<CollectionInfo> {

        ParentCollectionAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override
        public void handleNullResponse() {

        }

        @Override
        public void updateInfo(CollectionInfo collection) {
            // Clear out the placeholder value
            clear();
            if (FieldUtils.allowsEmptyValues(ObjectType.valueOf(parent_id))) {
                add("");
            }
            // Add the new values
            addAll(collection.getDataValues());
            for (RecordInfo record : collection.getData()) {
                true_values.put(record.toString(), record.getId());
                if (record.getId() == Integer.parseInt(first_value)) {
                    setDefaultValue(getPosition(record.toString()));
                }
            }
        }

        @Override
        public void updateParentInfo(CollectionInfo collection) {
            throw new UnsupportedOperationException();
        }
    }
}
