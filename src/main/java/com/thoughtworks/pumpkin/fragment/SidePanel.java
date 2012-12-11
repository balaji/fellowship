package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.ShopActivity;
import com.thoughtworks.pumpkin.ViewBooksActivity;
import com.thoughtworks.pumpkin.adapter.SidePanelAdapter;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.pumpkin.helper.Constant.ParseObject.WISH_LIST;

public class SidePanel extends RoboFragment {
    private static final String NAME = "NAME";
    List<List<Map<String, String>>> childData;

    @Inject
    SharedPreferences preferences;
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
                                    final ParseObject wishList = new ParseObject(WISH_LIST);
                                    final String wishListName = ((TextView) layout.findViewById(R.id.newListName)).getText().toString();
                                    wishList.put(Constant.ParseObject.COLUMN.WISH_LIST.NAME, wishListName);
                                    wishList.put(Constant.ParseObject.COLUMN.WISH_LIST.USER, preferences.getString(Constant.Preferences.USER_ID, null));
                                    wishList.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            pumpkinDB.insertWishList(wishListName, wishList.getObjectId());
                                            childData.set(0, wishListChildData(pumpkinDB));
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
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
                        break;
                }

                intent.putExtra(key, ((TextView) view).getText());
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
        childData.add(new ArrayList<Map<String, String>>());
        return new SidePanelAdapter(this, getActivity(), groupData, android.R.layout.simple_expandable_list_item_1,
                new String[]{NAME}, new int[]{android.R.id.text1}, childData, android.R.layout.simple_expandable_list_item_1,
                new String[]{NAME}, new int[]{android.R.id.text1});
    }

    private ArrayList<Map<String, String>> wishListChildData(PumpkinDB pumpkinDB) {
        ArrayList<Map<String, String>> wishListNames = getChildData(pumpkinDB.getWishListColumn("name"));
        wishListNames.add(0, new HashMap<String, String>() {{
            put(NAME, "Create New...");
        }});
        return wishListNames;
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
