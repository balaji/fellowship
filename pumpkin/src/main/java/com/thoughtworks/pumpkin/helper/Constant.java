package com.thoughtworks.pumpkin.helper;

public interface Constant {
    interface Preferences {
        final String USERNAME = "username";
        final String TOKEN = "token";
        final String SECRET = "secret";
        final String PREFERRED_STORE = "firstTimeUser";
    }

    interface Message {
        final String NO_INTERNET_CONNECTION = "No Internet Connection";
        final String LOADING = "Loading...";
        final String OK = "Ok";
        final String GREETING = "Hello!";
    }
}
