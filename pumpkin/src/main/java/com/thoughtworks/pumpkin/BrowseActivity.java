package com.thoughtworks.pumpkin;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.adapter.BooksCursor;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;

import java.util.List;

public class BrowseActivity extends AbstractParseActivity {
    BrowseActivity browseActivity = this;
    @InjectView(R.id.books)
    ListView books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);
        ParseQuery query = new ParseQuery("Book");
        query.setLimit(20);
        query.orderByAscending("rating");
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        new LoadingActivity(progress).execute();
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "bookImage", "Title"});
                for (int i = 0; i < parseObjects.size(); i++) {
                    cursor.addRow(new Object[]{i, parseObjects.get(i).getString("thumbnail"), parseObjects.get(i).getString("title")});
                }
                books.setAdapter(new BooksCursor(browseActivity, R.layout.book, cursor,
                        new String[]{"bookImage", "title"}, new int[]{R.id.bookImage, R.id.title}));
            }
        });
    }
}
