package com.thoughtworks.pumpkin.helper;

public enum OAuthClient {
    TWITTER(Keys.TWITTER_CONSUMER_KEY,
            Keys.TWITTER_CONSUMER_SECRET,
            Constant.URL.TWITTER_REQUEST,
            Constant.URL.TWITTER_ACCESS,
            Constant.URL.TWITTER_AUTHORIZE,
            "x-oauthflow-twitter://com.thoughtworks.pumpkin"),

    GOOGLE(Keys.GOOGLE_CONSUMER_KEY,
            Keys.GOOGLE_CONSUMER_SECRET,
            Constant.URL.GOOGLE_REQUEST,
            Constant.URL.GOOGLE_ACCESS,
            Constant.URL.GOOGLE_AUTHORIZE,
            "x-oauthflow-google://com.thoughtworks.pumpkin"),

    YAHOO(Keys.YAHOO_CONSUMER_KEY,
            Keys.YAHOO_CONSUMER_SECRET,
            Constant.URL.YAHOO_REQUEST,
            Constant.URL.YAHOO_ACCESS,
            Constant.URL.YAHOO_AUTHORIZE,
            "x-oauthflow-yahoo://com.thoughtworks.pumpkin");

    private String apiKey;
    private String apiSecret;
    private String requestTokenUrl;
    private String accessTokenUrl;
    private String authorizationUrl;
    private String callbackUrl;

    OAuthClient(String apiKey, String apiSecret, String requestTokenUrl, String accessTokenUrl,
                String authorizationUrl, String callbackUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.requestTokenUrl = requestTokenUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.authorizationUrl = authorizationUrl;
        this.callbackUrl = callbackUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public String getRequestTokenUrl() {
        return requestTokenUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
