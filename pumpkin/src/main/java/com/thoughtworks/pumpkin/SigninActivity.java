package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.inject.Inject;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinOnClickListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SigninActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.twitterSiginin)
    Button twitter;

    @InjectView(R.id.facebookSiginin)
    Button facebook;

    @InjectView(R.id.googleSiginin)
    Button google;

    @InjectView(R.id.yahooSiginin)
    Button yahoo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferences.getString(Constant.Preferences.USERNAME, null) != null) {
            if (preferences.getString(Constant.Preferences.PREFERRED_STORE, null) != null) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                startActivity(new Intent(this, ZipCodeActivity.class));
            }
            return;
        }
        final SigninActivity signinActivity = this;
        setContentView(R.layout.signin);
        onClick(twitter, TwitterLoginActivity.class, signinActivity);
        onClick(facebook, FacebookLoginActivity.class, signinActivity);
        onClick(yahoo, YahooLoginActivity.class, signinActivity);
        onClick(google, GoogleLoginActivity.class, signinActivity);
    }

    private void onClick(Button button, final Class clazz, final SigninActivity signinActivity) {
        button.setOnClickListener(new PumpkinOnClickListener(signinActivity) {
            @Override
            public void done(View view) {
                startActivity(new Intent(signinActivity, clazz));
            }
        });
    }
}

