package com.ontraport.app.ontraport.tasks;

import android.os.AsyncTask;
import com.ontraport.app.ontraport.OntraportApplication;
import com.ontraport.app.ontraport.adapters.RecordAdapter;
import com.ontraport.app.ontraport.http.NullResponseException;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;

public class GetOne extends AsyncTask<RequestParams, Void, SingleResponse> {

    private RecordAdapter adapter;
    private NullResponseException exception;

    public GetOne(RecordAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SingleResponse one) {
        super.onPostExecute(one);
        if (exception != null) {
            adapter.handleNullResponse();
            return;
        }
        adapter.updateInfo(one.getData());
    }

    @Override
    protected SingleResponse doInBackground(RequestParams... params) {
        try {
            RequestParams first = params[0];
            OntraportApplication ontraport_app = OntraportApplication.getInstance();
            return ontraport_app.getApi().objects().retrieveSingle(first);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        catch (NullResponseException e) {
            exception = e;
        }
        return new SingleResponse();
    }
}