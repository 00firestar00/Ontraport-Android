package com.ontraport.mobileapp.tasks;

import android.os.AsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.adapters.RecordAdapter;
import com.ontraport.mobileapp.http.NullResponseException;
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
