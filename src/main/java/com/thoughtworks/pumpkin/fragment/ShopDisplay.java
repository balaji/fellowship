package com.thoughtworks.pumpkin.fragment;

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
import roboguice.fragment.RoboFragment;

import java.io.File;

public class ShopDisplay extends RoboFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((BaseActivity)getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.mapLayout);
        XYTileSource mbtilesSource = new XYTileSource("mbtiles", ResourceProxy.string.offline_mode, 19, 21, 256, ".png", "http://pumpkin.thoughtworks.com/");
        DefaultResourceProxyImpl resourceProxy = new DefaultResourceProxyImpl(getActivity().getApplicationContext());
        SimpleRegisterReceiver simpleReceiver = new SimpleRegisterReceiver(getActivity());
        File f = new File(Environment.getExternalStorageDirectory(), "ascendas.mbtiles");
        IArchiveFile[] files = {MBTilesFileArchive.getDatabaseFileArchive(f)};
        MapTileModuleProviderBase moduleProvider = new MapTileFileArchiveProvider(simpleReceiver, mbtilesSource, files);
        MapTileProviderArray tileProviderArray = new MapTileProviderArray(mbtilesSource, null, new MapTileModuleProviderBase[]{moduleProvider});

        MapView mapView = new MapView(getActivity(), 256, resourceProxy, tileProviderArray);
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
    }
}
