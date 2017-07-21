package com.mofiler.mofiler;

import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.provider.Settings;
import android.util.DebugUtils;
import android.util.Log;

import com.unity3d.player.UnityPlayer;
import com.mofiler.Mofiler;

/**
 * Created by hernaez on 5/3/17.
 */

public class MofilerBridge {

    public static String _SetURL(String url)
    {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.setURL(url);

        return mof.getURL();
    }
    public static String _SetAppKey(String key)
    {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.setSdkTypeAndVersion("Unity Android SDK", "1.0.4");
        mof.setAppKey(key);
        return mof.getAppKey();

    }
    public static String _SetAppName(String name)
    {
        Log.v("AppKey",name);
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.setAppName(name);
        return mof.getAppName();

    }

    public static String _AddIdentity(String key, String value)
    {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.addIdentity(key, value);
        return mof.getIdentity(key);
    }

    public static String _SetUseVerboseContext(boolean value) {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.setUseVerboseContext(value);
        return "Verbose";
    }
    public static String _SetUseLocation(boolean value) {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.setUseLocation(value);
        return "Location";
    }
    public static String _InjectValue(String key, String value)
    {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.injectValue(key, value);
        return "Inject";
    }

    public static String _FlushDataToMofiler()
    {
        Mofiler mof = Mofiler.getInstance(UnityPlayer.currentActivity.getApplicationContext());
        mof.flushDataToMofiler();
        return "Flushed";
    }

}

