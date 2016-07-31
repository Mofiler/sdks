package com.mofiler.service;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mofiler.receivers.AlarmNotificationReceiver;
import com.mofiler.util.Utils;

public class AlarmService extends Service {
	private final String TAG = "MofilerAlarmService";

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "created");
 		//initializeAlarms(getApplicationContext());
 		//INIT STUFF HERE
		boolean useVerboseContext = Utils.getSharedPreferences(getApplicationContext()).getBoolean("useVerboseContext", false);
		if (useVerboseContext)
			doScheduleNextInjection(getApplicationContext());
 	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "started");
		return super.onStartCommand(intent, START_STICKY, startId);
	}
	
	public static void doScheduleNextInjection(Context context)
	{
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, AlarmNotificationReceiver.class);
		int uniqueCode = Utils.getUniqueCode(context);
		intent.setAction(String.valueOf(uniqueCode));
	
		PendingIntent sender = PendingIntent.getBroadcast(context, uniqueCode, intent, PendingIntent.FLAG_ONE_SHOT);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60000, sender);			
			
		
	}
	
	public static void cancelCurrentScheduledInjection(Context context) {
		int reqCode = Utils.getUniqueCode(context);
		reqCode--;

		if (reqCode >= 0){
			
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
	
			Intent intent = new Intent(context, AlarmNotificationReceiver.class);
			intent.setAction(String.valueOf(reqCode));
			PendingIntent sender = PendingIntent.getBroadcast(context, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
			if (sender != null)
			{
				alarmManager.cancel(sender);		
			}
		}
	}


	
}
