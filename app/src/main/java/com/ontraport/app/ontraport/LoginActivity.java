package com.ontraport.app.ontraport;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ontraport.sdk.Ontraport;
import com.ontraport.sdk.exceptions.RequiredParamsException;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.http.RequestParams;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private EditText api_id;
    private EditText api_key;
    private View progress_spinner;
    private View login_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        api_id = findViewById(R.id.appid);
        //populateAutoComplete();

        api_key = findViewById(R.id.key);
        api_key.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        login_form = findViewById(R.id.login_form);
        progress_spinner = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        api_id.setError(null);
        api_key.setError(null);

        // Store values at the time of the login attempt.
        String email = api_id.getText().toString();
        String password = api_key.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserLoginTask(email, password, this);
        mAuthTask.execute((Void) null);
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        login_form.setVisibility(show ? View.GONE : View.VISIBLE);
        login_form.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                login_form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progress_spinner.setVisibility(show ? View.VISIBLE : View.GONE);
        progress_spinner.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progress_spinner.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Meta> {

        private final String api_id;
        private final String api_key;
        private final Activity activity;

        UserLoginTask(String api_id, String api_key, Activity activity) {
            this.api_id = api_id;
            this.api_key = api_key;
            this.activity = activity;
        }

        @Override
        protected Meta doInBackground(Void... voids) {
            Meta response = null;
            try {
                response = new Ontraport(api_id, api_key).objects().retrieveMeta(new RequestParams());
            }
            catch (RequiredParamsException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(final Meta response) {
            mAuthTask = null;
            showProgress(false);

            if (response != null && response.getCode() == 0) {
                OntraportApplication app = (OntraportApplication) getApplication();
                app.setApi(new Ontraport(api_id, api_key, new OkClient(app.getCacheDir())));
                app.setMeta(response);
                Intent intent = new Intent(activity, MainActivity.class);
                showProgress(false);
                startActivity(intent);
                finish();
            }
            else {
                //api_key.setError(getString(R.string.error_incorrect_password));
                LoginActivity.this.api_key.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

