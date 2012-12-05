package com.thoughtworks.pumpkin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.thoughtworks.pumpkin.BooksCategoryActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.WishListActivity;
import roboguice.fragment.RoboListFragment;

public class SideNavigation extends RoboListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.side_nav, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                new String[]{"My WishLists", "Books", "Shops", "Settings"}));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 1:
                startActivity(new Intent(getActivity(), BooksCategoryActivity.class));
                return;
            case 0:
                startActivity(new Intent(getActivity(), WishListActivity.class));
        }
    }
}
