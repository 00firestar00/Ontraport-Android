package com.ontraport.mobileapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.ontraport.mobileapp.adapters.CollectionAdapter;
import com.ontraport.mobileapp.adapters.RecordAdapter;
import com.ontraport.mobileapp.tasks.Create;
import com.ontraport.mobileapp.tasks.GetInfo;
import com.ontraport.mobileapp.tasks.GetOne;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

public class OntraportApplication extends Application {

    private static OntraportApplication instance;

    private Ontraport ontraportApi = null;
    private Meta meta = null;

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

    public void setApi(Ontraport ontraportApi) {
        this.ontraportApi = ontraportApi;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return meta;
    }

    public Meta.Data getMetaData(int object_id) {
        return meta.getData().get(Integer.toString(object_id));
    }

    public static OntraportApplication getInstance() {
        return instance;
    }

    public static void getCollection(CollectionAdapter adapter, RequestParams params) {
        new GetInfo(adapter, params).execute(params);
    }

    public static void getRecord(RecordAdapter adapter, RequestParams params) {
        new GetOne(adapter).execute(params);
    }

    public static void createRecord(RecordAdapter adapter, RequestParams params) {
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
