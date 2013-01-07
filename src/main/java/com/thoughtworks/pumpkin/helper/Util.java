package com.thoughtworks.pumpkin.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

public class Util {

    public static boolean isConnectingToInternet(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info == null) {
                return false;
            }
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static AlertDialog dialog(String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setNeutralButton(Constant.Message.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public static AlertDialog dialog(Context context, final View layout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options:");
        builder.setView(layout);
        builder.setPositiveButton(Constant.Message.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }

    public static ProgressDialog showProgressDialog(Activity activity) {
        return ProgressDialog.show(activity, "", Constant.Message.LOADING, true, false);
    }
}
