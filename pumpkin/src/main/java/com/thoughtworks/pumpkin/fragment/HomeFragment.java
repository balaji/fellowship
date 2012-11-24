package com.thoughtworks.pumpkin.fragment;

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
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class HomeFragment extends RoboFragment {

    @InjectView(R.id.welcome)
    TextView welcome;

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.browseButton)
    Button browseButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welcome.setText(preferences.getString(Constant.Preferences.USERNAME, null));
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BrowseActivity.class));
            }
        });
    }
}
