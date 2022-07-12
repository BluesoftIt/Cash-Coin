package com.bluesoftit.cashcoin.Models;

import android.app.Application;

import com.bluesoftit.cashcoin.R;
import com.onesignal.OneSignal;

public class cashCoin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.onSignalAppId));
    }
}
