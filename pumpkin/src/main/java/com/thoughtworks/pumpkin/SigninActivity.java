package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;

public class SigninActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.twitterSignin)
    Button twitterButton;

    @InjectView(R.id.facebookSignin)
    Button facebookButton;

    @InjectView(R.id.googleSignin)
    Button googleButton;

    @InjectView(R.id.yahooSignin)
    Button yahooButton;

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
        setContentView(R.layout.signin);
        final SigninActivity signinActivity = this;
        final Util util = new Util(getApplicationContext());

        onClick(twitterButton, TwitterLoginActivity.class, util, signinActivity);
        onClick(facebookButton, FacebookLoginActivity.class, util, signinActivity);
        onClick(yahooButton, YahooLoginActivity.class, util, signinActivity);
        onClick(googleButton, GoogleLoginActivity.class, util, signinActivity);
    }

    private void onClick(Button button, final Class clazz, final Util util, final SigninActivity signinActivity) {
        button.setOnClickListener(new View.OnClickListener() {
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

