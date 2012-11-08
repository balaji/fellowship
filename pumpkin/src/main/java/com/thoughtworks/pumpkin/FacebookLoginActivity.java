package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.thoughtworks.pumpkin.helper.Constant;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Arrays;

public class FacebookLoginActivity extends AbstractParseActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseFacebookUtils.initialize("509095802449138", true);
        final FacebookLoginActivity facebookLoginActivity = this;
        ParseFacebookUtils.logIn(Arrays.asList(ParseFacebookUtils.Permissions.User.ABOUT_ME), this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    try {
                        String username = new JSONObject(ParseFacebookUtils.getFacebook().request("me")).getString("name");
                        preferences.edit().putString(Constant.Preferences.USERNAME, username).commit();
                        startActivity(new Intent(facebookLoginActivity, HomeActivity.class));
                    } catch (Exception ignored) {
                    }
                } else {
                    Log.d("Pumpkin", "The user cancelled the Facebook login.");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}

