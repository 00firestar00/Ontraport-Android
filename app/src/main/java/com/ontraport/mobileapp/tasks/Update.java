package com.ontraport.mobileapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.UpdateResponse;

public class Update extends AsyncTask<RequestParams, Void, UpdateResponse> {

    private final Context context;

    public Update(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(UpdateResponse one) {
        super.onPostExecute(one);
        Toast.makeText(context, "Updated", Toast.LENGTH_LONG).show();
    }

    @Override
    protected UpdateResponse doInBackground(RequestParams... params) {
        try {
            RequestParams first = params[0];
            OntraportApplication ontraport_app = OntraportApplication.getInstance();
            return ontraport_app.getApi().objects().update(first);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        return new UpdateResponse();
    }
}
