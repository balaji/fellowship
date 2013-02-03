package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.thoughtworks.pumpkin.*;
import com.thoughtworks.pumpkin.adapter.SidePanelAdapter;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SidePanel extends RoboFragment {
    public static boolean logout=false;
    private static final String NAME = "NAME";
    List<List<Map<String, String>>> childData;

    private SimpleExpandableListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.side_panel, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final PumpkinDB pumpkinDB = new PumpkinDB(getActivity());
        ExpandableListView listView = (ExpandableListView) view.findViewById(android.R.id.list);
        adapter = prepareAdapter(pumpkinDB);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String key = null;
                Intent intent = null;

                switch (i) {
                    case 0:
                        key = "wishlist";
                        if (i1 == 0) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setTitle("Name:");
                            final View layout = LayoutInflater.from(getActivity()).inflate(R.layout.create_wishlist_dialog, null);
                            alertDialog.setView(layout);
                            alertDialog.setPositiveButton(Constant.Message.OK, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    final String wishListName = ((TextView) layout.findViewById(R.id.newListName)).getText().toString();
                                    pumpkinDB.insertWishList(wishListName);
                                    childData.set(0, wishListChildData(pumpkinDB));
                                    adapter.notifyDataSetChanged();
                                    dialogInterface.dismiss();
                                }
                            });
                            alertDialog.setNegativeButton(Constant.Message.CANCEL, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    dialogInterface.dismiss();
                                }
                            });
                            alertDialog.show();
                            return true;
                        }
                        intent = new Intent(getActivity(), ViewBooksActivity.class);
                        break;

                    case 1:
                        key = "category";
                        intent = new Intent(getActivity(), ViewBooksActivity.class);
                        break;
                    case 2:
                        key = "shop";
                        intent = new Intent(getActivity(), ShopActivity.class);
                        intent.putExtra("books", new HashMap<String, String>());
                        break;
                    case 3:
                        key="Settings";
                        if(i1==0)
                            intent = new Intent(getActivity(), ZipCodeActivity.class);
                        if(i1==1)
                        {
                            logout = true;
                            intent = new Intent(getActivity(), FacebookLoginActivity.class);
                        }
                        intent.putExtra("Settings", new HashMap<String, String>());

                        break;

                }

                intent.putExtra(key, ((TextView) view.findViewById(R.id.text1)).getText());
                startActivity(intent);
                return false;
            }
        });
    }

    private SimpleExpandableListAdapter prepareAdapter(PumpkinDB pumpkinDB) {
        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<Map<String, String>>>();
        groupData.add(new HashMap<String, String>() {{
            put(NAME, "My Wishlists");
        }});
        groupData.add(new HashMap<String, String>() {{
            put(NAME, "Books");
        }});
        groupData.add(new HashMap<String, String>() {{
            put(NAME, "Shops");
        }});
        groupData.add(new HashMap<String, String>() {{
            put(NAME, "Settings");
        }});
        childData.add(wishListChildData(pumpkinDB));
        childData.add(getChildData(pumpkinDB.getBookCategories()));
        childData.add(getChildData(pumpkinDB.getShops()));
        childData.add(settingsChildData());
        childData.add(new ArrayList<Map<String, String>>());
        return new SidePanelAdapter(this, getActivity(), groupData, R.layout.side_panel_group,
                new String[]{NAME}, new int[]{R.id.text1}, childData, R.layout.side_panel_child,
                new String[]{NAME}, new int[]{R.id.text1});
    }

    private ArrayList<Map<String, String>> wishListChildData(PumpkinDB pumpkinDB) {
        ArrayList<Map<String, String>> wishListNames = getChildData(pumpkinDB.getWishlistNames());
        wishListNames.add(0, new HashMap<String, String>() {{
            put(NAME, "Create New...");
        }});
        return wishListNames;
    }

    private ArrayList<Map<String, String>> settingsChildData() {
       ArrayList<Map<String, String>> SettingsNames = new ArrayList<Map<String, String>>();
        SettingsNames.add(0, new HashMap<String, String>() {{
            put(NAME, "Reset Zip Code");
        }});
        SettingsNames.add(1, new HashMap<String, String>() {{
            put(NAME, "Logout");
        }});

        return SettingsNames;

    }

    private ArrayList<Map<String, String>> getChildData(List<String> data) {
        ArrayList<Map<String, String>> childData = new ArrayList<Map<String, String>>();
        for (String name : data) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(NAME, name);
            childData.add(map);
        }
        return childData;
    }
}