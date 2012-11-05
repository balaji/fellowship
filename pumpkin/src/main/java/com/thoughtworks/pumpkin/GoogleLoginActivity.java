package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.thoughtworks.pumpkin.helper.OAuthClient;

public class GoogleLoginActivity extends OAuthLoginActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.client = OAuthClient.GOOGLE;
        super.onCreate(savedInstanceState);
    }
}
