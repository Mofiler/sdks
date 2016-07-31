package com.mofiler.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mofiler.Mofiler;
import com.mofiler.MofilerClient;
import com.mofiler.device.MO_Device;
import com.mofiler.service.AlarmService;


public class AlarmNotificationReceiver extends BroadcastReceiver {
    Bundle extras;

    private static String TAG = AlarmNotificationReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "ENTER");
        if (intent.getExtras() != null) {

            // here we need to check all processes, and inject value into mofiler, and re-schedule alarm
            Mofiler mof;
            mof = Mofiler.getInstance(context);

            //if we have verbose flag set,  we will send running apps. If we have location at least,
            //we will send probe with location (location tracking).
            mof.injectValue(MofilerClient.K_MOFILER_PROBE_KEY_NAME, MO_Device.getExtras(context, mof.isUseVerboseContext()).toString());

            //finally reschedule next injection
            AlarmService.doScheduleNextInjection(context);
        }
    }
}
