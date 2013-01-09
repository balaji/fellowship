package com.thoughtworks.pumpkin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.fragment.SidePanel;

import java.util.List;
import java.util.Map;

public class SidePanelAdapter extends SimpleExpandableListAdapter {

    private SidePanel sidePanel;

    public SidePanelAdapter(SidePanel sidePanel, Context context, List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo, List<? extends List<? extends Map<String, ?>>> childData, int childLayout, String[] childFrom, int[] childTo) {
        super(context, groupData, groupLayout, groupFrom, groupTo, childData, childLayout, childFrom, childTo);
        this.sidePanel = sidePanel;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LinearLayout childView = (LinearLayout) super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
        ((TextView) (childView.findViewById(R.id.text1))).setTextAppearance(sidePanel.getActivity(), android.R.style.TextAppearance_Medium);
        return childView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LinearLayout groupView = (LinearLayout) super.getGroupView(groupPosition, isExpanded, convertView, parent);
        ((TextView) (groupView.findViewById(R.id.text1))).setTextAppearance(sidePanel.getActivity(), android.R.style.TextAppearance_Large);
        return groupView;
    }
}
