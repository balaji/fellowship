package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.thoughtworks.pumpkin.exception.OAuthFailedException;
import com.thoughtworks.pumpkin.helper.Constant;
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
    protected String getNameFromClient(OAuthConsumer consumer) throws Exception {
        HttpGet get = new HttpGet(String.format(Constant.URL.YAHOO_PROFILE, guid(consumer)));
        consumer.sign(get);
        HttpResponse response = new DefaultHttpClient().execute(get);
        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            return new JSONObject(writer.toString()).getJSONObject("profile").getString("nickname");
        }
        throw new OAuthFailedException();
    }

    private String guid(OAuthConsumer consumer) throws Exception {
        HttpGet get = new HttpGet(Constant.URL.YAHOO_GUID);
        consumer.sign(get);
        HttpResponse response = new DefaultHttpClient().execute(get);
        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), writer);
            return new JSONObject(writer.toString()).getJSONObject("guid").getString("value");
        }
        throw new Exception("Error in retrieving User information.");
    }
}
