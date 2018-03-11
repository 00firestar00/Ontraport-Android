package com.ontraport.mobileapp.tasks;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import com.ontraport.mobileapp.adapters.AsyncAdapter;
import com.ontraport.mobileapp.sdk.http.NullResponseException;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.AbstractResponse;
import com.ontraport.sdk.http.RequestParams;

public abstract class AbstractTask<A extends AsyncAdapter, R extends AbstractResponse>
        extends AsyncTask<RequestParams, Void, R> {

    protected A adapter;
    protected NullResponseException exception;

    public AbstractTask(A adapter) {
        this.adapter = adapter;
    }

    @Override
    @CallSuper
    protected void onPostExecute(R list) {
        super.onPostExecute(list);
        if (exception != null) {
            adapter.handleNullResponse();
        }
    }

    @Override
    protected R doInBackground(RequestParams... params) {
        try {
            RequestParams first = params[0];
            return background(first);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        catch (NullResponseException e) {
            exception = e;
        }
        exception = new NullResponseException();
        return null;
    }

    public abstract R background(RequestParams params) throws RequiredParamsException;

}
