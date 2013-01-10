package com.thoughtworks.pumpkin.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.slidingmenu.lib.SlidingMenu;
import com.thoughtworks.pumpkin.BaseActivity;
import com.thoughtworks.pumpkin.BookDetailActivity;
import com.thoughtworks.pumpkin.OfferActivity;
import com.thoughtworks.pumpkin.R;
import com.thoughtworks.pumpkin.helper.PumpkinDB;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopDisplay extends RoboFragment {

    private MapView mapView;
    private ArrayList<OverlayItem> items;
    private ProgressDialog dialog;
    private PumpkinDB pumpkinDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pumpkinDB = new PumpkinDB(getActivity());
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        dialog = Util.showProgressDialog(getActivity());
        ((BaseActivity) getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
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
        ((LinearLayout) view.findViewById(R.id.mapLayout)).addView(mapView);

        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setUseDataConnection(false);
        mapView.getController().setZoom(19);
        mapView.getController().setCenter(new GeoPoint(12.9839, 80.2462));
        mapView.getOverlays().clear();
        addOverlays(resourceProxy);
        if (dialog.isShowing()) dialog.dismiss();
        mapView.invalidate();
    }

    private void addOverlays(final DefaultResourceProxyImpl resourceProxy) {
        HashMap<String, String> books = (HashMap<String, String>) getActivity().getIntent().getExtras().get("books");
        items = new ArrayList<OverlayItem>();

        HashMap<String, String> localCache = new HashMap<String, String>();
        ArrayList<String> usedCoordinates = new ArrayList<String>();
        HashMap<String, List<String>> labels = new HashMap<String, List<String>>();
        String coordinates;
        for (Map.Entry<String, String> entry : books.entrySet()) {
            if (localCache.get(entry.getValue()) == null) {
                coordinates = pumpkinDB.getCoordinatesForCategory(entry.getValue());
                localCache.put(entry.getValue(), coordinates);
            } else {
                coordinates = localCache.get(entry.getValue());
            }

            if (!usedCoordinates.contains(coordinates)) {
                usedCoordinates.add(coordinates);
                items.add(createOverlayItem(coordinates, entry.getValue()));
            }
            if (labels.get(entry.getValue()) == null) {
                labels.put(entry.getValue(), new ArrayList<String>());
            }
            labels.get(entry.getValue()).add(entry.getKey());
        }
        addOfferOverlay(resourceProxy);
        refreshOverlays(labels, resourceProxy);
    }

    private void addOfferOverlay(ResourceProxy resourceProxy) {
        OverlayItem item1 = createOverlayItem("12.9838086,80.2466642", "Upto 50% discount on Picture books");
        OverlayItem item2 = createOverlayItem("12.9840541,80.246334", "5% off on Religious books");
        item1.setMarker(getActivity().getResources().getDrawable(R.drawable.marker));
        item2.setMarker(getActivity().getResources().getDrawable(R.drawable.marker));
        mapView.getOverlays().add(new ItemizedIconOverlay<OverlayItem>(Arrays.asList(item1, item2), new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem overlayItem) {
                new AlertDialog.Builder(getActivity())
                        .setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Arrays.asList(overlayItem.getSnippet())), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), OfferActivity.class);
                                intent.putExtra("index", overlayItem.getSnippet());
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("OK", null)
                        .setTitle("Recommendations for You!").show();
                return true;
            }

            @Override
            public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                return false;
            }
        }, resourceProxy));
    }

    private void refreshOverlays(Map<String, List<String>> labels, DefaultResourceProxyImpl resourceProxy) {
        ItemizedOverlay<OverlayItem> locationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemGestureListener<OverlayItem>(labels) {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        OverlayItem tappedItem = items.get(index);
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setTitle(tappedItem.getSnippet());
                        dialog.setContentView(R.layout.book_popup);
                        ((ListView) dialog.findViewById(R.id.storeBooks))
                                .setAdapter(new CustomAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, labels.get(tappedItem.getSnippet())));
                        dialog.show();
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                        startActivity(new Intent(getActivity(), BookDetailActivity.class));
                        return true;
                    }
                }, resourceProxy);

        mapView.getOverlays().add(locationOverlay);
    }

    private OverlayItem createOverlayItem(String coordinates, String title) {
        String[] coordinatePairs = coordinates.split(",");
        return new OverlayItem("Here", title,
                new GeoPoint(Double.parseDouble(coordinatePairs[0]), Double.parseDouble(coordinatePairs[1])));
    }

    abstract class ItemGestureListener<T> implements ItemizedIconOverlay.OnItemGestureListener<T> {
        protected Map<String, List<String>> labels;

        protected ItemGestureListener(Map<String, List<String>> labels) {
            super();
            this.labels = labels;
        }
    }

    class CustomAdapter<String> extends ArrayAdapter<String> {

        private View v;
        private Context context;

        public CustomAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            this.v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }
            TextView textView = (TextView) v.findViewById(android.R.id.text1);
            textView.setTextAppearance(context, android.R.style.TextAppearance_Small);
            textView.setText(getItem(position).toString());
            return v;
        }
    }
}
