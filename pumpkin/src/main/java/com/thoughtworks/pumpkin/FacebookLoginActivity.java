package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.os.Bundle;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.thoughtworks.pumpkin.helper.Constant;

public class FacebookLoginActivity extends FacebookActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session session = getSession();
        if(session != null) {
            session.closeAndClearTokenInformation();
        }
        this.openSession();
    }

    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
        final FacebookLoginActivity facebookLoginActivity = this;
        if (state.isOpened()) {
            Request request = Request.newMeRequest(this.getSession(), new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        getSharedPreferences(Constant.Preferences.FILE_NAME, MODE_WORLD_WRITEABLE).edit().putString(Constant.Preferences.USERNAME, user.getName()).commit();
                       startActivity(new Intent(facebookLoginActivity, HomeActivity.class));
                    }
                }
            });
            Request.executeBatchAsync(request);
        }
    }
}

