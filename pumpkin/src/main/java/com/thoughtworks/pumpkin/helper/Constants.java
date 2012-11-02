package com.thoughtworks.pumpkin.helper;

public interface Constants {
    String CALLBACK_SCHEME = "x-oauthflow-twitter";
    String CALLBACK_HOST = "com.thoughtworks.pumpkin";
    String TWITTER_REDIRECT_URL = CALLBACK_SCHEME + "://" + CALLBACK_HOST;
}
