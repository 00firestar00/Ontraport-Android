package com.ontraport.mobileapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.mobileapp.adapters.RecordAdapter;
import com.ontraport.mobileapp.sdk.http.CustomObjectResponse;
import com.ontraport.mobileapp.sdk.http.OkClient;
import com.ontraport.mobileapp.tasks.Create;
import com.ontraport.mobileapp.tasks.GetInfo;
import com.ontraport.mobileapp.tasks.GetOne;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

public class OntraportApplication extends Application {

    private static OntraportApplication instance;

    private Ontraport ontraportApi;
    private OkClient client;
    private Meta meta;
    private CustomObjectResponse custom_objects;

    public OntraportApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public Ontraport getApi() {
        return ontraportApi;
    }

    public OkClient getClient() {
        return client;
    }

    public Ontraport createApi(String api_id, String api_key) {
        client = new OkClient(getCacheDir());
        ontraportApi = new Ontraport(api_id, api_key, client);
        return ontraportApi;
    }

    public void setApi(Ontraport ontraportApi) {
        this.ontraportApi = ontraportApi;
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
            getClient().forceNetwork();
            getApi().setHttpClient(getClient());
        }
        new GetInfo(adapter, params, force).execute(params);
    }

    public void getRecord(RecordAdapter adapter, RequestParams params) {
        getRecord(adapter, params, false);
    }

    public void getRecord(RecordAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            getClient().forceNetwork();
            getApi().setHttpClient(getClient());
        }
        new GetOne(adapter).execute(params);
    }

    public void createRecord(RecordAdapter adapter, RequestParams params) {
        createRecord(adapter, params, false);
    }

    public void createRecord(RecordAdapter adapter, RequestParams params, boolean force) {
        if (force) {
            getClient().forceNetwork();
            getApi().setHttpClient(getClient());
        }
        new Create(adapter).execute(params);
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
