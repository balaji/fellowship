package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.listener.ImageButtonOnClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;

public class BooksAdapter extends SimpleAdapter {
    private ImageLoader imageLoader;
    private Map<String, Integer> booksInWishlist;
    private String userId;
    private List<ParseObject> wishLists;
    private Context context;

    public BooksAdapter(Context context, List<? extends Map<String, ?>> data,
                        int resource, String[] from, int[] to, String userId) {
        super(context, data, resource, from, to);
        this.context = context;
        this.userId = userId;
        this.booksInWishlist = new HashMap<String, Integer>();
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
        if ((Boolean) item.get("complete")) {
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

    private void fillView(final ParseObject book, final BookViewHolder holder) {
        imageLoader.DisplayImage(book.getString(COLUMN.BOOK.THUMBNAIL), holder.image);
        holder.rating.setText(book.getString(COLUMN.BOOK.RATING));
        holder.title.setText(book.getString(COLUMN.BOOK.TITLE));
        if (booksInWishlist.keySet().contains(book.getObjectId())) {
            drawHeartIcon(book, holder);
        } else {
            booksInWishlist.put(book.getObjectId(), -1);
            ParseQuery query = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
            ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.WISH_LIST);
            innerQuery.whereContainedIn(COLUMN.WISH_LIST.NAME, getWishListIdsFromLocalDB());
            query.whereMatchesQuery(COLUMN.WISH_LIST_BOOK.WISH_LIST, innerQuery);
            query.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, book);
            query.findInBackground(new FindCallback() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    booksInWishlist.put(book.getObjectId(), parseObjects.size());
                    drawHeartIcon(book, holder);
                }
            });
        }
        holder.wishListButton.setOnClickListener(new ImageButtonOnClickListener(this, book));
    }

    private List<String> getWishListIdsFromLocalDB() {
        SQLiteDatabase database = new PumpkinDB(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("select name from " + PumpkinDB.WISH_LIST_TABLE_NAME, null);
        ArrayList<String> wishLists = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                wishLists.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return wishLists;
    }

    private void drawHeartIcon(ParseObject book, BookViewHolder holder) {
        int resourceId;
        if (booksInWishlist.get(book.getObjectId()) > 0) {
            resourceId = R.drawable.ic_heart_filled;
        } else if (booksInWishlist.get(book.getObjectId()) < 0) {
            resourceId = R.drawable.ic_action_heart;
        } else {
            resourceId = R.drawable.ic_action_heart;
        }
        holder.wishListButton.setBackgroundDrawable(context.getResources().getDrawable(resourceId));
    }

    public List<ParseObject> getWishLists() {
        return wishLists;
    }

    public void setWishLists(List<ParseObject> wishLists) {
        this.wishLists = wishLists;
    }

    public Context getContext() {
        return context;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getBooksInWishlist() {
        return booksInWishlist;
    }
}
