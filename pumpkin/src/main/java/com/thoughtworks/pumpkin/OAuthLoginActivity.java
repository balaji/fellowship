package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.thoughtworks.pumpkin.helper.OAuthClient;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import roboguice.activity.RoboActivity;

import javax.inject.Inject;
import java.io.StringWriter;
import java.net.HttpURLConnection;

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
                if (client.getCallbackScheme().equals(data.getScheme())) {
                    preferences.edit().putString("username", getUserName(data.getQueryParameter("oauth_verifier"))).commit();
                    startActivity(new Intent(this, HomeActivity.class));
                    return;
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeRequestToken(client.getCallbackUrl())));
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUserName(String oauthVerifier) throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(client.getApiKey(), client.getApiSecret());
        OAuthProvider provider = new CommonsHttpOAuthProvider(client.getRequestTokenUrl(),
                client.getAccessTokenUrl(), client.getAuthorizationUrl());
        consumer.setTokenWithSecret(preferences.getString("consumer_token", null), preferences.getString("consumer_secret", null));
        provider.retrieveAccessToken(consumer, null, "oauth_verifier", oauthVerifier);

        HttpGet get = new HttpGet(client.getAccessProfileUrl());
        consumer.sign(get);
        HttpResponse response = new DefaultHttpClient().execute(get);

        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            return new JSONObject(writer.toString()).getString("name");
        }
        throw new Exception("Error in retrieving User information.");
    }

    public String storeRequestToken(String callbackUrl) throws Exception {
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(client.getApiKey(), client.getApiSecret());
        OAuthProvider provider = new CommonsHttpOAuthProvider(client.getRequestTokenUrl(),
                client.getAccessTokenUrl(), client.getAuthorizationUrl());

        String redirectUrl = provider.retrieveRequestToken(consumer, callbackUrl);
        preferences.edit().putString("consumer_token", consumer.getToken()).commit();
        preferences.edit().putString("consumer_secret", consumer.getTokenSecret()).commit();
        return redirectUrl;
    }
}
