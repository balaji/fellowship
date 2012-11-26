package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.inject.Inject;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.activity.RoboActivity;

public class FacebookLoginActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID, true);
        final FacebookLoginActivity facebookLoginActivity = this;
        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    preferences.edit().putString(Constant.Preferences.USER_ID, user.getObjectId()).commit();
                    startActivity(new Intent(facebookLoginActivity, ZipCodeActivity.class));
                } else {
                    Util.showDialog("Error in signing up, try again later", facebookLoginActivity);
                }
            }
        });
    }
}

