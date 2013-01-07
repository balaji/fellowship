package com.thoughtworks.pumpkin.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.slidingmenu.lib.SlidingMenu;
import com.thoughtworks.pumpkin.BaseActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.ShelfActivity;
import com.thoughtworks.pumpkin.helper.Constant;
import com.thoughtworks.pumpkin.helper.Util;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.MBTilesFileArchive;
import org.osmdroid.tileprovider.modules.MapTileFileArchiveProvider;
import org.osmdroid.tileprovider.modules.MapTileModuleProviderBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import roboguice.fragment.RoboFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopDisplay extends RoboFragment {

    MapView mapView;
    ArrayList<OverlayItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ProgressDialog dialog = Util.showProgressDialog(getActivity());
        ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.mapLayout);
        XYTileSource mbtilesSource = new XYTileSource("mbtiles", ResourceProxy.string.offline_mode, 19, 21, 256, ".png", "http://pumpkin.thoughtworks.com/");
        DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(getActivity());
        File f = new File(Environment.getExternalStorageDirectory(), "ascendas.mbtiles");
        IArchiveFile[] files = {MBTilesFileArchive.getDatabaseFileArchive(f)};
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, mbtilesSource, files);
        MapTileProviderArray tileProviderArray = new MapTileProviderArray(mbtilesSource, null, new MapTileModuleProviderBase[]{moduleProvider});

        mapView = new MapView(getActivity(), 256, resourceProxy, tileProviderArray);
        mapView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.addView(mapView);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(false);
        mapView.getController().setZoom(19);
        mapView.getController().setCenter(new GeoPoint(12.9839, 80.2462));

        try {
            addOverlays(resourceProxy);
        } catch (ParseException e) {
        }
        if (dialog.isShowing()) dialog.dismiss();
        mapView.invalidate();
    }

    private void addOverlays(final DefaultResourceProxyImpl resourceProxy) throws ParseException {
        HashMap<String, String> books = (HashMap<String, String>) getActivity().getIntent().getExtras().get("books");
        items = new ArrayList<OverlayItem>();

        HashMap<String, String> localCache = new HashMap<String, String>();
        ArrayList<String> usedCoordinates = new ArrayList<String>();
        String coordinates;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        for (Map.Entry<String, String> entry : books.entrySet()) {
            if (entry.getValue() == null) {
                ParseObject parseObject = new ParseQuery(Constant.ParseObject.BOOK).get(entry.getKey());
                ParseQuery query = new ParseQuery(Constant.ParseObject.SHOP_CATEGORY);
                query.whereEqualTo("category_name", parseObject.getParseObject("parent"));
                List<ParseObject> coordinateList = query.find();
                String coordinate = coordinateList.get(0).getString("coordinates");
                if (!usedCoordinates.contains(coordinate)) {
                    usedCoordinates.add(coordinate);
                    items.add(createOverlayItem(coordinate));
                    refreshOverlays(resourceProxy);
                }
                continue;
            }
            if (localCache.get(entry.getValue()) == null) {
                ParseQuery innerQuery = new ParseQuery(Constant.ParseObject.CATEGORY);
                ParseQuery query = new ParseQuery(Constant.ParseObject.SHOP_CATEGORY);
                query.whereEqualTo("category_name", innerQuery.get(entry.getValue()));
                List<ParseObject> coordinateList = query.find();
                coordinates = coordinateList.get(0).getString("coordinates");
                localCache.put(entry.getValue(), coordinates);
            } else {
                coordinates = localCache.get(entry.getValue());
            }
            items.add(createOverlayItem(coordinates));
        }
        refreshOverlays(resourceProxy);
    }

    private void refreshOverlays(DefaultResourceProxyImpl resourceProxy) {
        ItemizedOverlay<OverlayItem> locationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        startActivity(new Intent(getActivity(), ShelfActivity.class));
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                        return false;
                    }
                }, resourceProxy);

        mapView.getOverlays().clear();
        mapView.getOverlays().add(locationOverlay);
    }

    private OverlayItem createOverlayItem(String coordinates) {
        String[] coordinatePairs = coordinates.split(",");
        return new OverlayItem("Here", "SampleDescription",
                new GeoPoint(Double.parseDouble(coordinatePairs[0]), Double.parseDouble(coordinatePairs[1])));
    }
}
