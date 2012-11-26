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
import com.thoughtworks.pumpkin.helper.PumpkinAsyncTask;
import com.thoughtworks.pumpkin.helper.Util;
import org.json.JSONObject;
import roboguice.activity.RoboActivity;

import java.util.Arrays;

public class FacebookLoginActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID, true);
        final FacebookLoginActivity facebookLoginActivity = this;
        ParseFacebookUtils.logIn(Arrays.asList(ParseFacebookUtils.Permissions.User.ABOUT_ME), this, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException err) {
            if (user != null) {
                try {
                    new PumpkinAsyncTask<JSONObject>() {

                        @Override
                        public JSONObject call() throws Exception {
                            return new JSONObject(ParseFacebookUtils.getFacebook().request("me"));
                        }

                        @Override
                        protected void onSuccess(JSONObject jsonObject) throws Exception {
                            super.onSuccess(jsonObject);
                            String username = jsonObject.getString("name");
                            preferences.edit().putString(Constant.Preferences.USERNAME, username).commit();
                            startActivity(new Intent(facebookLoginActivity, ZipCodeActivity.class));
                        }
                    }.activity(facebookLoginActivity).execute();

                } catch (Exception ignored) {
                    Util.showDialog("Exception in signing up, try again later", facebookLoginActivity);
                }
            } else {
                Util.showDialog("Error in signing up, try again later", facebookLoginActivity);
            }
        }
    });
    }
}

