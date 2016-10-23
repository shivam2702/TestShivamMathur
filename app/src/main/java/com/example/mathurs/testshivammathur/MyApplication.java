package com.example.mathurs.testshivammathur;

import android.app.Application;

import com.helpshift.All;
import com.helpshift.Core;
import com.helpshift.InstallConfig;
import com.helpshift.exceptions.InstallException;
import com.helpshift.support.Support;

/**
 * Created by Mathurs on 23-10-2016.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // You initialize the library by calling Core.install(APPLICATION, API_KEY, DOMAIN,
        // APP_ID) in your application's onCreate()

        Core.init(All.getInstance());
        InstallConfig installConfig = new InstallConfig.Builder()
                .setEnableInAppNotification(true)
                .build();
        try {
            Core.install(this,
                    "23b2e1b73fb543f5b9f13fdd9c7a9869",
                    "testshivam.helpshift.com",
                    "testshivam_platform_20161022194943531-3027637828868ba",
                    installConfig);
        } catch (InstallException e) {
            android.util.Log.e("Helpshift", "install call : ", e);
        }

        android.util.Log.d("Helpshift", Support.libraryVersion + " - is the version for gradle");

    }

}
