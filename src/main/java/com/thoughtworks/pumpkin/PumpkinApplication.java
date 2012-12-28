package com.thoughtworks.pumpkin;

import android.app.Application;
import com.parse.Parse;
<<<<<<< HEAD
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
=======
>>>>>>> bb29d7bde8fe383ce5b7ecce3263cfc034900afa
import com.thoughtworks.pumpkin.helper.Keys;

public class PumpkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
<<<<<<< HEAD
        ParseFacebookUtils.initialize(Keys.FACEBOOK_APP_ID);
        ParseTwitterUtils.initialize(Keys.TWITTER_CONSUMER_KEY, Keys.TWITTER_CONSUMER_SECRET);
=======
>>>>>>> bb29d7bde8fe383ce5b7ecce3263cfc034900afa
    }
}
