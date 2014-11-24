package com.mofiler.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.mofiler.service.AlarmService;

public class OnBootReceiver extends BroadcastReceiver {

	/**
	 * Listens for Android's BOOT_COMPLETED broadcast and then executes
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
 			Log.d("mofiler boot receiver", "The Boot Receiver is called");
			Intent mServiceIntent = new Intent(context, AlarmService.class);
			context.startService(mServiceIntent);
			Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
		} else {
			Log.d("mofiler boot receiver ", "Something went wrong with the boot complete maybe your applicacion doesn't have permissions ");
		}
  	}
}