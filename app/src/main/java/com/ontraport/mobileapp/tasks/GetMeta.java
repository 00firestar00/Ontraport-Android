package com.ontraport.mobileapp.tasks;

import android.os.AsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

public class GetMeta extends AsyncTask<RequestParams, Void, Meta> {

    @Override
    protected void onPostExecute(Meta one) {
        super.onPostExecute(one);
    }

    @Override
    protected Meta doInBackground(RequestParams... params) {
        try {
            RequestParams first = params[0];
            OntraportApplication ontraport_app = OntraportApplication.getInstance();
            return ontraport_app.getApi().objects().retrieveMeta(first);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        return new Meta();
    }
}
