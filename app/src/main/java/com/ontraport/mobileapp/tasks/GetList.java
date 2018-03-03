package com.ontraport.mobileapp.tasks;

import android.text.TextUtils;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.mobileapp.adapters.CollectionInfo;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ListResponse;
import com.ontraport.sdk.http.RequestParams;

import java.util.Arrays;

public class GetList extends AbstractTask<CollectionAdapter, ListResponse> {

    private String[] list_fields;

    public GetList(CollectionAdapter adapter, String[] list_fields) {
        super(adapter);
        this.list_fields = list_fields;
    }

    @Override
    protected void onPostExecute(ListResponse list) {
        super.onPostExecute(list);
        adapter.updateInfo(new CollectionInfo(list.getData(), list_fields));
    }

    @Override
    public ListResponse background(RequestParams params) throws RequiredParamsException {
        String list = TextUtils.join(",", list_fields);
        if (!Arrays.asList(list_fields).contains("id")) {
            list += ",id";
        }
        if (Arrays.asList(list_fields).contains("fn")) {
            list += ",firstname,lastname";
        }
        params.put("listFields", list);
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().retrieveMultiple(params);
    }
}
