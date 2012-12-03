package com.thoughtworks.pumpkin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.inject.Inject;
import com.thoughtworks.pumpkin.BooksCategoryActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.WishListActivity;
import com.thoughtworks.pumpkin.listener.PumpkinOnClickListener;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class SideNavigation extends RoboFragment {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.browseButton)
    Button browseButton;

    @InjectView(R.id.settingsButton)
    Button settings;

    @InjectView(R.id.listsButton)
    Button lists;

    @InjectView(R.id.shopsButton)
    Button shops;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.side_nav, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        browseButton.setOnClickListener(new PumpkinOnClickListener((Activity) getActivity()) {
            @Override
            public void done(View view) {
                startActivity(new Intent(getActivity(), BooksCategoryActivity.class));
            }
        });
        lists.setOnClickListener(new PumpkinOnClickListener((Activity) getActivity()) {
            @Override
            public void done(View view) {
                startActivity(new Intent(getActivity(), WishListActivity.class));
            }
        });
    }
}
