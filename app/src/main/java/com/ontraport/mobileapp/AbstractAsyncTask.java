package com.ontraport.mobileapp;

import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import com.ontraport.sdk.exceptions.NullResponseException;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.AbstractResponse;
import com.ontraport.sdk.http.RequestParams;

public abstract class AbstractAsyncTask<A extends AsyncAdapter, R extends AbstractResponse>
        extends AsyncTask<RequestParams, Void, R> {

    protected A adapter;
    protected NullResponseException exception;

    public AbstractAsyncTask(A adapter) {
        this.adapter = adapter;
    }

    protected A getAdapter() {
        return adapter;
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
