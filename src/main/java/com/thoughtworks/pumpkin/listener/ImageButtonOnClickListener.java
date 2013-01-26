package com.thoughtworks.pumpkin.listener;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.domain.Book;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.Collection;
import java.util.List;

public class ImageButtonOnClickListener implements View.OnClickListener {
    private BookViewHolder holder;
    private Book book;
    private BooksAdapter booksAdapter;
    private PumpkinDB pumpkinDB;
    private List<String> wishlistNames;

    public ImageButtonOnClickListener(BooksAdapter booksAdapter, BookViewHolder holder, Book book) {
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
        wishlistNames = pumpkinDB.getWishlistNames();
        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, wishlistNames));
        setItemsInListAsChecked(listView, holder.wishListBooks);
        listView.setOnItemClickListener(new WishListsDialogOnClickListener(booksAdapter, book, holder.position));
        Util.dialog(context, layout).show();
    }

    private void setItemsInListAsChecked(ListView listView, Collection<String> chosenWishlists) {
        for (String chosenListName : chosenWishlists) {
            if (wishlistNames.contains(chosenListName)) {
                listView.setItemChecked(wishlistNames.indexOf(chosenListName), true);
            }
        }
    }
}
