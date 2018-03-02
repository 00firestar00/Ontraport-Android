package com.ontraport.mobileapp.tasks;

import android.os.AsyncTask;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ObjectInfo;
import com.ontraport.sdk.http.RequestParams;

public class GetInfo extends AsyncTask<RequestParams, Void, ObjectInfo> {

    private CollectionAdapter adapter;
    private RequestParams params;

    public GetInfo(CollectionAdapter adapter, RequestParams params) {
        this.adapter = adapter;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        new GetList(adapter, info.getData().getListFields()).execute(params);
    }

    @Override
    protected ObjectInfo doInBackground(RequestParams... params) {
        try {
            OntraportApplication ontraport_app = OntraportApplication.getInstance();
            return ontraport_app.getApi().objects().retrieveCollectionInfo(params[0]);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        return new ObjectInfo();
    }
}
