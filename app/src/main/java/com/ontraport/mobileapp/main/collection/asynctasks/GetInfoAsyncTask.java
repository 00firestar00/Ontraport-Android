package com.ontraport.mobileapp.main.collection.asynctasks;

import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ObjectInfo;
import com.ontraport.sdk.http.RequestParams;

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
        ObjectInfo.FieldSettings[] settings = info.getData().getListFieldSettings();
        for (ObjectInfo.FieldSettings field : settings) {
            String sort = field.getSortDir();
            if (sort != null) {
                params.put("sortDir", sort);
                params.put("sort", field.getName());
            }
        }
        adapter.setCountTitle(Integer.toString(info.getCount()));
        new GetListAsyncTask(adapter,
                info.getData().getListFields(),
                info.getCount(),
                params.getAsInt(Constants.OBJECT_TYPE_ID),
                force_network).execute(params);
    }

    @Override
    public ObjectInfo background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        return ontraport_app.getApi().objects().retrieveCollectionInfo(params);
    }
}
