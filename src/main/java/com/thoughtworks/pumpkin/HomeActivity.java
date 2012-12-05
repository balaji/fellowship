package com.thoughtworks.pumpkin;

import android.os.Bundle;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;


@ContentView(R.layout.home)
public class HomeActivity  extends RoboFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
