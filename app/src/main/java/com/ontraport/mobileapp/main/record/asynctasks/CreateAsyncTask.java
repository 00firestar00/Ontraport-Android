package com.ontraport.mobileapp.main.record.asynctasks;

import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.main.record.RecordAdapter;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;

public class CreateAsyncTask extends AbstractAsyncTask<RecordAdapter, SingleResponse> {

    public CreateAsyncTask(RecordAdapter adapter) {
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
