package com.thoughtworks.pumpkin.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import roboguice.fragment.RoboListFragment;


public class WishLists extends RoboListFragment {

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
