package com.ontraport.mobileapp.tasks;

import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.adapters.RecordAdapter;
import com.ontraport.mobileapp.adapters.RecordInfo;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;

public class Create extends AbstractTask<RecordAdapter, SingleResponse> {

    public Create(RecordAdapter adapter) {
        super(adapter);
    }

    @Override
    protected void onPostExecute(SingleResponse one) {
        super.onPostExecute(one);
        adapter.updateInfo(new RecordInfo(one.getData()));
    }

    @Override
    public SingleResponse background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().create(params);
    }
}
