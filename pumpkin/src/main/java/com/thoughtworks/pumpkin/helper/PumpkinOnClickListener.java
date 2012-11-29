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
            Util.dialog(Constant.Message.NO_INTERNET_CONNECTION, activity).show();
            return;
        }
        done(view);
    }

    public Activity getActivity() {
        return activity;
    }

    public abstract void done(View view);
}
