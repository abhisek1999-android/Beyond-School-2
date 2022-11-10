package com.maths.beyond_school_new_071020221400;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class App extends Application {
NotificationManager nManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        nManager = ((NotificationManager) getApplicationContext().getSystemService(NotificationManager.class));
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        nManager.cancelAll();
    }
}
