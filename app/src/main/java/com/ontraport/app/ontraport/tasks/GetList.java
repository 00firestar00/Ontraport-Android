package com.ontraport.app.ontraport.tasks;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.ontraport.app.ontraport.OntraportApplication;
import com.ontraport.app.ontraport.adapters.CollectionAdapter;
import com.ontraport.app.ontraport.http.NullResponseException;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ListResponse;
import com.ontraport.sdk.http.RequestParams;

import java.util.Arrays;

public class GetList extends AsyncTask<RequestParams, Void, ListResponse> {

    private CollectionAdapter adapter;
    private String[] list_fields;
    private NullResponseException exception;

    public GetList(CollectionAdapter adapter, String[] list_fields) {
        this.adapter = adapter;
        this.list_fields = list_fields;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ListResponse list) {
        super.onPostExecute(list);
        if (exception != null) {
            adapter.handleNullResponse();
            return;
        }
        adapter.updateInfo(list.getData(), list_fields);
    }

    @Override
    protected ListResponse doInBackground(RequestParams... params) {
        try {
            RequestParams first = params[0];

            String list = TextUtils.join(",", list_fields);
            if (!Arrays.asList(list_fields).contains("id")) {
                list += ",id";
            }
            if (Arrays.asList(list_fields).contains("fn")) {
                list += ",firstname,lastname";
            }
            first.put("listFields", list);
            OntraportApplication ontraport_app = OntraportApplication.getInstance();
            return ontraport_app.getApi().objects().retrieveMultiple(first);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        catch (NullResponseException e) {
            exception = e;
        }
        return new ListResponse();
    }
}
