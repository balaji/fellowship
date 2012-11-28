package com.thoughtworks.pumpkin.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.BrowseActivity;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.fragment.RoboListFragment;

import java.util.List;

public class BooksCategory extends RoboListFragment {

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), BrowseActivity.class);
        intent.putExtra("category", ((TextView) v).getText());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Parse.initialize(getActivity(), Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        ParseQuery query = new ParseQuery(Constant.ParseObject.CATEGORY);
        query.orderByAscending("name");
        final ProgressDialog dialog = Util.showProgressDialog(getActivity());
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "name"});
                for (int i = 0; i < parseObjects.size(); i++) {
                    cursor.addRow(new Object[]{i, parseObjects.get(i).getString("name")});
                }
                ListAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor,
                        new String[]{"name"}, new int[]{android.R.id.text1});
                setListAdapter(adapter);
            }
        });
    }
}
