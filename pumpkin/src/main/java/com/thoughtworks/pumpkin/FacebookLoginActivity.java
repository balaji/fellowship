package com.thoughtworks.pumpkin;

import android.os.Bundle;
import android.widget.TextView;
import com.facebook.FacebookActivity;
import com.facebook.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.SessionState;

public class FacebookLoginActivity extends FacebookActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello);
        this.openSession();
    }

    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
        if (state.isOpened()) {
            Request request = Request.newMeRequest(this.getSession(), new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        TextView welcome = (TextView) findViewById(R.id.welcome);
                        welcome.setText("Hello " + user.getName() + "!");
                    }
                }
            });
            Request.executeBatchAsync(request);
        }
    }
}

