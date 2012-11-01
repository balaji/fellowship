package com.thoughtworks.pumpkin;

import android.os.Bundle;
import android.widget.TextView;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;

public class FacebookLoginActivity extends FacebookActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session session = getSession();
        if(session != null) {
            session.closeAndClearTokenInformation();
        }
        setContentView(R.layout.index);
        this.openSession();
    }

    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
        if (state.isOpened()) {
            Request request = Request.newMeRequest(this.getSession(), new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        getSharedPreferences("com.thoughtworks.pumpkin_preferences", MODE_WORLD_WRITEABLE).edit().putString("username", user.getName()).commit();
                        ((TextView)findViewById(R.id.welcome)).setText("Hello " + user.getName() + "!");
                    }
                }
            });
            Request.executeBatchAsync(request);
        }
    }
}

