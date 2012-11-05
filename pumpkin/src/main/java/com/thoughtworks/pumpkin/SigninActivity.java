package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferences.getString("username", null) != null) {
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }
        setContentView(R.layout.signin);
        final SigninActivity signinActivity = this;

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signinActivity, TwitterLoginActivity.class));
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signinActivity, FacebookLoginActivity.class));
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signinActivity, GoogleLoginActivity.class));
            }
        });
    }
}

