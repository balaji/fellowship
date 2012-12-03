package com.thoughtworks.pumpkin.listener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST_BOOK;

public class ImageButtonOnClickListener implements View.OnClickListener {
    private ParseObject book;
    private BooksAdapter booksAdapter;

    public ImageButtonOnClickListener(BooksAdapter booksAdapter, ParseObject book) {
        this.booksAdapter = booksAdapter;
        this.book = book;
    }

    @Override
    public void onClick(final View view) {
        final View layout = LayoutInflater.from(booksAdapter.getContext()).inflate(R.layout.choose_wishlist_dialog, null);
        final ListView listView = (ListView) layout.findViewById(R.id.wishListItems);
        final ProgressDialog dialog = Util.showProgressDialog((Activity) booksAdapter.getContext());
        if (booksAdapter.getWishLists() == null) {
            Parse.initialize(booksAdapter.getContext(), Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
            ParseQuery wishListQuery = new ParseQuery(WISH_LIST);
            wishListQuery.orderByAscending(Constant.ParseObject.COLUMN.WISH_LIST.NAME);
            wishListQuery.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST.USER, booksAdapter.getUserId());
            wishListQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
            wishListQuery.findInBackground(new FindCallback() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    booksAdapter.setWishLists(parseObjects);
                    setListViewOnDialog(dialog, listView, booksAdapter.getContext(), layout, book, (ImageButton) view);
                }
            });
        } else {
            setListViewOnDialog(dialog, listView, booksAdapter.getContext(), layout, book, (ImageButton) view);
        }
    }

    private void setListViewOnDialog(final ProgressDialog dialog, final ListView listView,
                                     final Context context, final View layout, final ParseObject chosenBook, final ImageButton imageButton) {
        final String[] strings = new String[booksAdapter.getWishLists().size()];
        for (int i = 0; i < booksAdapter.getWishLists().size(); i++) {
            strings[i] = booksAdapter.getWishLists().get(i).get(Constant.ParseObject.COLUMN.WISH_LIST.NAME).toString();
        }

        ParseQuery queryToFetch = new ParseQuery(WISH_LIST_BOOK);
        queryToFetch.whereContainedIn(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST, booksAdapter.getWishLists());
        queryToFetch.whereEqualTo(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
        queryToFetch.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishListBookMappings, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, strings));
                setItemsInListAsChecked(listView, wishListBookMappings);
                listView.setOnItemClickListener(new WishListsDialogOnClickListener(booksAdapter, chosenBook, imageButton));
                Util.dialog(context, layout).show();
            }
        });
    }

    private void setItemsInListAsChecked(ListView listView, List<ParseObject> wishListBookMappings) {
        List<String> wishListIds = new ArrayList<String>();
        for (ParseObject wishList : booksAdapter.getWishLists()) {
            wishListIds.add(wishList.getObjectId());
        }
        for (ParseObject wishListBookMapping : wishListBookMappings) {
            ParseObject wishListObj = wishListBookMapping.getParseObject(Constant.ParseObject.COLUMN.WISH_LIST_BOOK.WISH_LIST);
            if (wishListIds.contains(wishListObj.getObjectId())) {
                listView.setItemChecked(wishListIds.indexOf(wishListObj.getObjectId()), true);
            }
        }
    }
}
