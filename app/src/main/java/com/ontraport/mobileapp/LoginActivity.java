package com.ontraport.mobileapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import com.ontraport.mobileapp.tasks.LoginTask;

public class LoginActivity extends AppCompatActivity {

    private LoginTask mAuthTask = null;

    private TextInputEditText api_id;
    private TextInputEditText api_key;
    private View progress_spinner;
    private View login_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        api_id = findViewById(R.id.appid);
        api_id.setText(preferences.getString("Api-Appid", ""));
        //populateAutoComplete();

        api_key = findViewById(R.id.key);
        api_key.setText(preferences.getString("Api-Key", ""));

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

        login_form = findViewById(R.id.email_login_form);
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
        mAuthTask = new LoginTask(email, password, this);
        mAuthTask.execute((Void) null);
    }

    public void postLogin() {
        mAuthTask = null;
        showProgress(false);
        api_key.requestFocus();
    }

    public void showProgress(final boolean show) {

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

}

