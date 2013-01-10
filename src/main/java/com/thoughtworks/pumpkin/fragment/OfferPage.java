package com.thoughtworks.pumpkin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.thoughtworks.pumpkin.R;
import roboguice.fragment.RoboFragment;

public class OfferPage extends RoboFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.offer_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String index = getActivity().getIntent().getExtras().getString("index");
        if(!index.contains("Picture")) {
            ((ImageView)view.findViewById(R.id.offerImage)).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offer1));
        } else {
            ((ImageView)view.findViewById(R.id.offerImage)).setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offer2));
        }
    }
}
