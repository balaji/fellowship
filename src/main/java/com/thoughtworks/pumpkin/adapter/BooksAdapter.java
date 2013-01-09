package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
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
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;

public class BooksAdapter extends SimpleAdapter {
    private ImageLoader imageLoader;
    private Context context;
    private Map<Integer, Map<String, ParseObject>> chest;

    public BooksAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        imageLoader = new ImageLoader(context);
        chest = new HashMap<Integer, Map<String, ParseObject>>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.book, null);
            holder = new BookViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.bookImage);
            holder.rating = (TextView) convertView.findViewById(R.id.rank);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.authors = (TextView) convertView.findViewById(R.id.authors);
            holder.wishListButton = (ImageButton) convertView.findViewById(R.id.heart);
            holder.spinner = (ProgressBar) convertView.findViewById(R.id.heartLoading);
            holder.position = position;
            holder.wishListBooks = new HashMap<String, ParseObject>();
            convertView.setTag(holder);
        } else {
            holder = (BookViewHolder) convertView.getTag();
        }

        Map<String, Object> item = (Map<String, Object>) getItem(position);
        ParseObject book = (ParseObject) item.get("book");
        fillView(position, holder, book);
        return convertView;
    }

    private void fillView(int position, BookViewHolder holder, ParseObject book) {
        imageLoader.DisplayImage(book.getString(COLUMN.BOOK.THUMBNAIL), holder.image);
        holder.rating.setText("#" + book.getInt(COLUMN.BOOK.RATING));
        holder.title.setText(book.getString(COLUMN.BOOK.TITLE));
        try {
            JSONArray jsonArray = book.getJSONArray(COLUMN.BOOK.AUTHORS);
            if (jsonArray != null) {
                holder.authors.setText("- " + jsonArray.join(", ").replaceAll("\"", ""));
            } else {
                holder.authors.setText("");
            }
        } catch (JSONException e) {
        }
        holder.description.setText(Html.fromHtml(book.getString(COLUMN.BOOK.SNIPPET)));
        if (chest.keySet().contains(position)) {
            drawHeartIcon(holder, position);
        } else {
            chest.put(position, null);
            heartIcon(false, holder);
            new LoadWishListInfoAsync().execute(book, position);
        }
        holder.wishListButton.setOnClickListener(new ImageButtonOnClickListener(this, holder, book));
    }

    private void drawHeartIcon(BookViewHolder holder, int position) {
        if (chest.get(position) == null) {
            heartIcon(false, holder);
        } else {
            heartIcon(true, holder);
            holder.wishListBooks.clear();
            holder.wishListBooks.putAll(chest.get(position));
            holder.wishListButton.setBackgroundDrawable(context.getResources()
                    .getDrawable(chest.get(position).isEmpty() ? R.drawable.ic_action_heart : R.drawable.ic_heart_filled));
        }
    }

    private void heartIcon(boolean toggle, BookViewHolder holder) {
        holder.spinner.setVisibility(!toggle ? View.VISIBLE : View.GONE);
        holder.wishListButton.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    public Context getContext() {
        return context;
    }

    public Map<Integer, Map<String, ParseObject>> getChest() {
        return chest;
    }

    abstract class GetCallback extends com.parse.GetCallback {
        int position;
        BookViewHolder holder;

        protected GetCallback(int position, BookViewHolder holder) {
            this.position = position;
            this.holder = holder;
        }
    }

    class LoadWishListInfoAsync extends AsyncTask<Object, Void, List<Object>> {

        @Override
        protected void onPostExecute(List<Object> objects) {
            super.onPostExecute(objects);
            Map<String, ParseObject> mappings = new HashMap<String, ParseObject>();
            for (ParseObject bookMapping : (List<ParseObject>) objects.get(0)) {
                mappings.put(bookMapping.getParseObject(COLUMN.WISH_LIST_BOOK.WISH_LIST).getObjectId(), bookMapping);
            }
            chest.put((Integer) objects.get(1), mappings);
            notifyDataSetChanged();
        }

        @Override
        protected List<Object> doInBackground(Object... params) {
            ParseQuery query = new ParseQuery(Constant.ParseObject.WISH_LIST_BOOK);
            ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.WISH_LIST);
            innerQuery.whereContainedIn(COLUMN.WISH_LIST.NAME, new PumpkinDB(context).getWishListColumn("name"));
            query.whereMatchesQuery(COLUMN.WISH_LIST_BOOK.WISH_LIST, innerQuery);
            query.whereEqualTo(COLUMN.WISH_LIST_BOOK.BOOK, params[0]);
            try {
                return Arrays.asList(query.find(), params[1]);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}
