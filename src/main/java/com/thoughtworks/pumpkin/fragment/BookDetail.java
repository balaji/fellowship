package com.thoughtworks.pumpkin.fragment;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.*;
import com.thoughtworks.pumpkin.R;
import org.json.JSONException;
import org.json.JSONObject;
import roboguice.fragment.RoboFragment;
import com.fedorvlasov.lazylist.ImageLoader;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BookDetail extends RoboFragment {
    ArrayList<String> book;
    private ImageLoader imageLoader=new ImageLoader(getActivity());
    private Button shareButton;

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
        shareButton = (Button) view.findViewById(R.id.shareButton);
        title.setText(book.get(0));
        imageLoader.DisplayImage(book.get(1),image);
        rank.setText("#"+book.get(2));
        author.setText(book.get(3));
        publisher.setText(book.get(4));
        pgno.setText("Pages - "+book.get(5));
        description.setText(book.get(6));
        description.setMovementMethod(new ScrollingMovementMethod());
        shareButton = (Button) view.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishStory();
            }
        });
    }

    private void publishStory() {
        Session session = Session.getActiveSession();

        if (session != null){
            Bundle postParams = new Bundle();
            postParams.putString("name", book.get(0));
            postParams.putString("caption", book.get(3));
            postParams.putString("description", book.get(6));
            postParams.putString("picture", book.get(1));

            Request.Callback callback= new Request.Callback() {
                public void onCompleted(Response response) {
                    JSONObject graphResponse = response
                            .getGraphObject()
                            .getInnerJSONObject();
                    String postId = null;
                    try {
                        postId = graphResponse.getString("id");
                    } catch (JSONException e) {

                    }
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        Toast.makeText(getActivity()
                                .getApplicationContext(),
                                error.getErrorMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity()
                                .getApplicationContext(),
                                postId,
                                Toast.LENGTH_LONG).show();
                    }
                }
            };

            Request request = new Request(session, "me/feed", postParams,
                    HttpMethod.POST, callback);

            RequestAsyncTask task = new RequestAsyncTask(request);
            task.execute();
        }

    }
}
