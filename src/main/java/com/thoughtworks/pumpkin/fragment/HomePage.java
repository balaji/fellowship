package com.thoughtworks.pumpkin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.ViewBooksActivity;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import roboguice.fragment.RoboListFragment;

public class HomePage extends RoboListFragment {

    private PumpkinDB pumpkinDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pumpkinDB = new PumpkinDB(getActivity());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                pumpkinDB.getWishListColumn("name"));
        setListAdapter(adapter);
    }
}
