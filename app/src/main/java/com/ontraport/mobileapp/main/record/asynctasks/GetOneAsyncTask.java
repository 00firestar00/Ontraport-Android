package com.ontraport.mobileapp.main.record.asynctasks;

import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.main.record.RecordAdapter;
import com.ontraport.mobileapp.main.record.RecordInfo;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;

public class GetOneAsyncTask extends AbstractAsyncTask<RecordAdapter, SingleResponse> {

    private int object_id;

    public GetOneAsyncTask(RecordAdapter adapter, int object_id) {
        super(adapter);
        this.object_id = object_id;
    }

    @Override
    protected void onPostExecute(SingleResponse one) {
        super.onPostExecute(one);
        adapter.updateInfo(new RecordInfo(object_id, one.getData()));
    }

    @Override
    public SingleResponse background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().retrieveSingle(params);
    }
}
