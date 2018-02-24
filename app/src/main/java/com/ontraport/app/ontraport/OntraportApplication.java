package com.ontraport.app.ontraport;

import android.app.Application;
import com.ontraport.app.ontraport.adapters.CollectionAdapter;
import com.ontraport.app.ontraport.adapters.RecordAdapter;
import com.ontraport.app.ontraport.tasks.GetInfo;
import com.ontraport.app.ontraport.tasks.GetOne;
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

}
