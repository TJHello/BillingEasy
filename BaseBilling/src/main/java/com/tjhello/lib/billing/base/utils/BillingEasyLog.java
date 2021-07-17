package com.tjhello.lib.billing.base.utils;

import android.util.Log;

public class BillingEasyLog {

    private static boolean IS_DEBUG = false;
    private static String TAG = "BillingEasyLog";

    public static void setVersionName(String versionName){
        TAG="BillingEasyLog-"+versionName;
    }

    public static void i(String log){
        if(!IS_DEBUG) return;
        Log.i(TAG,log);
    }
    public static void e(String log){
        if(!IS_DEBUG) return;
        Log.e(TAG,log);
    }

    public static void setDebug(boolean bool){
        IS_DEBUG = bool;
    }
}
