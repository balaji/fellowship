package com.thoughtworks.pumpkin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.thoughtworks.pumpkin.R;
import roboguice.fragment.RoboFragment;

public class BookDetail extends RoboFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_detail, container, false);
    }
}
