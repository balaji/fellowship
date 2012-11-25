package com.thoughtworks.pumpkin.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.inject.Inject;
import com.thoughtworks.pumpkin.BrowseActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinOnClickListener;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class HomeFragment extends RoboFragment {

    @InjectView(R.id.welcome)
    TextView welcome;

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
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcome.setText(Constant.Message.GREETING + " " + preferences.getString(Constant.Preferences.USERNAME, null));
        browseButton.setOnClickListener(new PumpkinOnClickListener((Activity) getActivity()) {
            @Override
            public void done(View view) {
                startActivity(new Intent(getActivity(), BrowseActivity.class));
            }
        });
    }
}
