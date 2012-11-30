package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.listener.ImageButtonOnClickListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;

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

    private void fillView(final ParseObject book, BookViewHolder holder) {
        imageLoader.DisplayImage(book.getString(COLUMN.BOOK.THUMBNAIL), holder.image);
        holder.rating.setText(book.getString(COLUMN.BOOK.RATING));
        holder.title.setText(book.getString(COLUMN.BOOK.TITLE));
        holder.wishListButton.setBackgroundDrawable(context.getResources().getDrawable((listOfAllBooksInWishList.contains(book.getObjectId())) ?
                R.drawable.ic_heart_filled : R.drawable.ic_action_heart));
        holder.wishListButton.setOnClickListener(new ImageButtonOnClickListener(this, book));
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

    public Set<String> getListOfAllBooksInWishList() {
        return listOfAllBooksInWishList;
    }
}
