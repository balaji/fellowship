package com.thoughtworks.pumpkin.listener;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import com.thoughtworks.pumpkin.BookDetailActivity;
import com.thoughtworks.pumpkin.ViewBooksActivity;
import com.thoughtworks.pumpkin.adapter.BooksAdapter;
import com.thoughtworks.pumpkin.domain.Book;
import android.view.View;

import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: rithi
 * Date: 25/1/13
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookDescriptionViewListener extends Activity implements View.OnClickListener{

    private Book book;

    private BooksAdapter booksAdapter;

    public BookDescriptionViewListener(BooksAdapter booksAdapter,Book book) {

        this.book = book;
        this.booksAdapter=booksAdapter;

    }

    @Override
    public void onClick(final View view) {

        ArrayList<String> bookdetails=new ArrayList<String>();
        bookdetails.add(book.getTitle());
        bookdetails.add(book.getThumbnail());
        String rate=Integer.toString(book.getRating());
        bookdetails.add(rate);
        bookdetails.add(book.getAuthors());
        bookdetails.add(book.getPublishers());
        String pgcount=Integer.toString(book.getPageCount());
        bookdetails.add(pgcount);
        bookdetails.add(book.getDescription());
        Intent intent = new Intent(booksAdapter.getContext(), BookDetailActivity.class);
        intent.putStringArrayListExtra("book", bookdetails);
        booksAdapter.getContext().startActivity(intent);



    }



}