package com.thoughtworks.pumpkin;

import android.app.Application;
import com.parse.Parse;
import com.thoughtworks.pumpkin.helper.Keys;

public class PumpkinApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
    }
}
