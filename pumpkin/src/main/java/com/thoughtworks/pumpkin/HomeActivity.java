package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;

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
        setContentView(R.layout.index);
        welcome.setText(preferences.getString("username", null));
        final HomeActivity homeActivity = this;
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeActivity, BrowseActivity.class));
            }
        });
    }
}
