package com.degamer106.serverinfo.ui;

import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.service.D3Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;

public class SettingsActivity extends SherlockPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	private static final int ALARM_ID = 0;
	private static final Map<Long, String> sValueMap = new HashMap<Long, String>();
	
	static {
		sValueMap.put(AlarmManager.INTERVAL_FIFTEEN_MINUTES, "15 minutes");
		sValueMap.put(AlarmManager.INTERVAL_HALF_HOUR, "30 minutes");
		sValueMap.put(AlarmManager.INTERVAL_HOUR, "1 hour");
		sValueMap.put(AlarmManager.INTERVAL_HALF_DAY, "12 hours");
		sValueMap.put(AlarmManager.INTERVAL_DAY, "1 day");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		getListView().setSelector(R.drawable.tab_indicator_ab_example);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		prefs.registerOnSharedPreferenceChangeListener(this);
		initUi();
	}
	
	private void initUi() {
		changeUpdateIntervalSummary();
		changeRegionSummary();
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Settings");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		// TODO Auto-generated method stub
		if (key.equals("pref_key_automatic_updates")) {
			initAlarm();
		}
		else if (key.equals("pref_key_update_interval")) {
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			Intent intent = new Intent(this, D3Service.class);
			PendingIntent pi = PendingIntent.getService(this, ALARM_ID, intent, PendingIntent.FLAG_NO_CREATE);
			am.cancel(pi);
			
			if (pi != null)
				pi.cancel();
			
			initAlarm();
			changeUpdateIntervalSummary();
		}
		else if (key.equals("pref_key_notification_region")) {
			changeRegionSummary();
		}
	}

    private void initAlarm() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, D3Service.class);
        PendingIntent pi = PendingIntent.getService(this, ALARM_ID, intent, PendingIntent.FLAG_NO_CREATE);
        
        if (prefs.getBoolean("pref_key_automatic_updates", false) == true) {
          if (pi == null) {
			long interval = Long.valueOf(prefs.getString("pref_key_update_interval", "0"));
			
			pi = PendingIntent.getService(this, ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pi);
          }
        }
        else {
        	am.cancel(pi);
        	
        	if (pi != null)
        		pi.cancel();
        }
    }
    
    private void changeUpdateIntervalSummary() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	ListPreference lp = (ListPreference)findPreference("pref_key_update_interval");
    	long interval = Long.valueOf(prefs.getString("pref_key_update_interval", "0"));
		lp.setSummary(sValueMap.get(interval));
    }
    
    private void changeRegionSummary() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	ListPreference lp = (ListPreference)findPreference("pref_key_notification_region");
		lp.setSummary(prefs.getString("pref_key_notification_region", ""));
    }
}
