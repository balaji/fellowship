package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.PumpkinOnClickListener;
import com.thoughtworks.pumpkin.helper.Util;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;

import java.util.List;


public class WishLists extends RoboListFragment {

    @InjectView(R.id.newList)
    Button newList;

    @Inject
    SharedPreferences preferences;
    ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Parse.initialize(getActivity(), Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        return inflater.inflate(R.layout.wishlists, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newList.setOnClickListener(new PumpkinOnClickListener(getActivity()) {
            @Override
            public void done(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setTitle("Name:");
                final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.create_wishlist_dialog, null);
                ad.setView(layout);
                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseObject wishLists = new ParseObject("WishLists");
                        wishLists.put("name", ((TextView) layout.findViewById(R.id.newListName)).getText().toString());
                        wishLists.put("owner", preferences.getString(Constant.Preferences.USER_ID, null));
                        wishLists.saveInBackground();
                        dialogInterface.dismiss();
                        loadData();
                    }
                });

                ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                ad.show();
            }
        });
        loadData();
    }

    private void loadData() {
        ParseQuery wishListQuery = new ParseQuery("WishLists");
        wishListQuery.orderByAscending("name");
        wishListQuery.whereEqualTo("owner", preferences.getString(Constant.Preferences.USER_ID, null));
        final ProgressDialog dialog = Util.showProgressDialog(getActivity());
        wishListQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                MatrixCursor cursor = new MatrixCursor(new String[]{"_id", "name"});
                for (int i = 0; i < parseObjects.size(); i++) {
                    cursor.addRow(new Object[]{i, parseObjects.get(i).getString("name")});
                }
                adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor,
                        new String[]{"name"}, new int[]{android.R.id.text1});
                setListAdapter(adapter);
            }
        });
    }
}
