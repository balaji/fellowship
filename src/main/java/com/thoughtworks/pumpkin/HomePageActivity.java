package com.thoughtworks.pumpkin;

import android.os.Bundle;
import com.actionbarsherlock.widget.SearchView;
import com.thoughtworks.pumpkin.fragment.HomePage;


public class HomePageActivity extends BaseActivity {

    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
    }
}
