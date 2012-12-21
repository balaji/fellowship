package com.thoughtworks.pumpkin.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.thoughtworks.pumpkin.fragment.SidePanel;
import android.widget.SearchView;

import android.content.Context;
import java.util.List;
import java.util.Map;
import android.app.SearchManager;

public class SidePanelAdapter extends SimpleExpandableListAdapter {

    private SidePanel sidePanel;

    public SidePanelAdapter(SidePanel sidePanel, Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        this.sidePanel = sidePanel;
                }

    @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView childView = (TextView) super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        childView.setTextAppearance(sidePanel.getActivity(), android.R.style.TextAppearance_Medium);
        return childView;
    }
}
