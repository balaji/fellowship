package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import com.google.inject.Inject;
import com.parse.LogInCallback;
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
<<<<<<< HEAD:src/main/java/com/thoughtworks/pumpkin/FacebookLoginActivity.java
=======
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID, false);
>>>>>>> bb29d7bde8fe383ce5b7ecce3263cfc034900afa:src/main/java/com/thoughtworks/pumpkin/FacebookLoginActivity.java
        final FacebookLoginActivity facebookLoginActivity = this;
        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    preferences.edit().putString(Constant.Preferences.USER_ID, user.getObjectId()).commit();
                    startActivity(new Intent(facebookLoginActivity, ZipCodeActivity.class));
                }   else if(user == null)  {
                        startActivity(new Intent(facebookLoginActivity,SigninActivity.class));
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            startActivity(new Intent(this,SigninActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}

