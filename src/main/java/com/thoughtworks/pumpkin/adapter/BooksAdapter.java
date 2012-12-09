package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.fedorvlasov.lazylist.ImageLoader;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.listener.ImageButtonOnClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;

public class BooksAdapter extends SimpleAdapter {
    private ImageLoader imageLoader;
    private Context context;
    int count;

    public BooksAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        count = 0;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookViewHolder holder;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.book, null);
            holder = new BookViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.bookImage);
            holder.rating = (TextView) convertView.findViewById(R.id.rank);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.wishListButton = (ImageButton) convertView.findViewById(R.id.heart);
            holder.spinner = (ProgressBar) convertView.findViewById(R.id.heartLoading);
            holder.bookSpinner = (ProgressBar) convertView.findViewById(R.id.bookLoading);
            holder.position = position;
            convertView.setTag(holder);
        } else {
            holder = (BookViewHolder) convertView.getTag();
        }

        Map<String, Object> item = (Map<String, Object>) getItem(position);
        ParseObject book = (ParseObject) item.get("book");
        if ((Boolean) item.get("complete")) {
            fillView(position, holder, book);
        } else {
            new ParseQuery(Constant.ParseObject.BOOK).getInBackground(book.getObjectId(), new GetCallback(position, holder) {
                @Override
                public void done(ParseObject book, ParseException e) {
                    fillView(position, holder, book);
                }
            });
        }
        return convertView;
    }

    private void fillView(final int p, BookViewHolder h, ParseObject book) {
        h.bookSpinner.setVisibility(View.GONE);
        imageLoader.DisplayImage(book.getString(COLUMN.BOOK.THUMBNAIL), h.image);
        h.rating.setText(book.getString(COLUMN.BOOK.RATING));
        h.title.setText(book.getString(COLUMN.BOOK.TITLE));
        if (h.spinner.getVisibility() == View.GONE) {
            drawHeartIcon(h);
        } else {
            ParseQuery query = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
            ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.WISH_LIST);
            innerQuery.whereContainedIn(COLUMN.WISH_LIST.NAME, new PumpkinDB(context).getWishListColumn("name"));
            query.whereMatchesQuery(COLUMN.WISH_LIST_BOOK.WISH_LIST, innerQuery);
            query.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, book);
            query.findInBackground(new FindCallback(p, book.getObjectId(), h) {
                @Override
                public void done(List<ParseObject> wishListBookMappings, ParseException e) {
                    count++;
                    if (p == position) {
                        Map<String, ParseObject> mappings = new HashMap<String, ParseObject>();
                        for (ParseObject bookMapping : wishListBookMappings) {
                            mappings.put(bookMapping.getParseObject(COLUMN.WISH_LIST_BOOK.WISH_LIST).getObjectId(), bookMapping);
                        }
                        holder.wishListBooks = mappings;
                        drawHeartIcon(holder);
                    }
                }
            });
        }
        h.wishListButton.setOnClickListener(new ImageButtonOnClickListener(this, h, book));
    }

    private void drawHeartIcon(BookViewHolder holder) {
        if (holder.wishListBooks == null) return;
        holder.spinner.setVisibility(View.GONE);
        holder.wishListButton.setVisibility(View.VISIBLE);
        holder.wishListButton.setBackgroundDrawable(context.getResources()
                .getDrawable(holder.wishListBooks.isEmpty() ? R.drawable.ic_action_heart : R.drawable.ic_heart_filled));
    }

    public Context getContext() {
        return context;
    }

    abstract class GetCallback extends com.parse.GetCallback {
        int position;
        BookViewHolder holder;

        protected GetCallback(int position, BookViewHolder holder) {
            this.position = position;
            this.holder = holder;
        }
    }

    abstract class FindCallback extends com.parse.FindCallback {

        int position;
        String bookId;
        BookViewHolder holder;

        FindCallback(int position, String bookId, BookViewHolder holder) {
            this.position = position;
            this.bookId = bookId;
            this.holder = holder;
        }
    }
}
