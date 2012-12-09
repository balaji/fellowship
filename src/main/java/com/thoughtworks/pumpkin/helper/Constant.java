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

    interface ParseObject {
        final String WISH_LIST = "WishLists";
        final String CATEGORY = "Category";
        final String BOOK = "Book";
        final String WISH_LIST_BOOK = "WishList_Book";
        final String SHOP = "Shop";

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

            interface SHOP {
                final String NAME = "name";
            }
        }
    }
}
