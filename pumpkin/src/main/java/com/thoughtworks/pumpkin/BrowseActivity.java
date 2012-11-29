package com.thoughtworks.pumpkin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.List;

public class BrowseActivity extends RoboActivity {

    @InjectView(R.id.books)
    GridView books;

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
        final BrowseActivity browseActivity = this;
        ParseQuery fetchWishList = new ParseQuery(Constant.ParseObject.WISH_LIST);
        fetchWishList.whereEqualTo("name", wishList);
        fetchWishList.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishListObj, ParseException e) {
                final ProgressDialog dialog = Util.showProgressDialog(browseActivity);
                ParseQuery query = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
                query.whereEqualTo("wishListId", wishListObj.iterator().next());
                query.findInBackground(new FindCallback() {
                    @Override
                    public void done(List<ParseObject> wishListBooks, ParseException e) {
                        if (dialog.isShowing()) dialog.dismiss();
                        if (wishListBooks.isEmpty()) {
                            AlertDialog alertDialog = Util.dialog("No books in this list.", browseActivity);
                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                        final List<ParseObject> books = new ArrayList<ParseObject>();
                        for (ParseObject wishListBook : wishListBooks) {
                            books.add(wishListBook.getParseObject("bookId"));
                        }
                        loadBooks(books, browseActivity);
                    }
                });
            }
        });
    }

    private void findByCategory(String category) {
        ParseQuery query = new ParseQuery(Constant.ParseObject.BOOK);
        ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.CATEGORY);
        innerQuery.whereEqualTo("name", category);
        query.whereMatchesQuery("parent", innerQuery);
        query.orderByAscending("rating");
        final BrowseActivity browseActivity = this;
        final ProgressDialog dialog = Util.showProgressDialog(this);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                loadBooks(parseObjects, browseActivity);
            }
        });
    }

    private void loadBooks(List<ParseObject> books, BrowseActivity browseActivity) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "bookImage", "title", "rank", "objectId"});
        if (Build.VERSION.SDK_INT > 10) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        for (int i = 0; i < books.size(); i++) {
            try {
                ParseObject book = books.get(i).fetchIfNeeded();
                cursor.addRow(new Object[]{i, book.getString("thumbnail"), book.getString("title"),
                        String.format("#%s", book.getString("rating")), book.getObjectId()});
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        setContentView(R.layout.books);
        this.books.setAdapter(new BooksCursor(browseActivity, R.layout.book, cursor,
                new String[]{"bookImage", "title", "rank", "objectId"}, new int[]{R.id.bookImage, R.id.title, R.id.rank, R.id.heart},
                preferences.getString(Constant.Preferences.USER_ID, null)));
    }
}
