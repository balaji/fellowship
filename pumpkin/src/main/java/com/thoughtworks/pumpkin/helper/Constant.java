package com.thoughtworks.pumpkin.helper;

public interface Constant {

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

    interface ParseObject {
        final String WISH_LIST = "WishLists";
        final String CATEGORY = "Category";
        final String BOOK = "Book";
        final String WISH_LIST_BOOK = "WishList_Book";
    }
}
