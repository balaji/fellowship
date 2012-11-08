package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.OAuthClient;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import roboguice.activity.RoboActivity;

import javax.inject.Inject;

public abstract class OAuthLoginActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    OAuthClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        try {
            if (data != null) {
                if (client.getCallbackUrl().startsWith(data.getScheme())) {
            preferences.edit().putString(Constant.Preferences.USERNAME, getUserName(data.getQueryParameter("oauth_verifier"))).commit();
                    startActivity(new Intent(this, HomeActivity.class));
                    return;
                }
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeRequestToken()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserName(String oauthVerifier) throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(client.getApiKey(), client.getApiSecret());
        OAuthProvider provider = new CommonsHttpOAuthProvider(client.getRequestTokenUrl(), client.getAccessTokenUrl(), client.getAuthorizationUrl());
        consumer.setTokenWithSecret(preferences.getString(Constant.Preferences.TOKEN, null), preferences.getString(Constant.Preferences.SECRET, null));
        provider.retrieveAccessToken(consumer, null, "oauth_verifier", oauthVerifier);
        return getNameFromClient(consumer);
    }

    protected abstract String getNameFromClient(OAuthConsumer consumer) throws Exception;

    private String storeRequestToken() throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(client.getApiKey(), client.getApiSecret());
        OAuthProvider provider = new CommonsHttpOAuthProvider(client.getRequestTokenUrl(), client.getAccessTokenUrl(), client.getAuthorizationUrl());
        String redirectUrl = provider.retrieveRequestToken(consumer, client.getCallbackUrl());
        preferences.edit().putString(Constant.Preferences.TOKEN, consumer.getToken()).commit();
        preferences.edit().putString(Constant.Preferences.SECRET, consumer.getTokenSecret()).commit();
        return redirectUrl;
    }
}
