package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.facebook.FacebookActivity;

public class IndexActivity extends FacebookActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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

