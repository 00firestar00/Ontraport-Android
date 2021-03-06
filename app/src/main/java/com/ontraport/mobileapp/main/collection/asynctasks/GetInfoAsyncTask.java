package com.ontraport.mobileapp.main.collection.asynctasks;

import android.text.TextUtils;
import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ObjectInfo;
import com.ontraport.sdk.http.RequestParams;

import java.util.Arrays;
import java.util.List;

public class GetInfoAsyncTask extends AbstractAsyncTask<CollectionAdapter, ObjectInfo> {

    private RequestParams params;
    private boolean force_network = false;

    public GetInfoAsyncTask(CollectionAdapter adapter, RequestParams params) {
        this(adapter, params, false);
    }

    public GetInfoAsyncTask(CollectionAdapter adapter, RequestParams params, boolean force_network) {
        super(adapter);
        this.force_network = force_network;
        this.params = params;
    }

    @Override
    protected void onPostExecute(ObjectInfo info) {
        super.onPostExecute(info);
        if (info == null) {
            System.out.println("Couldn't retrieve info...");
            return;
        }
        List<ObjectInfo.FieldSettings> settings = info.getData().getListFieldSettings();
        for (ObjectInfo.FieldSettings field : settings) {
            String sort = field.getSortDir();
            if (sort != null) {
                params.put("sortDir", sort);
                params.put("sort", field.getName());
            }
        }

        String[] list_fields = info.getData().getListFields();
        String list = TextUtils.join(",", list_fields);
        if (!Arrays.asList(list_fields).contains("id")) {
            list += ",id";
        }
        if (Arrays.asList(list_fields).contains("fn")) {
            list += ",firstname,lastname";
        }
        params.put("listFields", list);

        int object_id = params.getAsInt(FieldUtils.OBJECT_ID);

        new GetListAsyncTask<>(adapter,
                list_fields,
                info.getCount(),
                object_id,
                force_network).execute(params);

        if (OntraportApplication.getInstance().isCustomObject(object_id)) {
            new GetFieldsAsyncTask<>(adapter,
                    list_fields,
                    info.getCount(),
                    object_id,
                    force_network).execute(params);
        }
    }

    @Override
    public ObjectInfo background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().retrieveCollectionInfo(params);
    }
}
