package com.ontraport.mobileapp.main.collection.asynctasks;

import android.text.TextUtils;
import com.ontraport.mobileapp.AbstractAsyncTask;
import com.ontraport.mobileapp.AsyncAdapter;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.collection.CollectionInfo;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.ListResponse;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.objects.ObjectType;

public class GetListAsyncTask<A extends AsyncAdapter<CollectionInfo>>
        extends AbstractAsyncTask<A, ListResponse> {

    private int object_id;
    private String[] list_fields;
    private boolean force_network = false;
    private int count = 0;

    public GetListAsyncTask(A adapter, String[] list_fields, int object_id) {
        this(adapter, list_fields, 0, object_id, false);
    }

    public GetListAsyncTask(A adapter, String[] list_fields, int count, int object_id) {
        this(adapter, list_fields, count, object_id, false);
    }

    public GetListAsyncTask(A adapter, String[] list_fields, int count, int object_id, boolean force_network) {
        super(adapter);
        this.force_network = force_network;
        this.list_fields = list_fields;
        this.count = count;
        this.object_id = object_id;
    }

    @Override
    protected void onPostExecute(ListResponse list) {
        super.onPostExecute(list);

        Meta.Data object_meta = OntraportApplication.getInstance().getMetaData(object_id);
        for (String field : list_fields) {
            Meta.Field meta_field = object_meta.getFields().get(field);
            if (meta_field != null && meta_field.hasParent()) {
                int parent_id = Integer.parseInt(meta_field.getParent());
                RequestParams parent_params = new RequestParams();

                String[] list_fields = FieldUtils.getParentLabelListFields(ObjectType.valueOf(parent_id));

                parent_params.put(FieldUtils.OBJECT_ID, parent_id);
                parent_params.put("listFields", TextUtils.join(",", list_fields));
                new GetParentListAsyncTask<>(adapter,
                        list_fields,
                        parent_id).execute(parent_params);
            }
        }

        adapter.updateInfo(new CollectionInfo(object_id, list.getData(), list_fields, count).force(force_network));
    }

    @Override
    public ListResponse background(RequestParams params) throws RequiredParamsException {
        OntraportApplication ontraport_app = OntraportApplication.getInstance();
        if (force_network) {
            ontraport_app.getClient().forceNetwork();
        }
        return ontraport_app.getApi().objects().retrieveMultiple(params);
    }
}
