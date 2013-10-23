package com.degamer106.serverinfo.adapter;

import com.degamer106.serverinfo.ui.BoxFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BoxPagerAdapter extends FragmentStatePagerAdapter {
	private static final int NUM_ITEMS = 3;
	
	public BoxPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int position) {		
		return BoxFragment.newInstance(position);
	}

	@Override
	public int getCount() {
		return NUM_ITEMS;
	}
}