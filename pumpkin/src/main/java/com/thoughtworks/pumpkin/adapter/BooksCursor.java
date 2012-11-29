package com.thoughtworks.pumpkin.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.BOOK;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST_BOOK;

public class BooksCursor extends SimpleCursorAdapter {
    private ImageLoader imageLoader;
    private List<String> listOfAllBooksInWishList;
    private String userId;
    private List<ParseObject> wishLists;

    public BooksCursor(List<String> listOfAllBooksInWishList, Context context, int layout, Cursor c, String[] from, int[] to, String userId) {
        super(context, layout, c, from, to);
        this.listOfAllBooksInWishList = listOfAllBooksInWishList;
        this.userId = userId;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        imageLoader.DisplayImage(cursor.getString(cursor.getColumnIndex("bookImage")), (ImageView) view.findViewById(R.id.bookImage));
        setViewText((TextView) view.findViewById(R.id.title), cursor.getString(cursor.getColumnIndex("title")));
        setViewText((TextView) view.findViewById(R.id.rank), cursor.getString(cursor.getColumnIndex("rank")));
        ImageButton heartButton = (ImageButton) view.findViewById(R.id.heart);
        final String bookObjectId = cursor.getString(cursor.getColumnIndex("objectId"));
        if (listOfAllBooksInWishList.contains(bookObjectId)) {
            heartButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_heart_filled));
        } else {
            heartButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_action_heart));
        }
        heartButton.setTag(bookObjectId);
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View layout = LayoutInflater.from(context).inflate(R.layout.choose_wishlist_dialog, null);
                final ListView listView = (ListView) layout.findViewById(R.id.wishListItems);
                final ProgressDialog dialog = Util.showProgressDialog((Activity) context);
                if (wishLists == null) {
                    Parse.initialize(context, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
                    ParseQuery wishListQuery = new ParseQuery(WISH_LIST);
                    wishListQuery.orderByAscending(COLUMN.WISH_LIST.NAME);
                    wishListQuery.whereEqualTo(COLUMN.WISH_LIST.USER, userId);
                    wishListQuery.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK);
                    wishListQuery.findInBackground(new FindCallback() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            wishLists = parseObjects;
                            setListViewOnDialog(dialog, listView, context, layout, bookObjectId);
                        }
                    });
                } else {
                    setListViewOnDialog(dialog, listView, context, layout, bookObjectId);
                }
            }
        });
    }

    private void setListViewOnDialog(final ProgressDialog dialog, final ListView listView,
                                     final Context context, final View layout, final String bookObjectId) {
        final String[] strings = new String[wishLists.size()];
        for (int i = 0; i < wishLists.size(); i++) {
            strings[i] = wishLists.get(i).get(COLUMN.WISH_LIST.NAME).toString();
        }

        new ParseQuery(BOOK).getInBackground(bookObjectId, new GetCallback() {
            @Override
            public void done(final ParseObject chosenBook, ParseException e) {
                ParseQuery queryToFetch = new ParseQuery(WISH_LIST_BOOK);
                queryToFetch.whereContainedIn(COLUMN.WISH_LIST_BOOK.WISH_LIST, wishLists);
                queryToFetch.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
                queryToFetch.findInBackground(new FindCallback() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (dialog.isShowing()) dialog.dismiss();
                        listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, strings));
                        setItemsInListAsChecked(listView, parseObjects);
                        listView.setOnItemClickListener(new DialogListOnClickListener(chosenBook));
                        Util.dialog(context, layout).show();
                    }
                });
            }
        });
    }

    class DialogListOnClickListener implements AdapterView.OnItemClickListener {
        ParseObject chosenBook;

        DialogListOnClickListener(ParseObject chosenBook) {
            this.chosenBook = chosenBook;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (!((CheckedTextView) view).isChecked()) {   //save
                ParseObject parseObject = new ParseObject(WISH_LIST_BOOK);
                parseObject.put(COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
                parseObject.put(COLUMN.WISH_LIST_BOOK.WISH_LIST, wishLists.get(i));
                parseObject.saveInBackground();
            } else {   //delete
                ParseQuery queryToDelete = new ParseQuery(WISH_LIST_BOOK);
                queryToDelete.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
                queryToDelete.whereEqualTo(COLUMN.WISH_LIST_BOOK.WISH_LIST, wishLists.get(i));
                queryToDelete.findInBackground(new FindCallback() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        for (ParseObject parseObject : parseObjects) {
                            parseObject.deleteInBackground();
                        }
                    }
                });
            }
        }
    }

    private void setItemsInListAsChecked(ListView listView, List<ParseObject> wishListBookMappings) {
        List<String> wishListIds = new ArrayList<String>();
        for (ParseObject wishList : wishLists) {
            wishListIds.add(wishList.getObjectId());
        }
        for (ParseObject wishListBookMapping : wishListBookMappings) {
            ParseObject wishListObj = wishListBookMapping.getParseObject(COLUMN.WISH_LIST_BOOK.WISH_LIST);
            if (wishListIds.contains(wishListObj.getObjectId())) {
                listView.setItemChecked(wishListIds.indexOf(wishListObj.getObjectId()), true);
            }
        }
    }
}
