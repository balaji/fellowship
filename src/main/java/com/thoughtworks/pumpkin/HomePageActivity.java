package com.thoughtworks.pumpkin;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.widget.SearchView;
import com.thoughtworks.pumpkin.fragment.HomePage;


public class HomePageActivity extends BaseActivity {

    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new HomePage()).commit();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            searchView.setIconifiedByDefault(false);
            searchView.setFocusable(true);
            searchView.requestFocus();
            searchView.requestFocusFromTouch();
            searchView.setSubmitButtonEnabled(true);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doMySearch(String Query) {
        Intent intent = new Intent(this, ViewBooksActivity.class);
        intent.putExtra("query", Query);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.view_cart, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                return false;
            }

        });

        return true;
    }

}
