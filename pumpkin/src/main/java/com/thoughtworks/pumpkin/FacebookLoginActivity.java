package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.inject.Inject;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;
import org.json.JSONObject;

import java.util.Arrays;

import static com.parse.ParseFacebookUtils.logIn;

public class FacebookLoginActivity extends AbstractParseActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID, true);
        final FacebookLoginActivity facebookLoginActivity = this;
        logIn(Arrays.asList(ParseFacebookUtils.Permissions.User.ABOUT_ME), this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    try {
                        String username = new JSONObject(ParseFacebookUtils.getFacebook().request("me")).getString("name");
                        preferences.edit().putString(Constant.Preferences.USERNAME, username).commit();
                        startActivity(new Intent(facebookLoginActivity, ZipCodeActivity.class));
                    } catch (Exception ignored) {
                        Util.showDialog("Exception in signing up, try again later", facebookLoginActivity);
                    }
                } else {
                    Util.showDialog("Error in signing up, try again later", facebookLoginActivity);
                }
            }
        });
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
//    }
}

