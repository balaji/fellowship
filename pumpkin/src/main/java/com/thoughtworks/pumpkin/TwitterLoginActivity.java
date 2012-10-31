package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.thoughtworks.pumpkin.helpers.Constants;
import com.thoughtworks.pumpkin.helpers.Keys;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;

public class TwitterLoginActivity extends RoboActivity {

    public static final String REQUEST_TOKEN_URL = "http://twitter.com/oauth/request_token";
    public static final String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
    public static final String AUTHORIZATION_URL = "http://twitter.com/oauth/authorize";

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.welcome)
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Keys.TWITTER_CONSUMER_KEY, Keys.TWITTER_CONSUMER_SECRET);
        OAuthProvider provider = new CommonsHttpOAuthProvider(REQUEST_TOKEN_URL, ACCESS_TOKEN_URL, AUTHORIZATION_URL);

        Uri data = getIntent().getData();
        try {
            if (data != null) {
                if (Constants.CALLBACK_SCHEME.equals(data.getScheme())) {
                    consumer.setTokenWithSecret(preferences.getString("consumer_token", null), preferences.getString("consumer_secret", null));
                    provider.retrieveAccessToken(consumer, data.getQueryParameter("oauth_verifier"));
                    setContentView(R.layout.index);

                    HttpGet get = new HttpGet("http://api.twitter.com/1/account/verify_credentials.json");
                    consumer.sign(get);
                    HttpResponse response = new DefaultHttpClient().execute(get);

                    if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                        StringWriter writer = new StringWriter();
                        IOUtils.copy(response.getEntity().getContent(), writer);
                        String username = new JSONObject(writer.toString()).getString("name");
                        preferences.edit().putString("username", username).commit();
                        welcome.setText("Welcome " + username + "!");
                    }
                    return;
                }
            }

            String redirectUrl = provider.retrieveRequestToken(consumer, Constants.TWITTER_REDIRECT_URL);
            preferences.edit().putString("consumer_token", consumer.getToken()).commit();
            preferences.edit().putString("consumer_secret", consumer.getTokenSecret()).commit();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } catch (OAuthMessageSignerException
                | OAuthNotAuthorizedException
                | OAuthExpectationFailedException
                | OAuthCommunicationException
                | IOException
                | JSONException e) {
            e.printStackTrace();
        }
    }
}
