package com.thoughtworks.pumpkin.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;

import java.util.HashMap;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST_BOOK;

public class WishListsDialogOnClickListener implements AdapterView.OnItemClickListener {
    private ParseObject book;
    private Integer position;
    private BooksAdapter booksAdapter;

    public WishListsDialogOnClickListener(BooksAdapter booksAdapter, ParseObject book, Integer position) {
        this.booksAdapter = booksAdapter;
        this.book = book;
        this.position = position;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView textView = (CheckedTextView) view;
        final String id = new PumpkinDB(booksAdapter.getContext()).getWishListId(textView.getText().toString());
        if (!textView.isChecked()) {   //save
            new ParseQuery(Constant.ParseObject.WISH_LIST).getInBackground(id, new GetCallback() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    final ParseObject wishListBook = new ParseObject(WISH_LIST_BOOK);
                    wishListBook.put(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK, book);
                    wishListBook.put(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, parseObject);
                    wishListBook.saveInBackground();
                    booksAdapter.getChest().put(position, new HashMap<String, ParseObject>() {{
                        put(id, wishListBook);
                    }});
                    booksAdapter.notifyDataSetChanged();
                }
            });
        } else {   //delete
            ParseObject wishListMapping = booksAdapter.getChest().get(position).remove(id);
            wishListMapping.deleteInBackground();
            booksAdapter.notifyDataSetChanged();
        }
    }
}
