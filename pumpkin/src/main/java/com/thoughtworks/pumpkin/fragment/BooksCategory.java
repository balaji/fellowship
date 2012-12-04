package com.thoughtworks.pumpkin.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.thoughtworks.pumpkin.BrowseActivity;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import com.thoughtworks.pumpkin.helper.Constant;
import roboguice.fragment.RoboListFragment;

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
        PumpkinDB categories = new PumpkinDB(getActivity());
        SQLiteDatabase database = categories.getReadableDatabase();
        Cursor cursor = database.rawQuery("select rowid _id," + Constant.ParseObject.COLUMN.CATEGORY.NAME + " from " + PumpkinDB.CATEGORY_TABLE_NAME, null);
        ListAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor,
                new String[]{"name"}, new int[]{android.R.id.text1});
        setListAdapter(adapter);
    }
}
