package com.thoughtworks.pumpkin.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.adapter.BooksCursor;
import com.thoughtworks.pumpkin.helper.Constant;

import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST_BOOK;

public class WishListsDialogOnClickListener implements AdapterView.OnItemClickListener {
    private ParseObject chosenBook;
    private ImageButton holder;
    private BooksCursor booksCursor;

    public WishListsDialogOnClickListener(BooksCursor booksCursor, ParseObject chosenBook, ImageButton holder) {
        this.booksCursor = booksCursor;
        this.chosenBook = chosenBook;
        this.holder = holder;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!((CheckedTextView) view).isChecked()) {   //save
            ParseObject wishListBook = new ParseObject(WISH_LIST_BOOK);
            wishListBook.put(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
            wishListBook.put(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, booksCursor.getWishLists().get(i));
            wishListBook.saveInBackground();
            holder.setBackgroundDrawable(booksCursor.getContext().getResources().getDrawable(R.drawable.ic_heart_filled));
            booksCursor.getListOfAllBooksInWishList().add(chosenBook.getObjectId());
        } else {   //delete
            ParseQuery queryToDelete = new ParseQuery(WISH_LIST_BOOK);
            queryToDelete.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
            queryToDelete.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, booksCursor.getWishLists().get(i));
            queryToDelete.findInBackground(new FindCallback() {
                @Override
                public void done(List<ParseObject> wishListBookMappings, ParseException e) {
                    for (ParseObject wishListMapping : wishListBookMappings) {
                        wishListMapping.deleteInBackground();
                    }
                }
            });
            if (((ListView) adapterView).getCheckedItemCount() == 0) {
                holder.setBackgroundDrawable(booksCursor.getContext().getResources().getDrawable(R.drawable.ic_action_heart));
                booksCursor.getListOfAllBooksInWishList().remove(chosenBook.getObjectId());
            }
        }
    }
}