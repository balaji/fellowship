package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import roboguice.activity.RoboActivity;
import roboguice.util.SafeAsyncTask;

public class SplashActivity extends RoboActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final SplashActivity splashActivity = this;
        new SafeAsyncTask() {

            @Override
            public Object call() throws Exception {
                Thread.sleep(2000);
                return null;
            }

            @Override
            protected void onSuccess(Object o) throws Exception {
                startActivity(new Intent(splashActivity, SigninActivity.class));
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
