package com.example.saggu.myapplication;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Saggu on 1/26/2017.
 */

public class FirebaseInstanceID extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        String refreshedtokan = FirebaseInstanceId.getInstance().getToken();

        Log.d("Tokan: ", refreshedtokan);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
      //  sendRegistrationToServer(refreshedToken);
    }
}
