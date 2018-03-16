package com.ontraport.mobileapp.main.record.views;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.sdk.http.RequestParams;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    public final View view;
    protected String old_val;
    protected RequestParams params;

    public RecordViewHolder(View view) {
        super(view);
        this.view = view;
    }

    @CallSuper
    public void setText(String key, String value, String alias) {
        old_val = value;
    }

    public void setParams(int object_id, int id) {
        params = new RequestParams();
        params.put(Constants.OBJECT_TYPE_ID, object_id);
        params.put("id", id);
    }

}
