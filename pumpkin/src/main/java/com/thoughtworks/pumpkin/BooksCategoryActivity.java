package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.thoughtworks.pumpkin.fragment.BooksCategory;
import roboguice.activity.RoboFragmentActivity;

public class BooksCategoryActivity extends RoboFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BooksCategory booksCategory = new BooksCategory();
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, booksCategory).commit();
    }
}
