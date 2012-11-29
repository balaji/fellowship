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

        interface COLUMN {
            interface WISH_LIST {
                final String NAME = "name";
                final String USER = "owner";
            }

            interface WISH_LIST_BOOK {
                final String BOOK = "bookId";
                final String WISH_LIST = "wishListId";
            }

            interface BOOK {
                final String NAME = "name";
                final String RATING = "rating";
                final String TITLE = "title";
                final String THUMBNAIL = "thumbnail";
            }

            interface CATEGORY {
                final String NAME = "name";
            }
        }

        final String CATEGORY = "Category";
        final String BOOK = "Book";
        final String WISH_LIST_BOOK = "WishList_Book";
    }
}
