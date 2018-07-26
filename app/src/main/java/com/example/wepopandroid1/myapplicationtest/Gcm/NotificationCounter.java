package com.example.wepopandroid1.myapplicationtest.Gcm;

import android.app.Application;

/**
 * Created by User on 3/22/2017.
 */

public class NotificationCounter extends Application {
    private static int pendingNotificationsCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static int getPendingNotificationsCount() {
        return pendingNotificationsCount;
    }

    public static void setPendingNotificationsCount(int pendingNotifications) {
        pendingNotificationsCount = pendingNotifications;
    }
}
