package com.ontraport.mobileapp.sdk.http;

import android.support.annotation.Nullable;
import com.ontraport.sdk.http.CustomObjectResponse;
import com.ontraport.sdk.http.Meta;

public class CombinedMeta {
    private Meta meta;
    private CustomObjectResponse co_res;

    public CombinedMeta() {
    }

    public CombinedMeta(Meta meta, CustomObjectResponse co_res) {
        this.meta = meta;
        this.co_res = co_res;
    }

    @Nullable
    public Meta getMeta() {
        return meta;
    }

    @Nullable
    public CustomObjectResponse getCustomObjectResponse() {
        return co_res;
    }
}
