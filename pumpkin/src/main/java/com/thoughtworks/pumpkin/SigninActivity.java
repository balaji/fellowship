package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.inject.Inject;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.signin)
public class SigninActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.twitterSiginin)
    ImageView twitter;

    @InjectView(R.id.facebookSiginin)
    ImageView facebook;

    @InjectView(R.id.googleSiginin)
    ImageView google;

    @InjectView(R.id.yahooSiginin)
    ImageView yahoo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferences.getString(Constant.Preferences.USERNAME, null) != null) {
            if (preferences.getString(Constant.Preferences.PREFERRED_STORE, null) != null) {
                startActivity(new Intent(this, HomeActivity.class));
            }
            startActivity(new Intent(this, ZipCodeActivity.class));
            return;
        }
        final SigninActivity signinActivity = this;
        final Util util = new Util(getApplicationContext());

        onClick(twitter, TwitterLoginActivity.class, util, signinActivity);
        onClick(facebook, FacebookLoginActivity.class, util, signinActivity);
        onClick(yahoo, YahooLoginActivity.class, util, signinActivity);
        onClick(google, GoogleLoginActivity.class, util, signinActivity);
    }

    private void onClick(ImageView imageView, final Class clazz, final Util util, final SigninActivity signinActivity) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!util.isConnectingToInternet()) {
                    util.showDialog(Constant.Message.NO_INTERNET_CONNECTION);
                } else {
                    startActivity(new Intent(signinActivity, clazz));
                }
            }
        });
    }
}

