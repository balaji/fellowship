package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.google.inject.Inject;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.inject.ContentView;
import roboguice.util.SafeAsyncTask;

import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.CATEGORY;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.SHOP;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;

@ContentView(R.layout.splash)
public class SplashActivity extends RoboSherlockActivity {

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        final SplashActivity splashActivity = this;
        final PumpkinDB pumpkinDB = new PumpkinDB(splashActivity);
        new SafeAsyncTask() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(2000);
                return null;
            }

            @Override
            protected void onSuccess(Object o) throws Exception {
                startActivity(new Intent(splashActivity, SigninActivity.class));
            }
        }.execute();

        if (!Util.isConnectingToInternet(this)) {
            return;
        }

        ParseQuery query = new ParseQuery(CATEGORY);
        query.orderByAscending(Constant.ParseObject.COLUMN.CATEGORY.NAME);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> categories, ParseException e) {
                pumpkinDB.resetBookCategories(categories);
            }
        });

        ParseQuery shopQuery = new ParseQuery(SHOP);
        shopQuery.orderByAscending(Constant.ParseObject.COLUMN.SHOP.NAME);
        shopQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> shops, ParseException e) {
                pumpkinDB.resetShops(shops);
            }
        });

        String userId = preferences.getString(Constant.Preferences.USER_ID, null);
        if (userId == null) {
            return;
        }
        ParseQuery wishListQuery = new ParseQuery(WISH_LIST);
        wishListQuery.orderByAscending(Constant.ParseObject.COLUMN.WISH_LIST.NAME);
        wishListQuery.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST.USER, userId);
        wishListQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishLists, ParseException e) {
                pumpkinDB.resetWishLists(wishLists);
            }
        });
    }
}
