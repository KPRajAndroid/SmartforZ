package com.example.wepopandroid1.myapplicationtest;

/**
 * Created by wepopandroid1 on 31/5/16.
 */
        import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;

/**
 * Created by Sort on 11/30/2015.
 */
public class model {
    private static model ourInstance = new model();
    Calendar c = Calendar.getInstance();
    //System.out.println("Current time => " + c.getTime());
    public String userId="",session_id= "";

    public static model getInstance() {
        return ourInstance;
    }
    public static boolean isInternetConnected (Context ctx) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }

    private model() {
    }






}