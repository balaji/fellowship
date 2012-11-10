package com.thoughtworks.pumpkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.thoughtworks.pumpkin.helper.Constant;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import com.thoughtworks.pumpkin.helper.ConnectionDetector;
import android.app.AlertDialog;
import android.content.DialogInterface;

import javax.inject.Inject;

public class SigninActivity extends RoboActivity {

    @Inject
    SharedPreferences preferences;

    @InjectView(R.id.twitterSignin)
    Button twitterButton;

    @InjectView(R.id.facebookSignin)
    Button facebookButton;

    @InjectView(R.id.googleSignin)
    Button googleButton;

    @InjectView(R.id.yahooSignin)
    Button yahooButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (preferences.getString(Constant.Preferences.USERNAME, null) != null) {
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }
        setContentView(R.layout.signin);
        final SigninActivity signinActivity = this;

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Network Settings");

        // Setting Dialog Message
        alertDialog.setMessage("Connection Not Established");

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });



        final ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isInternetPresent = cd.isConnectingToInternet();
                if(!isInternetPresent)
                {
                    // Showing Alert Message
                    alertDialog.show();
                }
                else
                startActivity(new Intent(signinActivity, TwitterLoginActivity.class));
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isInternetPresent = cd.isConnectingToInternet();
                if(!isInternetPresent)
                {
                    // Showing Alert Message
                    alertDialog.show();
                }
                else
                startActivity(new Intent(signinActivity, FacebookLoginActivity.class));
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isInternetPresent = cd.isConnectingToInternet();
                if(!isInternetPresent)
                {
                    // Showing Alert Message
                    alertDialog.show();
                }
                else
                startActivity(new Intent(signinActivity, GoogleLoginActivity.class));
            }
        });

        yahooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isInternetPresent = cd.isConnectingToInternet();
                if(!isInternetPresent)
                {
                    // Showing Alert Message
                    alertDialog.show();
                }
                else
                startActivity(new Intent(signinActivity, YahooLoginActivity.class));
            }
        });
    }
}

