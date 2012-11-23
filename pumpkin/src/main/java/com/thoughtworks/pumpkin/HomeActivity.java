package com.thoughtworks.pumpkin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;



@ContentView(R.layout.home)
public class HomeActivity  extends RoboActivity {

    @InjectView(R.id.welcome)
    TextView welcome;

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.browseButton)
    Button browseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
