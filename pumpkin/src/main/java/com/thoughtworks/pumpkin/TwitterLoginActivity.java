package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.inject.Inject;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.PumpkinAsyncTask;
import com.thoughtworks.pumpkin.helper.Util;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import roboguice.activity.RoboActivity;

import java.io.StringWriter;
import java.net.HttpURLConnection;

public class TwitterLoginActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        ParseTwitterUtils.initialize(Keys.TWITTER_CONSUMER_KEY, Keys.TWITTER_CONSUMER_SECRET);
        final TwitterLoginActivity twitterLoginActivity = this;
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user != null) {
                    try {
                        new PumpkinAsyncTask<String>() {

                            @Override
                            public String call() throws Exception {
                                HttpClient client = new DefaultHttpClient();
                                HttpGet verifyGet = new HttpGet("https://api.twitter.com/1/account/verify_credentials.json");
                                ParseTwitterUtils.getTwitter().signRequest(verifyGet);
                                HttpResponse response = client.execute(verifyGet);
                                if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                                    StringWriter writer = new StringWriter();
                                    IOUtils.copy(response.getEntity().getContent(), writer);
                                    return new JSONObject(writer.toString()).getString("name");
                                }
                                return null;
                            }

                            @Override
                            protected void onSuccess(String response) throws Exception {
                                super.onSuccess(response);
                                if (response != null) {
                                    preferences.edit().putString(Constant.Preferences.USERNAME, response).commit();
                                    startActivity(new Intent(twitterLoginActivity, ZipCodeActivity.class));
                                } else {
                                    Util.showDialog("Error in signing up, try again later", twitterLoginActivity);
                                }
                            }
                        }.activity(twitterLoginActivity).execute();
                    } catch (Exception e) {
                        Util.showDialog("Exception in signing up, try again later", twitterLoginActivity);
                    }
                } else {
                    Util.showDialog("Error in signing up, try again later", twitterLoginActivity);
                }
            }
        });
    }
}
