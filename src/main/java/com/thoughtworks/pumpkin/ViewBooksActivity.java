package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.thoughtworks.pumpkin.fragment.ViewBooks;

public class ViewBooksActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ViewBooks()).commit();
    }
}
