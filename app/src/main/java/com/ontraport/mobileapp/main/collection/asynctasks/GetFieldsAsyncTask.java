package com.ontraport.mobileapp.main.collection.asynctasks;

import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.collection.CollectionInfo;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.FieldEditorResponse;
import com.ontraport.sdk.http.RequestParams;

public class GetFieldsAsyncTask<A extends AsyncAdapter<CollectionInfo>>
        extends AbstractAsyncTask<A, FieldEditorResponse> {

    private int object_id;
    private String[] list_fields;
    private boolean force_network = false;
    private int count = 0;

    public GetFieldsAsyncTask(A adapter, String[] list_fields, int object_id) {
        this(adapter, list_fields, 0, object_id, false);
    }

    public GetFieldsAsyncTask(A adapter, String[] list_fields, int count, int object_id) {
        this(adapter, list_fields, count, object_id, false);
    }

    public GetFieldsAsyncTask(A adapter, String[] list_fields, int count, int object_id, boolean force_network) {
        super(adapter);
        this.force_network = force_network;
        this.list_fields = list_fields;
        this.count = count;
        this.object_id = object_id;
    }

    @Override
    protected void onPostExecute(FieldEditorResponse field_editor) {
        super.onPostExecute(field_editor);
        OntraportApplication.getInstance().setFieldSections(object_id, field_editor);
    }

    @Override
    public FieldEditorResponse background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        if (force_network) {
            ontraport_app.getClient().forceNetwork();
        }
        params = new RequestParams();
        params.put(FieldUtils.OBJECT_ID, object_id);
        return ontraport_app.getApi().objects().retrieveFields(params);
    }
}
