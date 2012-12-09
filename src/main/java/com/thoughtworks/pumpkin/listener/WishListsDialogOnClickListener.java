package com.thoughtworks.pumpkin.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST_BOOK;

public class WishListsDialogOnClickListener implements AdapterView.OnItemClickListener {
    private BookViewHolder holder;
    private ImageButton imageButton;
    private ParseObject book;
    private BooksAdapter booksAdapter;

    public WishListsDialogOnClickListener(BooksAdapter booksAdapter, BookViewHolder holder, ImageButton imageButton, ParseObject book) {
        this.booksAdapter = booksAdapter;
        this.holder = holder;
        this.imageButton = imageButton;
        this.book = book;
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
                    holder.wishListBooks.put(id, wishListBook);
                }
            });
            imageButton.setBackgroundDrawable(booksAdapter.getContext().getResources().getDrawable(R.drawable.ic_heart_filled));
        } else {   //delete
            ParseObject wishListMapping = holder.wishListBooks.remove(id);
            wishListMapping.deleteInBackground();
            if (((ListView) adapterView).getCheckedItemCount() == 0) {
                imageButton.setBackgroundDrawable(booksAdapter.getContext().getResources().getDrawable(R.drawable.ic_action_heart));
            }
        }
    }
}
