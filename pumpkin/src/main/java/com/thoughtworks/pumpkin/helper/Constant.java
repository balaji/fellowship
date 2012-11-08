package com.thoughtworks.pumpkin.helper;

public interface Constant {
    interface Preferences {
        final String USERNAME = "username";
        final String TOKEN = "token";
        final String SECRET = "secret";
    }

    interface URL {
        final String GOOGLE_PROFILE = "https://www.googleapis.com/oauth2/v1/userinfo";
        final String GOOGLE_REQUEST = "https://www.google.com/accounts/OAuthGetRequestToken?scope=https://www.googleapis.com/auth/userinfo.profile";
        final String GOOGLE_AUTHORIZE = "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default";
        final String GOOGLE_ACCESS = "https://www.google.com/accounts/OAuthGetAccessToken";

        final String TWITTER_PROFILE = "http://api.twitter.com/1/account/verify_credentials.json";
        final String TWITTER_REQUEST = "http://twitter.com/oauth/request_token";
        final String TWITTER_AUTHORIZE = "http://twitter.com/oauth/authorize";
        final String TWITTER_ACCESS = "http://twitter.com/oauth/access_token";

        final String YAHOO_PROFILE = "http://social.yahooapis.com/v1/user/%s/profile/tinyusercard?format=json";
        final String YAHOO_REQUEST = "https://api.login.yahoo.com/oauth/v2/get_request_token";
        final String YAHOO_AUTHORIZE = "https://api.login.yahoo.com/oauth/v2/request_auth";
        final String YAHOO_ACCESS = "https://api.login.yahoo.com/oauth/v2/get_token";
        final String YAHOO_GUID = "http://social.yahooapis.com/v1/me/guid?format=json";
    }
}
