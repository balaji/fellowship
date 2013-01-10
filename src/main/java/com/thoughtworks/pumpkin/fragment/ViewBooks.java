package com.thoughtworks.pumpkin.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import com.actionbarsherlock.app.SherlockFragment;
import com.thoughtworks.pumpkin.BaseActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.ShopActivity;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.adapter.ShopsAdapter;
import com.thoughtworks.pumpkin.domain.Book;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBooks extends SherlockFragment {

    private ListView booksListView;
    private String wishList;
    private HashMap<String, String> forMapView;
    private ProgressDialog dialog;
    private PumpkinDB pumpkinDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return inflater.inflate(R.layout.books, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final String category = getActivity().getIntent().getStringExtra("category");
        pumpkinDB = new PumpkinDB(getActivity());
        wishList = getActivity().getIntent().getStringExtra("wishlist");
        String query = getActivity().getIntent().getStringExtra("query");

        Spinner spinner = (Spinner) view.findViewById(R.id.cart);
        List<String> shops = new PumpkinDB(getActivity()).getShops();
        shops.add(0, getResources().getString(R.string.view_cart));
        spinner.setAdapter(new ShopsAdapter<String>(getActivity(), R.layout.spin_shop_rowitem, shops));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) return;
                Intent intent = new Intent(getActivity(), ShopActivity.class);
                if (category != null) intent.putExtra("category", category);
                if (wishList != null) intent.putExtra("wishlist", wishList);
                intent.putExtra("books", forMapView);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        booksListView = (ListView) view.findViewById(R.id.books);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle((category != null) ? category : wishList);

        if (category != null) {
            findByCategory(category);
            return;
        }
        if (wishList != null) {
            findByWishList(wishList);
            return;
        }
        if (query != null) {
            searchBooks(query);
        }
    }

    private void findByWishList(String wishList) {
        dialog = Util.showProgressDialog(getActivity());
        loadBooks(pumpkinDB.getBooksByWishlist(wishList));
    }

    private void findByCategory(String category) {
        dialog = Util.showProgressDialog(getActivity());
        loadBooks(pumpkinDB.getBooksByCategory(category));
    }

    public void searchBooks(final String queryString) {
        dialog = Util.showProgressDialog(getActivity());
        List<Book> books = pumpkinDB.getBooksByKeyword(queryString);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("\"" + queryString + "\" - " + books.size() + " results");
        loadBooks(books);
    }

    private void loadBooks(List<Book> books) {
        ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        forMapView = new HashMap<String, String>();
        for (Book book : books) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("book", book);
            maps.add(map);
            forMapView.put(book.getTitle(), book.getCategory());
        }
        if (dialog.isShowing()) dialog.dismiss();
        booksListView.setAdapter(new BooksAdapter(getActivity(), maps, R.layout.book,
                new String[]{"bookImage", "title", "rank", "objectId", "snippet", "authors"},
                new int[]{R.id.bookImage, R.id.title, R.id.rank, R.id.heart, R.id.description, R.id.authors}));
    }
}
