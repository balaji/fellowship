package com.thoughtworks.pumpkin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.thoughtworks.pumpkin.R;
import roboguice.fragment.RoboFragment;

public class ShopDisplay extends RoboFragment {

    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        String category = getActivity().getIntent().getStringExtra("category");
//        String wishlist = getActivity().getIntent().getStringExtra("wishlist");
//        String shop = getActivity().getIntent().getStringExtra("shop");
        tv = (TextView) view.findViewById(R.id.gpsDisplay);
    }
}
