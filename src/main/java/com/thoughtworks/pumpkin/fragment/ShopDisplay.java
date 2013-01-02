package com.thoughtworks.pumpkin.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.slidingmenu.lib.SlidingMenu;
import com.thoughtworks.pumpkin.BaseActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.ShelfActivity;
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

public class ShopDisplay extends RoboFragment implements LocationListener {

    MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
        mapView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        layout.addView(mapView);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setClickable(true);
        mapView.setUseDataConnection(false);
        mapView.getController().setZoom(19);
        mapView.getController().setCenter(new GeoPoint(12.9840, 80.246));

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);
        Drawable marker = getActivity().getResources().getDrawable(R.drawable.marker);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
//        OverlayItem item1 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2461532));
//        OverlayItem item2 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2461858));
//        OverlayItem item3 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2462229));
//        OverlayItem item4 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2462504));
//        OverlayItem item5 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2462875));
//        OverlayItem item6 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2463187));
//        OverlayItem item7 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9837211, 80.2463521));
//        OverlayItem item8 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9838773, 80.2461658));
//        OverlayItem item9 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9838773, 80.2462311));
//        OverlayItem item10 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9838773, 80.2462957));
//        OverlayItem item11 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9838773, 80.246361));
//        OverlayItem item12 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9841189, 80.2461503));
//        OverlayItem item13 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9841189, 80.2462828));
//        OverlayItem item14 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.983806, 80.2466289));
//        OverlayItem item15 = new OverlayItem("Here", "SampleDescription", new GeoPoint(12.9842354, 80.2463476));
//
//        item1.setMarker(marker);
//        item2.setMarker(marker);
//        item3.setMarker(marker);
//        item4.setMarker(marker);
//        item5.setMarker(marker);
//        item6.setMarker(marker);
//        item7.setMarker(marker);
//        item8.setMarker(marker);
//        item9.setMarker(marker);
//        item10.setMarker(marker);
//        item11.setMarker(marker);
//        item12.setMarker(marker);
//        item13.setMarker(marker);
//        item14.setMarker(marker);
//        item15.setMarker(marker);
//
//        items.add(item1);
//        items.add(item2);
//        items.add(item3);
//        items.add(item4);
//        items.add(item5);
//        items.add(item6);
//        items.add(item7);
//        items.add(item8);
//        items.add(item9);
//        items.add(item10);
//        items.add(item11);
//        items.add(item12);
//        items.add(item13);
//        items.add(item14);
//        items.add(item15);

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
        mapView.getOverlays().add(locationOverlay);
        mapView.invalidate();

    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude() * 1E6);
        int lng = (int) (location.getLongitude() * 1E6);
        GeoPoint gpt = new GeoPoint(lat, lng);
        mapView.getController().setCenter(gpt);
        mapView.invalidate();
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
