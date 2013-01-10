package com.thoughtworks.pumpkin.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.domain.Book;
import com.thoughtworks.pumpkin.helper.PumpkinDB;

public class WishListsDialogOnClickListener implements AdapterView.OnItemClickListener {
    private Book book;
    private Integer position;
    private BooksAdapter booksAdapter;

    public WishListsDialogOnClickListener(BooksAdapter booksAdapter, Book book, Integer position) {
        this.booksAdapter = booksAdapter;
        this.book = book;
        this.position = position;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView textView = (CheckedTextView) view;
        PumpkinDB pumpkinDB = new PumpkinDB(booksAdapter.getContext());
        Integer id = pumpkinDB.getWishListId(textView.getText().toString());
        if (!textView.isChecked()) {   //save
            pumpkinDB.insertWishListMapping(id, book.getId());
        } else {   //delete
            pumpkinDB.remvoeWishListMapping(id, book.getId());
        }
        booksAdapter.notifyDataSetChanged();
    }
}
