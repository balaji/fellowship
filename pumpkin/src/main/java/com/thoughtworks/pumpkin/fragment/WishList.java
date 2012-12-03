package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.pumpkin.BrowseActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.Util;
import com.thoughtworks.pumpkin.listener.PumpkinOnClickListener;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;


public class WishList extends RoboListFragment {

    @InjectView(R.id.newList)
    Button newList;

    @Inject
    SharedPreferences preferences;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Parse.initialize(getActivity(), Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        return inflater.inflate(R.layout.wishlists, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), BrowseActivity.class);
        intent.putExtra("wishlist", ((TextView) v).getText().toString());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newList.setOnClickListener(new PumpkinOnClickListener(getActivity()) {
            @Override
            public void done(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Name:");
                final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.create_wishlist_dialog, null);
                alertDialog.setView(layout);
                alertDialog.setPositiveButton(Constant.Message.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ParseObject wishLists = new ParseObject(WISH_LIST);
                        String wishListName = ((TextView) layout.findViewById(R.id.newListName)).getText().toString();
                        wishLists.put(COLUMN.WISH_LIST.NAME, wishListName);
                        wishLists.put(COLUMN.WISH_LIST.USER, preferences.getString(Constant.Preferences.USER_ID, null));
                        wishLists.saveInBackground();
                        adapter.add(wishListName);
                        adapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.setNegativeButton(Constant.Message.CANCEL, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        loadData();
    }

    private void loadData() {
        ParseQuery wishListQuery = new ParseQuery(WISH_LIST);
        wishListQuery.orderByAscending(COLUMN.WISH_LIST.NAME);
        wishListQuery.whereEqualTo(COLUMN.WISH_LIST.USER, preferences.getString(Constant.Preferences.USER_ID, null));
        final ProgressDialog dialog = Util.showProgressDialog(getActivity());
        wishListQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List<ParseObject> wishLists, ParseException e) {
                if (dialog.isShowing()) dialog.dismiss();
                ArrayList<String> strings = new ArrayList<String>();
                for (ParseObject wishList : wishLists) {
                    strings.add(wishList.getString(COLUMN.WISH_LIST.NAME));
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings);
                setListAdapter(adapter);
            }
        });
    }
}
