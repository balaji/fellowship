package com.thoughtworks.pumpkin.helper;

public enum OAuthClient {
    TWITTER(Keys.TWITTER_CONSUMER_KEY,
            Keys.TWITTER_CONSUMER_SECRET,
            "http://twitter.com/oauth/request_token",
            "http://twitter.com/oauth/access_token",
            "http://twitter.com/oauth/authorize",
            "http://api.twitter.com/1/account/verify_credentials.json",
            Constants.TWITTER_CALLBACK_SCHEME,
            Constants.TWITTER_CALLBACK_SCHEME + "://" + Constants.CALLBACK_HOST),

    GOOGLE(Keys.GOOGLE_CONSUMER_KEY,
            Keys.GOOGLE_CONSUMER_SECRET,
            "https://www.google.com/accounts/OAuthGetRequestToken?scope=https://www.googleapis.com/auth/userinfo.profile",
            "https://www.google.com/accounts/OAuthGetAccessToken",
            "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default",
            "https://www.googleapis.com/oauth2/v1/userinfo",
            Constants.GOOGLE_CALLBACK_SCHEME,
            Constants.GOOGLE_CALLBACK_SCHEME + "://" + Constants.CALLBACK_HOST);

    private String apiKey;
    private String apiSecret;
    private String requestTokenUrl;
    private String accessTokenUrl;
    private String authorizationUrl;
    private String accessProfileUrl;
    private String callbackScheme;
    private String callbackUrl;

    OAuthClient(String apiKey, String apiSecret, String requestTokenUrl, String accessTokenUrl,
                String authorizationUrl, String accessProfileUrl, String callbackScheme, String callbackUrl) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.requestTokenUrl = requestTokenUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.authorizationUrl = authorizationUrl;
        this.accessProfileUrl = accessProfileUrl;
        this.callbackScheme = callbackScheme;
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

    public String getCallbackScheme() {
        return callbackScheme;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }
}
