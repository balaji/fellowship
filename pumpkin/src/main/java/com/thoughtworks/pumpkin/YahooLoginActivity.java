package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.thoughtworks.pumpkin.helper.OAuthClient;
import oauth.signpost.OAuthConsumer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.StringWriter;
import java.net.HttpURLConnection;

public class YahooLoginActivity extends OAuthLoginActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.client = OAuthClient.YAHOO;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected String clientDance(OAuthConsumer consumer) throws Exception {
        HttpGet get = new HttpGet(client.getAccessProfileUrl());
        consumer.sign(get);
        HttpResponse response = new DefaultHttpClient().execute(get);
        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            get = new HttpGet("http://social.yahooapis.com/v1/user/" + new JSONObject(writer.toString()).getJSONObject("guid").getString("value") + "/profile/tinyusercard?format=json");
            consumer.sign(get);
            response = new DefaultHttpClient().execute(get);
            if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                writer = new StringWriter();
                IOUtils.copy(response.getEntity().getContent(), writer);
                return new JSONObject(writer.toString()).getJSONObject("profile").getString("nickname");
            }
        }
        throw new Exception("Error in retrieving User information.");
    }
}
