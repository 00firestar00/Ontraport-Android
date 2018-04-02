package com.ontraport.mobileapp.main.collection.asynctasks;

import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.MessageResponse;
import com.ontraport.sdk.http.RequestParams;

public class SubscribeAsyncTask extends AbstractAsyncTask<CollectionAdapter,MessageResponse> {

    private RequestParams params;

    public SubscribeAsyncTask(CollectionAdapter adapter, RequestParams params) {
        super(adapter);
        this.params = params;
    }

    @Override
    protected void onPostExecute(MessageResponse info) {
        super.onPostExecute(info);
    }

    @Override
    public MessageResponse background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().subscribe(params);
    }
}
