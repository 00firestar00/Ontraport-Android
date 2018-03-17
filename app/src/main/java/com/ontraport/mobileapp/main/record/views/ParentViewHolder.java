package com.ontraport.mobileapp.main.record.views;

import android.view.View;
import com.ontraport.sdk.http.Meta;

public class ParentViewHolder extends DropDownViewHolder {

    public ParentViewHolder(View view) {
        super(view);
    }

    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasParent()) {
            int parent_id = Integer.parseInt(meta_field.getParent());
//            new GetListAsyncTask(getAdapter(),
//                    list_fields,
//                    parent_id).execute(params);
        }
    }
}
