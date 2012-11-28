package com.thoughtworks.pumpkin.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.ArrayList;
import java.util.List;

public class BooksCursor extends SimpleCursorAdapter {
    private AlphabetIndexer alphaIndexer;
    private ImageLoader imageLoader;
    private String userId;
    private List<ParseObject> wishLists;

    public BooksCursor(Context context, int layout, Cursor c, String[] from, int[] to, String userId) {
        super(context, layout, c, from, to);
        this.userId = userId;
        alphaIndexer = new AlphabetIndexer(c, 1, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        imageLoader = new ImageLoader(context);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        imageLoader.DisplayImage(cursor.getString(cursor.getColumnIndex("bookImage")), (ImageView) view.findViewById(R.id.bookImage));
        setViewText((TextView) view.findViewById(R.id.title), cursor.getString(cursor.getColumnIndex("title")));
        setViewText((TextView) view.findViewById(R.id.rank), cursor.getString(cursor.getColumnIndex("rank")));
        View heartButton = view.findViewById(R.id.heart);
        heartButton.setTag(cursor.getString(cursor.getColumnIndex("objectId")));
        heartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String bookObjectId = view.getTag().toString();
                final View layout = LayoutInflater.from(context).inflate(R.layout.choose_wishlist_dialog, null);
                final ListView listView = (ListView) layout.findViewById(R.id.wishListItems);
                final ProgressDialog dialog = Util.showProgressDialog((Activity) context);
                if (wishLists == null) {
                    Parse.initialize(context, Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
                    ParseQuery wishListQuery = new ParseQuery(Constant.ParseObject.WISH_LIST);
                    wishListQuery.orderByAscending("name");
                    wishListQuery.whereEqualTo("owner", userId);
                    wishListQuery.findInBackground(new FindCallback() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            wishLists = parseObjects;
                            setListView(dialog, listView, context, layout, bookObjectId);
                        }
                    });
                } else {
                    setListView(dialog, listView, context, layout, bookObjectId);
                }
            }
        });
    }

    private void showDialog(Context context, final View layout, final String bookObjectId) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Options:");
        alertDialog.setView(layout);
        alertDialog.setPositiveButton(Constant.Message.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void setListView(final ProgressDialog dialog, final ListView listView,
                             final Context context, final View layout, final String bookObjectId) {
        final String[] strings = new String[wishLists.size()];
        for (int i = 0; i < wishLists.size(); i++) {
            strings[i] = wishLists.get(i).get("name").toString();
        }
        ParseQuery queryToFetch = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
        queryToFetch.whereContainedIn("wishListId", wishListIds());
        queryToFetch.whereEqualTo("bookId", bookObjectId);
        queryToFetch.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, strings));
                setItemsInListAsChecked(listView, parseObjects);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (!((CheckedTextView) view).isChecked()) {
                            ParseObject parseObject = new ParseObject(Constant.ParseObject.WISH_LIST_BOOK);
                            parseObject.put("bookId", bookObjectId);
                            parseObject.put("wishListId", wishListIds().get(i));
                            parseObject.saveInBackground();
                        } else {
                            ParseQuery queryToDelete = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
                            queryToDelete.whereEqualTo("bookId", bookObjectId);
                            queryToDelete.whereEqualTo("wishListId", wishListIds().get(i));
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
                });
                showDialog(context, layout, bookObjectId);
            }
        });
    }

    private void setItemsInListAsChecked(ListView listView, List<ParseObject> wishListBookMappings) {
        List<String> wishListIds = new ArrayList<String>();
        for (ParseObject wishList : wishLists) {
            wishListIds.add(wishList.getObjectId());
        }
        for (ParseObject wishListBookMapping : wishListBookMappings) {
            if (wishListIds.contains(wishListBookMapping.getString("wishListId"))) {
                listView.setItemChecked(wishListIds.indexOf(wishListBookMapping.getString("wishListId")), true);
            }
        }
    }

    private List<String> wishListIds() {
        ArrayList<String> strings = new ArrayList<String>();
        for (ParseObject wishList : wishLists) {
            strings.add(wishList.getObjectId());
        }
        return strings;
    }
}
