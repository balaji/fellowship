package com.thoughtworks.pumpkin;

import android.content.SharedPreferences;
import android.os.Bundle;
import roboguice.activity.RoboActivity;

import javax.inject.Inject;

public class StoreSelectActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stores);
    }
}
