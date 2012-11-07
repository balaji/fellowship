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

public class TwitterLoginActivity extends OAuthLoginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.client = OAuthClient.TWITTER;
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
            return new JSONObject(writer.toString()).getString("name");
        }
        throw new Exception("unable to get username");
    }
}
