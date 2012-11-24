package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.inject.Inject;
import com.thoughtworks.pumpkin.helper.Constant;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;



@ContentView(R.layout.zip_code)
public class ZipCodeActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.submit)
    Button zipCodeSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ZipCodeActivity activity = this;
        zipCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putString(Constant.Preferences.PREFERRED_STORE, "sample").commit();
                startActivity(new Intent(activity, HomeActivity.class));
            }
        });

    }
}
