package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBooks extends RoboFragment {

    GridView booksGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.books, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        String category = getActivity().getIntent().getExtras().getString("category");
        String wishList = getActivity().getIntent().getExtras().getString("wishlist");
        booksGridView = (GridView) view.findViewById(R.id.books);
        if (category != null) findByCategory(category);
        if (wishList != null) findByWishList(wishList);
    }

    private void findByWishList(String wishList) {
        final ProgressDialog dialog = Util.showProgressDialog(getActivity());
        ParseQuery query = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
        ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.WISH_LIST);
        innerQuery.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST.NAME, wishList);
        query.whereMatchesQuery(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, innerQuery);
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishListBooks, ParseException e) {
                if (wishListBooks.isEmpty()) {
                    AlertDialog alertDialog = Util.dialog("No books in this list.", getActivity());
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (dialog.isShowing()) dialog.dismiss();
                            getActivity().finish();
                        }
                    });
                    alertDialog.show();
                } else {
                    final List<ParseObject> books = new ArrayList<ParseObject>();
                    for (ParseObject wishListBook : wishListBooks) {
                        books.add(wishListBook.getParseObject(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK));
                    }
                    loadBooks(data(books, false), dialog);
                }
            }
        });
    }

    private void findByCategory(String category) {
        ParseQuery query = new ParseQuery(Constant.ParseObject.BOOK);
        ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.CATEGORY);
        innerQuery.whereEqualTo(Constant.ParseObject.COLUMN.BOOK.NAME, category);
        query.whereMatchesQuery("parent", innerQuery);
        query.orderByAscending(Constant.ParseObject.COLUMN.BOOK.RATING);
        final ProgressDialog dialog = Util.showProgressDialog(getActivity());
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                loadBooks(data(parseObjects, true), dialog);
            }
        });
    }

    private void loadBooks(final List<Map<String, Object>> data, final ProgressDialog dialog) {
        if (dialog.isShowing()) dialog.dismiss();
        booksGridView.setAdapter(new BooksAdapter(getActivity(), data, R.layout.book,
                new String[]{"bookImage", "title", "rank", "objectId"}, new int[]{R.id.bookImage, R.id.title, R.id.rank, R.id.heart}));
    }

    private List<Map<String, Object>> data(List<ParseObject> books, boolean fetchAll) {
        ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (ParseObject book : books) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("book", book);
            map.put("complete", fetchAll);
            maps.add(map);
        }
        return maps;
    }
}
