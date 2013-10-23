package com.degamer106.serverinfo.service;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.model.ServerStatusProvider;
import com.degamer106.serverinfo.ui.MainActivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class D3Service extends IntentService {
	private static final String TAG = D3Service.class.getSimpleName();	
	
	public static final String KEY_LOAD_MSG = "load_msg";
	public static final String KEY_NOTIFICATION = "key_notification";
	public static final String LOAD_COMPLETE = "com.degamer106.serverinfo.LOAD_COMPLETE";
	public static final String LOAD_ERROR = "com.degamer106.serverinfo.LOAD_ERROR";
	
	public D3Service() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Intent broadcastIntent = new Intent();
		int status = new ServerStatusLoader(this).load();
		String action = null;
		String msg = null;
		
		switch (status) {
			case ServerStatusLoader.OLD_DATA:
				action = LOAD_COMPLETE;
				msg = "No new updates for " +
					  PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_notification_region", null);
				break;
			case ServerStatusLoader.NEW_DATA:
				getContentResolver().notifyChange(ServerStatusProvider.CONTENT_URI, null);
				action = LOAD_COMPLETE;
				msg = "Server status has been updated for " + 
					  PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_notification_region", null);
				showNotification(msg);
				break;
			case ServerStatusLoader.ERROR:
				action = LOAD_ERROR;
				msg = "Error retrieving server status";
				showNotification(msg);
				break;
		}
		
		
		sendBroadcast(broadcastIntent.setAction(action)
				 					 .putExtra(KEY_LOAD_MSG, msg));
	}
	
	private void showNotification(String msg) {			
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (prefs.getBoolean("pref_key_allow_notifications", false) == false)
			return;
		
		Intent newIntent = new Intent(this, MainActivity.class);
//		newIntent.putExtra(KEY_NOTIFICATION, true);
		
		PendingIntent pi = PendingIntent.getActivity(this, 0, newIntent, 0);
		Notification noti = new NotificationCompat.Builder(this)
		        .setContentTitle("Server status")
		        .setContentText(msg)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pi)
		        .build();
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		if (prefs.getBoolean("pref_key_vibrate", false) == true)
			noti.defaults |= Notification.DEFAULT_VIBRATE;
		if (prefs.getBoolean("pref_key_ringtone", false) == true)
			noti.defaults |= Notification.DEFAULT_SOUND;
				
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, noti); 
	}
}
