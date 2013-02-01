package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.os.Bundle;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.thoughtworks.pumpkin.fragment.SidePanel;

public class FacebookLoginActivity extends RoboSherlockActivity {

    final FacebookLoginActivity facebookLoginActivity=this;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.empty);

        if(SidePanel.logout==true)
            Session.getActiveSession().closeAndClearTokenInformation();

        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {

                if (session.isOpened()) {

                    // make request to the /me API
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {

                            if (user != null) {

                                startActivity(new Intent(facebookLoginActivity,ZipCodeActivity.class));
                            }
                        }
                    });
                }

            }
        });
    }

}
