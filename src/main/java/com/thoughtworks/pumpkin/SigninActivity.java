package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.inject.Inject;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.listener.PumpkinOnClickListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class SigninActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.twitterSiginin)
    Button twitter;

    @InjectView(R.id.facebookSiginin)
    Button facebook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferences.getString(Constant.Preferences.USER_ID, null) != null) {
            startActivity(new Intent(this, (preferences.getString(Constant.Preferences.PREFERRED_STORE, null) != null) ?
                    WishListActivity.class : ZipCodeActivity.class));
            return;
        }
        setContentView(R.layout.signin);
        onClick(twitter, TwitterLoginActivity.class);
        onClick(facebook, FacebookLoginActivity.class);
    }

    private void onClick(Button button, final Class clazz) {
        button.setOnClickListener(new PumpkinOnClickListener(this) {
            @Override
            public void done(View view) {
                startActivity(new Intent(getActivity(), clazz));
            }
        });
    }
}

