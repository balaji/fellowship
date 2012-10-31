package com.thoughtworks.pumpkin.helpers;

public interface Constants {
    String CALLBACK_SCHEME = "x-oauthflow-twitter";
    String CALLBACK_HOST = "com.thoughtworks.pumpkin";
    String TWITTER_REDIRECT_URL = CALLBACK_SCHEME + "://" + CALLBACK_HOST;
}
