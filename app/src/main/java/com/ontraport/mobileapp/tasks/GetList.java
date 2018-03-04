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
    private boolean force_network = false;

    public GetList(CollectionAdapter adapter, String[] list_fields) {
        this(adapter, list_fields, false);
    }

    public GetList(CollectionAdapter adapter, String[] list_fields, boolean force_network) {
        super(adapter);
        this.force_network = force_network;
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
        if (force_network) {
            ontraport_app.getClient().forceNetwork();
        }
        return ontraport_app.getApi().objects().retrieveMultiple(params);
    }
}
