package com.thoughtworks.pumpkin.helper;

import android.app.ProgressDialog;
import roboguice.activity.RoboActivity;
import roboguice.util.SafeAsyncTask;

public abstract class PumpkinAsyncTask<T> extends SafeAsyncTask<T> {
    protected ProgressDialog progressDialog;
    protected RoboActivity activity;

    public PumpkinAsyncTask activity(RoboActivity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    protected void onPreExecute() throws Exception {
        progressDialog = Util.showProgressDialog(activity);
    }

    @Override
    protected void onSuccess(T t) throws Exception {
        super.onSuccess(t);
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }
}
