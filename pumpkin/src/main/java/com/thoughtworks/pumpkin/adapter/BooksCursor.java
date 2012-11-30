package com.thoughtworks.pumpkin.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST_BOOK;

public class BooksCursor extends SimpleAdapter {
    private ImageLoader imageLoader;
    private Set<String> listOfAllBooksInWishList;
    private String userId;
    private List<ParseObject> wishLists;
    private Context context;

    public BooksCursor(Set<String> listOfAllBooksInWishList, Context context, List<? extends Map<String, ?>> data,
                       int resource, String[] from, int[] to, String userId) {
        super(context, data, resource, from, to);
        this.context = context;
        this.listOfAllBooksInWishList = listOfAllBooksInWishList;
        this.userId = userId;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final BookViewHolder holder;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.book, null);
            holder = new BookViewHolder();
            holder.image = (ImageView) v.findViewById(R.id.bookImage);
            holder.rating = (TextView) v.findViewById(R.id.rank);
            holder.title = (TextView) v.findViewById(R.id.title);
            holder.wishListButton = (ImageButton) v.findViewById(R.id.heart);
            v.setTag(holder);
        } else {
            holder = (BookViewHolder) v.getTag();
        }

        final Map<String, Object> item = (Map<String, Object>) getItem(position);
        ParseObject book = (ParseObject) item.get("book");
        Boolean fetchAll = (Boolean) item.get("complete");
        if (fetchAll) {
            fillView(book, holder);
        } else {
            new ParseQuery(Constant.ParseObject.BOOK).getInBackground(book.getObjectId(), new GetCallback() {
                @Override
                public void done(ParseObject book, ParseException e) {
                    fillView(book, holder);
                }
            });
        }
        return v;
    }

    private void fillView(final ParseObject book, BookViewHolder holder) {
        imageLoader.DisplayImage(book.getString(COLUMN.BOOK.THUMBNAIL), holder.image);
        holder.rating.setText(book.getString(COLUMN.BOOK.RATING));
        holder.title.setText(book.getString(COLUMN.BOOK.TITLE));
        holder.wishListButton.setBackgroundDrawable(context.getResources().getDrawable((listOfAllBooksInWishList.contains(book.getObjectId())) ?
                R.drawable.ic_heart_filled : R.drawable.ic_action_heart));
        holder.wishListButton.setOnClickListener(new ImageButtonOnClickListener(book));
    }

    class ImageButtonOnClickListener implements View.OnClickListener {
        private ParseObject book;

        ImageButtonOnClickListener(ParseObject book) {
            this.book = book;
        }

        @Override
        public void onClick(final View view) {
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
                        setListViewOnDialog(dialog, listView, context, layout, book, (ImageButton) view);
                    }
                });
            } else {
                setListViewOnDialog(dialog, listView, context, layout, book, (ImageButton) view);
            }
        }
    }

    private void setListViewOnDialog(final ProgressDialog dialog, final ListView listView,
                                     final Context context, final View layout, final ParseObject chosenBook, final ImageButton imageButton) {
        final String[] strings = new String[wishLists.size()];
        for (int i = 0; i < wishLists.size(); i++) {
            strings[i] = wishLists.get(i).get(COLUMN.WISH_LIST.NAME).toString();
        }

        ParseQuery queryToFetch = new ParseQuery(WISH_LIST_BOOK);
        queryToFetch.whereContainedIn(COLUMN.WISH_LIST_BOOK.WISH_LIST, wishLists);
        queryToFetch.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
        queryToFetch.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishListBookMappings, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, strings));
                setItemsInListAsChecked(listView, wishListBookMappings);
                listView.setOnItemClickListener(new WishListsDialogOnClickListener(chosenBook, imageButton));
                Util.dialog(context, layout).show();
            }
        });
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

    private class WishListsDialogOnClickListener implements AdapterView.OnItemClickListener {
        private ParseObject chosenBook;
        private ImageButton holder;

        public WishListsDialogOnClickListener(ParseObject chosenBook, ImageButton holder) {
            this.chosenBook = chosenBook;
            this.holder = holder;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (!((CheckedTextView) view).isChecked()) {   //save
                ParseObject wishListBook = new ParseObject(WISH_LIST_BOOK);
                wishListBook.put(COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
                wishListBook.put(COLUMN.WISH_LIST_BOOK.WISH_LIST, wishLists.get(i));
                wishListBook.saveInBackground();
                holder.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_heart_filled));
                listOfAllBooksInWishList.add(chosenBook.getObjectId());
            } else {   //delete
                ParseQuery queryToDelete = new ParseQuery(WISH_LIST_BOOK);
                queryToDelete.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, chosenBook);
                queryToDelete.whereEqualTo(COLUMN.WISH_LIST_BOOK.WISH_LIST, wishLists.get(i));
                queryToDelete.findInBackground(new FindCallback() {
                    @Override
                    public void done(List<ParseObject> wishListBookMappings, ParseException e) {
                        for (ParseObject wishListMapping : wishListBookMappings) {
                            wishListMapping.deleteInBackground();
                        }
                    }
                });
                if (((ListView) adapterView).getCheckedItemCount() == 0) {
                    holder.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_action_heart));
                    listOfAllBooksInWishList.remove(chosenBook.getObjectId());
                }
            }
        }
    }
}
