package com.thoughtworks.pumpkin.fragment;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.thoughtworks.pumpkin.R;
import roboguice.fragment.RoboFragment;
import com.fedorvlasov.lazylist.ImageLoader;


import java.util.ArrayList;

public class BookDetail extends RoboFragment {
    ArrayList<String> book;
    private ImageLoader imageLoader=new ImageLoader(getActivity());

    public BookDetail(ArrayList<String> book)
    {
        this.book=book;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_detail, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        TextView title  =(TextView)view.findViewById(R.id.title);
        TextView rank  =(TextView)view.findViewById(R.id.rank);
        TextView author  =(TextView)view.findViewById(R.id.author);
        TextView publisher  =(TextView)view.findViewById(R.id.publisher);
        TextView pgno  =(TextView)view.findViewById(R.id.pgno);
        TextView description =(TextView)view.findViewById(R.id.description);
        ImageView image=(ImageView)view.findViewById(R.id.bookImage);
        title.setText(book.get(0));
        imageLoader.DisplayImage(book.get(1),image);
        rank.setText("#"+book.get(2));
        author.setText(book.get(3));
        publisher.setText(book.get(4));
        pgno.setText("Pages - "+book.get(5));
        description.setText(book.get(6));
        description.setMovementMethod(new ScrollingMovementMethod());



    }
}
