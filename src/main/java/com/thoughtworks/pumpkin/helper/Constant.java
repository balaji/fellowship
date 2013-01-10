package com.thoughtworks.pumpkin.helper;

public interface Constant {

    final String DATABASE_NAME = "pumpkin.db";
    final int DATABASE_VERSION = 1;


    interface Preferences {
        final String USER_ID = "logged_in";
        final String PREFERRED_STORE = "firstTimeUser";
    }

    interface Message {
        final String NO_INTERNET_CONNECTION = "No Internet Connection";
        final String LOADING = "Loading...";
        final String OK = "Ok";
        final String CANCEL = "Cancel";
    }
}
