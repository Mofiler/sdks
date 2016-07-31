package com.mofiler.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static String mSharedPreferencesName = "mofPreferences";

    public static boolean isNullOrWhitespace(String strtocheck) {
        return !(strtocheck != null && strtocheck.trim().length() > 0);
    }

    public static String getSafeString(String strtocheck) {
        if (strtocheck == null)
            return "";
        else
            return strtocheck.trim();
    }


    public static boolean isMyServiceRunning(Context context, Class<?> ServiceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(mSharedPreferencesName, 0);
    }

    public static SharedPreferences.Editor getPreferencesEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    public static int getUniqueCode(Context ctx) {
        int value;
        value = Utils.getSharedPreferences(ctx).getInt("unqCode", -1);
        value = value + 1;
        Utils.getPreferencesEditor(ctx).putInt("unqCode", value).commit();
        return value;
    }

}
