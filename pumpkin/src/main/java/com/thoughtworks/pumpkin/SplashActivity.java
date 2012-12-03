package com.thoughtworks.pumpkin;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.db.AllCategories;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.util.SafeAsyncTask;

import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.CATEGORY;

@ContentView(R.layout.splash)
public class SplashActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashActivity splashActivity = this;
        new SafeAsyncTask() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(1300);
                return null;
            }

            @Override
            protected void onSuccess(Object o) throws Exception {
                startActivity(new Intent(splashActivity, SigninActivity.class));
            }
        }.execute();

        Parse.initialize(this, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        ParseQuery query = new ParseQuery(CATEGORY);
        query.orderByAscending(Constant.ParseObject.COLUMN.CATEGORY.NAME);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> categories, ParseException e) {
                AllCategories allCategories = new AllCategories(splashActivity);
                SQLiteDatabase database = allCategories.getWritableDatabase();
                database.execSQL("delete from " + AllCategories.TABLE_NAME);
                for (ParseObject category : categories) {
                    ContentValues values = new ContentValues();
                    values.put(Constant.ParseObject.COLUMN.CATEGORY.NAME, category.getString(Constant.ParseObject.COLUMN.CATEGORY.NAME));
                    database.insert(AllCategories.TABLE_NAME, Constant.ParseObject.COLUMN.CATEGORY.NAME, values);
                }
            }
        });
    }
}
