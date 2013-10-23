package com.degamer106.serverinfo.ui;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.adapter.BoxPagerAdapter;
import com.degamer106.serverinfo.service.D3Service;
import com.degamer106.serverinfo.util.FontChanger;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockFragmentActivity {
	private static final String KEY_VIEWPAGER_INDEX = "viewpagerIndex";
	private static final String KEY_VIEWPAGER_VISIBILITY = "viewpager_visibility";
	private static final String KEY_ERROR_LABEL_VISIBILITY = "error_label_visibility";
	private static final String KEY_LOADING_LAYOUT_VISIBILITY = "loading_layout_visibility";
	private static final int ALARM_ID = 0;
	
	private ViewPager mViewPager;
	private LinearLayout mLoadingLayout;
	private LinearLayout mActionRefresh;
//	private LinearLayout mActionSettings;
	private TextView mErrorLabel;
	private BoxPagerAdapter mBoxPagerAdapter;
	
	private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			mActionRefresh.setEnabled(true);
			mLoadingLayout.setVisibility(View.GONE);
			
			if (intent.getAction().matches(D3Service.LOAD_COMPLETE)) {
				mViewPager.setVisibility(View.VISIBLE);
				mErrorLabel.setVisibility(View.GONE);
//				Toast.makeText(getApplicationContext(), intent.getStringExtra(D3Service.KEY_LOAD_MSG), Toast.LENGTH_LONG)
//	 	 	 	 	 .show();
			}
			else if (intent.getAction().matches(D3Service.LOAD_ERROR)) {
				mErrorLabel.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.GONE);
			}
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        registerReceiver(mDataReceiver, new IntentFilter(D3Service.LOAD_COMPLETE));
        registerReceiver(mDataReceiver, new IntentFilter(D3Service.LOAD_ERROR));

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        initUi();
        
//        boolean fromNotification = getIntent().getBooleanExtra(D3Service.KEY_NOTIFICATION, false);
        
        if (savedInstanceState == null) {
   	        mErrorLabel.setVisibility(View.GONE);
   	        mViewPager.setVisibility(View.GONE);
   	        mLoadingLayout.setVisibility(View.VISIBLE);
   	        startService(new Intent(this, D3Service.class));
        }
        
        initAlarm();
    }
    
    private void initUi() {
    	 mErrorLabel = (TextView)findViewById(R.id.error_label);
    	 mLoadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
    	 mActionRefresh = (LinearLayout)findViewById(R.id.action_refresh);
//    	 mActionSettings = (LinearLayout)findViewById(R.id.action_settings);
    	
    	 mBoxPagerAdapter = new BoxPagerAdapter(getSupportFragmentManager());
         
         mViewPager = (ViewPager)findViewById(R.id.pager);
         mViewPager.setAdapter(mBoxPagerAdapter);
         mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
 			@Override
 			public void onPageSelected(int position) {
 				getSupportActionBar().setSelectedNavigationItem(position);
 			}
 			@Override
 			public void onPageScrolled(int arg0, float arg1, int arg2) {}
 			@Override
 			public void onPageScrollStateChanged(int arg0) {}
 		 });
         
         ActionBar actionBar = getSupportActionBar();
         actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
         actionBar.setDisplayShowTitleEnabled(false);       
         actionBar.setDisplayShowHomeEnabled(false);
         
         String [] regions = getResources().getStringArray(R.array.regions);
         for (String region : regions) {
			View customView = LayoutInflater.from(this).inflate(R.layout.action_bar_tab, null);
			TextView title = (TextView)customView.findViewById(R.id.title);
			FontChanger.getInstance(this).changeFont(title);
			title.setText(region);
        	 
         	Tab tab = actionBar.newTab();
         	tab.setText(region);
         	tab.setCustomView(customView);
         	tab.setTabListener(new TabListener() {
				@Override
         		public void onTabSelected(Tab tab, FragmentTransaction ft) {
					mViewPager.setCurrentItem(tab.getPosition());
				}
				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {}
			});
         	actionBar.addTab(tab);
         }
         
         FontChanger.getInstance(this).changeFont(mErrorLabel);
         FontChanger.getInstance(this).changeFont((TextView)findViewById(R.id.refresh_label));
         FontChanger.getInstance(this).changeFont((TextView)findViewById(R.id.settings_label));
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
        	
        	mViewPager.setVisibility(View.VISIBLE);
  	        mLoadingLayout.setVisibility(View.GONE);
  	        mErrorLabel.setVisibility(View.GONE);
        }
    }
    
    /**
     * Since ADT 14, you're not allowed to use switch statements with View IDs and library projects.
     * @param view
     */
    public void onClick(View view) {
    	int id = view.getId();
    	
    	if (id == R.id.action_refresh) {
    		startService(new Intent(this, D3Service.class));
    		mViewPager.setVisibility(View.GONE);
    		mErrorLabel.setVisibility(View.GONE);
    		mLoadingLayout.setVisibility(View.VISIBLE);
    		view.setEnabled(false);
    	}
    	else if (id == R.id.action_settings) {
    		startActivity(new Intent(this, SettingsActivity.class));
    	}
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_VIEWPAGER_INDEX, mViewPager.getCurrentItem());
		outState.putInt(KEY_VIEWPAGER_VISIBILITY, mViewPager.getVisibility());
		outState.putInt(KEY_ERROR_LABEL_VISIBILITY, mErrorLabel.getVisibility());
		outState.putInt(KEY_LOADING_LAYOUT_VISIBILITY, mLoadingLayout.getVisibility());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		mViewPager.setCurrentItem(savedInstanceState.getInt(KEY_VIEWPAGER_INDEX), false);		
		mViewPager.setVisibility(savedInstanceState.getInt(KEY_VIEWPAGER_VISIBILITY));
		mErrorLabel.setVisibility(savedInstanceState.getInt(KEY_ERROR_LABEL_VISIBILITY));
		mLoadingLayout.setVisibility(savedInstanceState.getInt(KEY_LOADING_LAYOUT_VISIBILITY));
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mDataReceiver);
	}
}
