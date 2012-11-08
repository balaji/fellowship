package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.parse.Parse;
import com.thoughtworks.pumpkin.helper.Keys;
import roboguice.activity.RoboActivity;

public abstract class AbstractParseActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
    }
}
