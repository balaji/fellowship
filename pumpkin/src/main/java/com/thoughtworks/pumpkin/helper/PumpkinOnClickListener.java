package com.thoughtworks.pumpkin.helper;

import android.app.Activity;
import android.view.View;

public abstract class PumpkinOnClickListener implements View.OnClickListener {
    Activity activity;
    protected PumpkinOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        if(!Util.isConnectingToInternet(activity)) {
            Util.showDialog(Constant.Message.NO_INTERNET_CONNECTION, activity);
            return;
        }
        done(view);
    }

    public abstract void done(View view);
}
