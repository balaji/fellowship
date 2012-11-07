package com.thoughtworks.pumpkin.helper;

public enum OAuthClient {
    TWITTER(Keys.TWITTER_CONSUMER_KEY,
            Keys.TWITTER_CONSUMER_SECRET,
            "http://twitter.com/oauth/request_token",
            "http://twitter.com/oauth/access_token",
            "http://twitter.com/oauth/authorize",
            "http://api.twitter.com/1/account/verify_credentials.json",
            "x-oauthflow-twitter://com.thoughtworks.pumpkin"),

    GOOGLE(Keys.GOOGLE_CONSUMER_KEY,
            Keys.GOOGLE_CONSUMER_SECRET,
            "https://www.google.com/accounts/OAuthGetRequestToken?scope=https://www.googleapis.com/auth/userinfo.profile",
            "https://www.google.com/accounts/OAuthGetAccessToken",
            "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default",
            "https://www.googleapis.com/oauth2/v1/userinfo",
            "x-oauthflow-google://com.thoughtworks.pumpkin"),
   YAHOO(Keys.YAHOO_CONSUMER_KEY,
            Keys.YAHOO_CONSUMER_SECRET,
            "https://api.login.yahoo.com/oauth/v2/get_request_token",
            "https://api.login.yahoo.com/oauth/v2/get_token",
            "https://api.login.yahoo.com/oauth/v2/request_auth",
            "http://social.yahooapis.com/v1/me/guid?format=json",
            "x-oauthflow-yahoo://com.thoughtworks.pumpkin");

    private String apiKey;
    private String apiSecret;
    private String requestTokenUrl;
    private String accessTokenUrl;
    private String authorizationUrl;
    private String accessProfileUrl;
    private String callbackUrl;

    OAuthClient(String apiKey, String apiSecret, String requestTokenUrl, String accessTokenUrl,
                String authorizationUrl, String accessProfileUrl, String callbackUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.requestTokenUrl = requestTokenUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.authorizationUrl = authorizationUrl;
        this.accessProfileUrl = accessProfileUrl;
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

    public String getAccessProfileUrl() {
        return accessProfileUrl;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
