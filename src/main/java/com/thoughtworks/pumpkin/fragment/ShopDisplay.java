package com.thoughtworks.pumpkin.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.skyhookwireless.wps.Location;
import com.skyhookwireless.wps.RegistrationCallback;
import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSPeriodicLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.XPS;
import com.thoughtworks.pumpkin.R;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class ShopDisplay extends RoboFragment {

    private XPS xps;
    private boolean isRegistrationRequired;
    private boolean isRegistering;
    private String username = "";
    private String realm = "";
    private MyRegistrationCallback registrationCallback = new MyRegistrationCallback();
    private static final int LOCATION_MESSAGE = 1;
    private static final int ERROR_MESSAGE = 2;
    private static final int REGISTRATION_SUCCESS_MESSAGE = 4;
    private static final int REGISTRATION_ERROR_MESSAGE = 5;
    private static final int LOCATION_UPDATE_FAILED_MESSAGE = 6;
    private Handler handler;
    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        xps = new XPS(getActivity());
        xps.setTiling("", 0, 0, null);
        xps.setLocalFilePaths(new ArrayList<String>(Arrays.asList(new String[]{""})));
        isRegistrationRequired = true;
        setUIHandler();
        return inflater.inflate(R.layout.shop_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        String category = getActivity().getIntent().getStringExtra("category");
//        String wishlist = getActivity().getIntent().getStringExtra("wishlist");
//        String shop = getActivity().getIntent().getStringExtra("shop");
        tv = (TextView) view.findViewById(R.id.gpsDisplay);
        view.findViewById(R.id.readLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLocation();
            }
        });
    }

    private void updateLocation() {
        final WPSAuthentication auth = new WPSAuthentication(username, realm);
        xps.getXPSLocation(auth, 1, 30, new WPSPeriodicLocationCallback() {
            @Override
            public WPSContinuation handleWPSPeriodicLocation(WPSLocation wpsLocation) {
                handler.sendMessage(handler.obtainMessage(LOCATION_MESSAGE, wpsLocation));
                return WPSContinuation.WPS_CONTINUE;
            }

            @Override
            public void done() {
            }

            @Override
            public WPSContinuation handleError(WPSReturnCode wpsReturnCode) {
                handler.sendMessage(handler.obtainMessage(LOCATION_UPDATE_FAILED_MESSAGE, wpsReturnCode));
                return WPSContinuation.WPS_CONTINUE;
            }
        });
    }

    private void registerUser(WPSAuthentication auth) {
        isRegistrationRequired = false;
        isRegistering = true;
        tv.setText("Starting registration");
        xps.registerUser(auth, null, registrationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRegistrationRequired)
            registerUser(new WPSAuthentication(username, realm));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isRegistering) {
            isRegistrationRequired = true;
        }
        xps.abort();
    }

    private void setUIHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                switch (msg.what) {
                    case LOCATION_MESSAGE:
                        final Location location = (Location) msg.obj;
                        tv.setText(location.toString());
                        return;
                    case ERROR_MESSAGE:
                        tv.setText(((WPSReturnCode) msg.obj).name());
                        return;
                    case REGISTRATION_SUCCESS_MESSAGE:
                        tv.setText("Registration succeeded");
                        return;
                    case REGISTRATION_ERROR_MESSAGE:
                        tv.setText("Registration failed (" + ((WPSReturnCode) msg.obj).name() + ")");
                        return;
                    case LOCATION_UPDATE_FAILED_MESSAGE:
                        tv.setText("Location update failed (" + ((WPSReturnCode) msg.obj).name() + ")");
                        return;
                }
            }
        };
    }

    private class MyRegistrationCallback implements RegistrationCallback {
        public void done() {
            isRegistering = false;
        }

        public void handleSuccess() {
            handler.sendMessage(handler.obtainMessage(REGISTRATION_SUCCESS_MESSAGE));
        }

        public WPSContinuation handleError(final WPSReturnCode error) {
            handler.sendMessage(handler.obtainMessage(REGISTRATION_ERROR_MESSAGE, error));
            return WPSContinuation.WPS_CONTINUE;
        }
    }
}
