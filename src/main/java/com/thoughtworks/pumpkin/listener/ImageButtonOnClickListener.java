package com.thoughtworks.pumpkin.listener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.parse.ParseObject;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.Collection;
import java.util.List;

public class ImageButtonOnClickListener implements View.OnClickListener {
    private BookViewHolder holder;
    private ParseObject book;
    private BooksAdapter booksAdapter;
    private PumpkinDB pumpkinDB;

    public ImageButtonOnClickListener(BooksAdapter booksAdapter, BookViewHolder holder, ParseObject book) {
        this.booksAdapter = booksAdapter;
        this.holder = holder;
        this.book = book;
        pumpkinDB = new PumpkinDB(booksAdapter.getContext());
    }

    @Override
    public void onClick(final View view) {
        final View layout = LayoutInflater.from(booksAdapter.getContext()).inflate(R.layout.choose_wishlist_dialog, null);
        final ListView listView = (ListView) layout.findViewById(R.id.wishListItems);
        final Context context = booksAdapter.getContext();
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, pumpkinDB.getWishListColumn("name")));
         setItemsInListAsChecked(listView, holder.wishListBooks.values());
        listView.setOnItemClickListener(new WishListsDialogOnClickListener(booksAdapter, holder, (ImageButton) view, book));
        Util.dialog(context, layout).show();
    }

    private void setItemsInListAsChecked(ListView listView, Collection<ParseObject> wishListBookMappings) {
        List<String> wishListIds = pumpkinDB.getWishListColumn("id");
        for (ParseObject wishListBookMapping : wishListBookMappings) {
            ParseObject wishListObj = wishListBookMapping.getParseObject(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST);
            if (wishListIds.contains(wishListObj.getObjectId())) {
                listView.setItemChecked(wishListIds.indexOf(wishListObj.getObjectId()), true);
            }
        }
    }
}
