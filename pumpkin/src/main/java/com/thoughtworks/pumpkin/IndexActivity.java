package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class IndexActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @Nullable
    @InjectView(R.id.welcome)
    TextView welcome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferences.getString("username", null) != null) {
            setContentView(R.layout.index);
            if (welcome != null) {
                welcome.setText(preferences.getString("username", null));
            }
            return;
        }
        setContentView(R.layout.login);
        final IndexActivity indexActivity = this;

        Button twitterButton = (Button) findViewById(R.id.twitterSignin);
        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indexActivity, TwitterLoginActivity.class));
            }
        });

        Button facebookButton = (Button) findViewById(R.id.facebookSignin);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(indexActivity, FacebookLoginActivity.class));
            }
        });
    }

}

