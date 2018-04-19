package com.ontraport.mobileapp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.main.MainActivity;
import com.ontraport.mobileapp.sdk.http.CombinedMeta;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.exceptions.NullResponseException;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.CustomObjectResponse;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;
import com.ontraport.sdk.objects.CustomObjectsMeta;

public class LoginAsyncTask extends AsyncTask<Void, Void, CombinedMeta> {

    private final String api_id;
    private final String api_key;
    private SharedPreferences preferences;

    private final LoginActivity activity;
    private final OntraportApplication application;
    private NullResponseException exception;

    private Ontraport ontraport;

    public LoginAsyncTask(String api_id, String api_key, LoginActivity activity) {
        this.api_id = api_id;
        this.api_key = api_key;
        this.activity = activity;
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        application = (OntraportApplication) activity.getApplication();
        ontraport = application.createApi(api_id, api_key);
    }

    @Override
    protected void onPostExecute(final CombinedMeta combined) {
        SharedPreferences.Editor editor = preferences.edit();
        Meta response = combined.getMeta();
        application.setCustomObjects(combined.getCustomObjectResponse());
        if (response != null && response.getCode() == 0) {
            application.setMeta(response);

            editor.putString("Api-Appid", api_id);
            editor.putString("Api-Key", api_key);
            editor.apply();

            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
        else {
            //api_key.setError(getString(R.string.error_incorrect_password));
            if (exception != null) {
                Toast.makeText(activity, "Could not login. No network connection!", Toast.LENGTH_SHORT).show();
            }
            ontraport = null;
            application.setApi(null);
            editor.clear();
        }
        activity.postLogin();
    }

    @Override
    protected void onCancelled() {
        activity.postLogin();
    }

    @Override
    protected CombinedMeta doInBackground(Void... voids) {
        CombinedMeta response = new CombinedMeta();
        try {
            Meta mres = ontraport.objects().retrieveMeta(new RequestParams());
            CustomObjectResponse cores = new CustomObjectsMeta(ontraport).meta(new RequestParams());
            response = new CombinedMeta(mres, cores);
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        catch (NullResponseException e) {
            exception = e;
        }

        return response;
    }
}
