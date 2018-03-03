package com.ontraport.mobileapp.http;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.sdk.http.AbstractResponse;
import com.ontraport.sdk.http.Client;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.http.SingleResponse;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class OkClient extends Client {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient okHttpClient = new OkHttpClient();

    public OkClient(File cacheDir) {
        setCache(cacheDir);
    }

    private void setCache(File cacheDir) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDir, cacheSize);
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new ResponseCacheInterceptor())
                .addInterceptor(new OfflineResponseCacheInterceptor())
                .cache(cache)
                .build();
    }

    public SingleResponse httpRequest(RequestParams params, String url, String method) {
        return httpRequest(params, url, method, SingleResponse.class);
    }

    public <T extends AbstractResponse> T httpRequest(RequestParams params, String url, String method, Class<T> responseClazz) {
        HttpUrl.Builder http_builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        method = method.toUpperCase();
        if (method.equals("GET")) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object super_val = entry.getValue();
                String val = super_val.toString();
                if (super_val instanceof Integer) {
                    val = Integer.toString((Integer) super_val);
                }
                if (super_val instanceof String) {
                    val = (String) super_val;
                }
                http_builder.addQueryParameter(entry.getKey(), val);
            }
        }

        String http_url = http_builder.build().toString();
        Request.Builder request_builder = new Request.Builder().url(http_url);

        Gson gson = new Gson();
        if (!method.equals("GET")) {
            RequestBody post_body = RequestBody.create(JSON, gson.toJson(params));
            request_builder.method(method, post_body);
        }

        for (Map.Entry<String, String> entry : getRequestHeaders().entrySet()) {
            request_builder.addHeader(entry.getKey(), entry.getValue());
        }

        Request requestParams = request_builder.build();

        String json = null;
        try {
            Response response = okHttpClient.newCall(requestParams).execute();
            if (response.cacheResponse() != null) {
                System.out.println("Getting from cache");
            }
            setLastStatusCode(response.code());
            json = Objects.requireNonNull(response.body()).string();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (json == null || json.isEmpty()) {
            throw new NullResponseException();
        }

        return gson.fromJson(json, responseClazz);
    }

    private static class ResponseCacheInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + 60)
                    .build();
        }
    }

    private static class OfflineResponseCacheInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            if (!OntraportApplication.isNetworkAvailable()) {
                request = request.newBuilder()
                        .header("Cache-Control",
                                "public, only-if-cached, max-stale=" + 7*60*60*24)
                        .build();
            }
            return chain.proceed(request);
        }
    }
}
