package com.thoughtworks.pumpkin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;
import com.google.inject.Inject;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.adapter.BooksCursor;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BrowseActivity extends RoboActivity {

    @InjectView(R.id.books)
    GridView booksGridView;

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        String category = getIntent().getExtras().getString("category");
        String wishList = getIntent().getExtras().getString("wishlist");
        if (category != null) findByCategory(category);
        if (wishList != null) findByWishList(wishList);
    }

    private void findByWishList(String wishList) {
        ParseQuery fetchWishList = new ParseQuery(Constant.ParseObject.WISH_LIST);
        fetchWishList.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST.NAME, wishList);
        final BrowseActivity browseActivity = this;
        final ProgressDialog dialog = Util.showProgressDialog(browseActivity);
        fetchWishList.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishListObj, ParseException e) {
                ParseQuery query = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
                query.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, wishListObj.iterator().next());
                query.findInBackground(new FindCallback() {
                    @Override
                    public void done(List<ParseObject> wishListBooks, ParseException e) {
                        if (wishListBooks.isEmpty()) {
                            AlertDialog alertDialog = Util.dialog("No books in this list.", browseActivity);
                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (dialog.isShowing()) dialog.dismiss();
                                    finish();
                                }
                            });
                            alertDialog.show();
                        } else {
                            final List<ParseObject> books = new ArrayList<ParseObject>();
                            for (ParseObject wishListBook : wishListBooks) {
                                books.add(wishListBook.getParseObject(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK));
                            }
                            loadBooks(data(books, false), browseActivity, dialog);
                        }
                    }
                });
            }
        });
    }

    private void findByCategory(String category) {
        ParseQuery query = new ParseQuery(Constant.ParseObject.BOOK);
        ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.CATEGORY);
        innerQuery.whereEqualTo(Constant.ParseObject.COLUMN.BOOK.NAME, category);
        query.whereMatchesQuery("parent", innerQuery);
        query.orderByAscending(Constant.ParseObject.COLUMN.BOOK.RATING);
        final BrowseActivity browseActivity = this;
        final ProgressDialog dialog = Util.showProgressDialog(this);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                loadBooks(data(parseObjects, true), browseActivity, dialog);
            }
        });
    }

    private void loadBooks(final List<Map<String, Object>> data, final BrowseActivity browseActivity, final ProgressDialog dialog) {
        ParseQuery fetchAllBooksForUser = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
        ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.WISH_LIST);
        innerQuery.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST.USER, preferences.getString(Constant.Preferences.USER_ID, null));
        fetchAllBooksForUser.whereMatchesQuery(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, innerQuery);
        fetchAllBooksForUser.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishListBooks, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                Set<String> listOfAllBooksInWishList = new HashSet<String>();
                for (ParseObject wishListBook : wishListBooks) {
                    listOfAllBooksInWishList.add(wishListBook.getParseObject(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK).getObjectId());
                }
                setContentView(R.layout.books);
                booksGridView.setAdapter(new BooksCursor(listOfAllBooksInWishList, browseActivity, data, R.layout.book,
                        new String[]{"bookImage", "title", "rank", "objectId"}, new int[]{R.id.bookImage, R.id.title, R.id.rank, R.id.heart},
                        preferences.getString(Constant.Preferences.USER_ID, null)));
            }
        });
    }

    private List<Map<String, Object>> data(List<ParseObject> books, boolean fetchAll) {
        ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (ParseObject book : books) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("book", book);
            map.put("complete", fetchAll);
            maps.add(map);
        }
        return maps;
    }
}
