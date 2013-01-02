package com.thoughtworks.pumpkin;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.widget.SearchView;
import com.thoughtworks.pumpkin.fragment.ViewBooks;

public class ViewBooksActivity extends BaseActivity {

    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ViewBooks()).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.view_cart, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setFocusable(false);
        final ViewBooksActivity activity = this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(activity, ViewBooksActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                finish();
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
