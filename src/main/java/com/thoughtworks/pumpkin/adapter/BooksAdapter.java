package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
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
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.domain.Book;
import com.thoughtworks.pumpkin.helper.BookViewHolder;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.listener.ImageButtonOnClickListener;
import com.thoughtworks.pumpkin.listener.BookDescriptionViewListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BooksAdapter extends SimpleAdapter {
    private ImageLoader imageLoader;
    private Context context;
    private PumpkinDB pumpkinDB;

    public BooksAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        imageLoader = new ImageLoader(context);
        pumpkinDB = new PumpkinDB(context);
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
            holder.wishListBooks = new ArrayList<String>();
            convertView.setTag(holder);
        } else {
            holder = (BookViewHolder) convertView.getTag();
        }

        Map<String, Object> item = (Map<String, Object>) getItem(position);
        Book book = (Book) item.get("book");
        fillView(holder, book);
        return convertView;
    }

    private void fillView(BookViewHolder holder, Book book) {
        imageLoader.DisplayImage(book.getThumbnail(), holder.image);
        holder.rating.setText("#" + book.getRating());
        holder.title.setText(book.getTitle());
        holder.authors.setText("- " + book.getAuthors());
        holder.description.setText(Html.fromHtml(book.getSnippet()));
        drawHeartIcon(holder, book.getId());
        holder.wishListButton.setOnClickListener(new ImageButtonOnClickListener(this, holder, book));
        holder.description.setOnClickListener(new BookDescriptionViewListener(this,book));
        holder.image.setOnClickListener(new BookDescriptionViewListener(this,book));

    }

    private void drawHeartIcon(BookViewHolder holder, int bookId) {
        heartIcon(true, holder);
        ArrayList<String> wishlistEntries = pumpkinDB.getWistlistEntriesForBook(bookId);
        holder.wishListBooks.clear();
        holder.wishListBooks.addAll(wishlistEntries);
        holder.wishListButton.setBackgroundDrawable(context.getResources()
                .getDrawable(!wishlistEntries.isEmpty() ? R.drawable.ic_heart_filled : R.drawable.ic_action_heart));
    }

    private void heartIcon(boolean toggle, BookViewHolder holder) {
        holder.spinner.setVisibility(!toggle ? View.VISIBLE : View.GONE);
        holder.wishListButton.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    public Context getContext() {
        return context;
    }
}
