package com.ontraport.mobileapp.main.collection.asynctasks;

import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;

public class DeleteAsyncTask extends AbstractAsyncTask<CollectionAdapter, SingleResponse> {

    private RequestParams params;

    public DeleteAsyncTask(CollectionAdapter adapter, RequestParams params) {
        super(adapter);
        this.params = params;
    }

    @Override
    protected void onPostExecute(SingleResponse info) {
        super.onPostExecute(info);
    }

    @Override
    public SingleResponse background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().deleteMultiple(params);
    }
}
