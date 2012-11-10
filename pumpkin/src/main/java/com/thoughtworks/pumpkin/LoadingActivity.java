package com.thoughtworks.pumpkin;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoadingActivity extends AsyncTask<Void, Void, Void> {
    ProgressDialog progress;
    public LoadingActivity(ProgressDialog progress) {
        this.progress = progress;
    }

    public void onPreExecute() {
        progress.show();
    }

    public Void doInBackground(Void... unused) {
        return null;
    }

    public void onPostExecute(Void unused) {
        progress.dismiss();
    }
}

