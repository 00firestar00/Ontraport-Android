package com.ontraport.mobileapp.main.record.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.R;
import com.ontraport.mobileapp.main.collection.CollectionInfo;
import com.ontraport.mobileapp.main.collection.asynctasks.GetListAsyncTask;
import com.ontraport.sdk.http.Meta;

import java.util.List;

public class ParentViewHolder extends DropDownViewHolder {

    public ParentViewHolder(View view) {
        super(view);
    }

    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasParent()) {
            int parent_id = Integer.parseInt(meta_field.getParent());
            new GetListAsyncTask<>(getAdapter(),
                    new String[]{},
                    parent_id).execute(params);
        }
    }

    @Override
    public ParentCollectionAdapter getAdapter() {
        return new ParentCollectionAdapter(view.getContext(),
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

    class ParentCollectionAdapter extends ArrayAdapter<String> implements AsyncAdapter<CollectionInfo> {

        ParentCollectionAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override
        public void handleNullResponse() {

        }

        @Override
        public void updateInfo(CollectionInfo collection) {
            addAll(collection.getDataValues());
        }
    }
}
