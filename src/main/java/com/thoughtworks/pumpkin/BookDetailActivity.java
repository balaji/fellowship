package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.thoughtworks.pumpkin.fragment.BookDetail;

import java.util.ArrayList;

public class BookDetailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
       ArrayList<String> book=getIntent().getStringArrayListExtra("book");
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new BookDetail(book)).commit();
    }
}
