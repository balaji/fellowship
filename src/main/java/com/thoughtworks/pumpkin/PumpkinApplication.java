package com.thoughtworks.pumpkin;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.thoughtworks.pumpkin.helper.Keys;

public class PumpkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID);
        ParseTwitterUtils.initialize(Keys.TWITTER_CONSUMER_KEY, Keys.TWITTER_CONSUMER_SECRET);
    }
}
