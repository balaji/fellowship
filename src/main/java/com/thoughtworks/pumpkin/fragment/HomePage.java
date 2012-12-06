package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.thoughtworks.pumpkin.ViewBooksActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Keys;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import roboguice.fragment.RoboListFragment;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.COLUMN;
import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;

public class HomePage extends RoboListFragment {

    @Inject
    SharedPreferences preferences;
    ArrayAdapter<String> adapter;
    PumpkinDB pumpkinDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Parse.initialize(getActivity(), Keys.PARSE_API_KEY, Keys.PARSE_CLIENT_KEY);
        pumpkinDB = new PumpkinDB(getActivity());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.wishlists, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ViewBooksActivity.class);
        intent.putExtra("wishlist", ((TextView) v).getText().toString());
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                pumpkinDB.getWishListColumn("name"));
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wishlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Name:");
                final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.create_wishlist_dialog, null);
                alertDialog.setView(layout);
                alertDialog.setPositiveButton(Constant.Message.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ParseObject wishList = new ParseObject(WISH_LIST);
                        final String wishListName = ((TextView) layout.findViewById(R.id.newListName)).getText().toString();
                        wishList.put(COLUMN.WISH_LIST.NAME, wishListName);
                        wishList.put(COLUMN.WISH_LIST.USER, preferences.getString(Constant.Preferences.USER_ID, null));
                        wishList.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                pumpkinDB.insertWishList(wishListName, wishList.getObjectId());
                            }
                        });
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
