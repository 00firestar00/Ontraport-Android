package com.ontraport.mobileapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import com.ontraport.mobileapp.LoginActivity;
import com.ontraport.mobileapp.MainActivity;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.http.NullResponseException;
import com.ontraport.mobileapp.http.OkClient;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

public class LoginTask extends AsyncTask<Void, Void, Meta> {

    private final String api_id;
    private final String api_key;
    private SharedPreferences preferences;

    private final LoginActivity activity;
    private final OntraportApplication application;
    private NullResponseException exception;

    private Ontraport ontraport;

    public LoginTask(String api_id, String api_key, LoginActivity activity) {
        this.api_id = api_id;
        this.api_key = api_key;
        this.activity = activity;
        preferences = activity.getPreferences(Context.MODE_PRIVATE);
        application = (OntraportApplication) activity.getApplication();
        ontraport = new Ontraport(api_id, api_key, new OkClient(application.getCacheDir()));
    }

    @Override
    protected Meta doInBackground(Void... voids) {
        Meta response = null;
        try {
            response = ontraport.objects().retrieveMeta(new RequestParams());
        }
        catch (RequiredParamsException e) {
            e.printStackTrace();
        }
        catch (NullResponseException e) {
            exception = e;
        }

        return response;
    }

    @Override
    protected void onPostExecute(final Meta response) {

        SharedPreferences.Editor editor = preferences.edit();

        if (response != null && response.getCode() == 0) {
            application.setApi(ontraport);
            application.setMeta(response);

            editor.putString("Api-Appid", api_id);
            editor.putString("Api-Key", api_key);
            editor.apply();

            Intent intent = new Intent(activity, MainActivity.class);
            activity.showProgress(false);
            activity.startActivity(intent);
            activity.finish();
        }
        else {
            //api_key.setError(getString(R.string.error_incorrect_password));
            if (exception != null) {
                Toast.makeText(activity, "Could not login. No network connection!", Toast.LENGTH_SHORT).show();
            }
            ontraport = null;
            editor.clear();
        }
        activity.postLogin();
    }

    @Override
    protected void onCancelled() {
        activity.postLogin();
    }
}
