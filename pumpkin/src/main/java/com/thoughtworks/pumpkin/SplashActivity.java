package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.os.Bundle;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.util.SafeAsyncTask;

@ContentView(R.layout.splash)
public class SplashActivity extends RoboActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashActivity splashActivity = this;
        new SafeAsyncTask() {

            @Override
            public Object call() throws Exception {
                Thread.sleep(4000);
                return null;
            }

            @Override
            protected void onSuccess(Object o) throws Exception {
                startActivity(new Intent(splashActivity, SigninActivity.class));
            }
        }.execute();
    }
}
