package com.ontraport.mobileapp.tasks;

import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ObjectInfo;
import com.ontraport.sdk.http.RequestParams;

public class GetInfo extends AbstractTask<CollectionAdapter, ObjectInfo> {

    private RequestParams params;
    private boolean force_network = false;

    public GetInfo(CollectionAdapter adapter, RequestParams params) {
        this(adapter, params, false);
    }

    public GetInfo(CollectionAdapter adapter, RequestParams params, boolean force_network) {
        super(adapter);
        this.force_network = force_network;
        this.params = params;
    }

    @Override
    protected void onPostExecute(ObjectInfo info) {
        super.onPostExecute(info);
        ObjectInfo.FieldSettings[] settings = info.getData().getListFieldSettings();
        for (ObjectInfo.FieldSettings field : settings) {
            String sort = field.getSortDir();
            if (sort != null) {
                params.put("sortDir", sort);
                params.put("sort", field.getName());
            }
        }
        new GetList(adapter, info.getData().getListFields(), force_network).execute(params);
    }

    @Override
    public ObjectInfo background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().retrieveCollectionInfo(params);
    }
}
