package com.ontraport.mobileapp.tasks;

import android.os.AsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.adapters.RecordAdapter;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;

public class Create extends AsyncTask<RequestParams, Void, SingleResponse> {

    private RecordAdapter adapter;

    public Create(RecordAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SingleResponse one) {
        super.onPostExecute(one);
        adapter.updateInfo(one.getData());
    }

    @Override
    protected SingleResponse doInBackground(RequestParams... params) {
        try {
            RequestParams first = params[0];
            OntraportApplication ontraport_app = OntraportApplication.getInstance();
            return ontraport_app.getApi().objects().create(first);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        return new SingleResponse();
    }
}
