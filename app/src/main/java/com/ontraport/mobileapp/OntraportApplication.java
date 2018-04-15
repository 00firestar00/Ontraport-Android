package com.ontraport.mobileapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.SparseArray;
import com.ontraport.mobileapp.main.collection.CollectionAdapter;
import com.ontraport.mobileapp.main.collection.asynctasks.GetInfoAsyncTask;
import com.ontraport.mobileapp.main.record.RecordAdapter;
import com.ontraport.mobileapp.main.record.asynctasks.CreateAsyncTask;
import com.ontraport.mobileapp.main.record.asynctasks.GetOneAsyncTask;
import com.ontraport.mobileapp.sdk.http.OkClient;
import com.ontraport.mobileapp.utils.Constants;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.http.CustomObjectResponse;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.objects.ObjectType;

public class OntraportApplication extends Application {

    private static OntraportApplication instance;

    private Ontraport ontraport_api;
    private OkClient client;
    private Meta meta;
    private CustomObjectResponse custom_objects;
    private SparseArray<String> object_labels = new SparseArray<>();

    public OntraportApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Ontraport getApi() {
        return ontraport_api;
    }

    public OkClient getClient() {
        return client;
    }

    public Ontraport createApi(String api_id, String api_key) {
        client = new OkClient(getCacheDir());
        ontraport_api = new Ontraport(api_id, api_key, client);
        return ontraport_api;
    }

    public void setApi(Ontraport ontraport_api) {
        this.ontraport_api = ontraport_api;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setCustomObjects(CustomObjectResponse custom_objects) {
        this.custom_objects = custom_objects;
    }

    public CustomObjectResponse getCustomObjects() {
        return custom_objects;
    }

    public String getObjectLabel(int object_id) {
        String label = object_labels.get(object_id);
        if (label == null) {
            if (ObjectType.valueOf(object_id) == ObjectType.STAFF) {
                label = "[First Name] [Last Name]";
                object_labels.put(object_id, label);
            }
            for (CustomObjectResponse.Data data : getCustomObjects().getData()) {
                if (Integer.parseInt(data.getId()) == object_id) {
                    label = data.getLabel();
                    object_labels.put(object_id, label);
                    break;
                }
            }
        }
        return label;
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    public String getCollectionLabel(int object_id, int count) {
        for (CustomObjectResponse.Data data : getCustomObjects().getData()) {
            if (data.equals(object_id)) {
                if (count == 1) {
                    return data.getSingular();
                }
                return data.getPlural();
            }
        }

        String name = getMetaData(object_id).getName();
        if (count == 1) {
            return name;
        }
        return name + "s";
    }

    public Meta.Data getMetaData(int object_id) {
        return meta.getData().get(Integer.toString(object_id));
    }

    public static OntraportApplication getInstance() {
        return instance;
    }

    public void getCollection(CollectionAdapter adapter, RequestParams params) {
        getCollection(adapter, params, false);
    }

    public void getCollection(CollectionAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            ((OkClient) getApi().getHttpClient()).forceNetwork();
        }
        new GetInfoAsyncTask(adapter, params, force).execute(params);
    }

    public void getRecord(RecordAdapter adapter, RequestParams params) {
        getRecord(adapter, params, false);
    }

    public void getRecord(RecordAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            ((OkClient) getApi().getHttpClient()).forceNetwork();
        }
        new GetOneAsyncTask(adapter, params.getAsInt(Constants.OBJECT_ID)).execute(params);
    }

    public void createRecord(RecordAdapter adapter, RequestParams params) {
        createRecord(adapter, params, false);
    }

    public void createRecord(RecordAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            ((OkClient) getApi().getHttpClient()).forceNetwork();
        }
        new CreateAsyncTask(adapter, params.getAsInt(Constants.OBJECT_ID)).execute(params);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
