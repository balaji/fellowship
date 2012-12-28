package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.inject.Inject;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.activity.RoboActivity;

public class TwitterLoginActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD:src/main/java/com/thoughtworks/pumpkin/TwitterLoginActivity.java
=======
        ParseTwitterUtils.initialize(Keys.TWITTER_CONSUMER_KEY, Keys.TWITTER_CONSUMER_SECRET);
>>>>>>> bb29d7bde8fe383ce5b7ecce3263cfc034900afa:src/main/java/com/thoughtworks/pumpkin/TwitterLoginActivity.java
        final TwitterLoginActivity twitterLoginActivity = this;
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    preferences.edit().putString(Constant.Preferences.USER_ID, user.getObjectId()).commit();
                    startActivity(new Intent(twitterLoginActivity, ZipCodeActivity.class));
                }   else if(user == null)  {
                    startActivity(new Intent(twitterLoginActivity,SigninActivity.class));
                }
            }
        });
    }
}
