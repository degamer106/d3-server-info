package com.degamer106.serverinfo.adapter;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.util.FontChanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class PreferenceListAdapter extends BaseAdapter {
	private Context mContext;
	private CharSequence [] mEntries;
	
	class ViewHolder {
		private CheckedTextView mItem;
	}
	
	public PreferenceListAdapter(Context context, CharSequence [] entries) {
		mContext = context;
		mEntries = entries;
	}
	
	@Override
	public int getCount() {		
		return mEntries.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_pref, null);
			
			holder = new ViewHolder();
			holder.mItem = (CheckedTextView)convertView.findViewById(R.id.pref_item);
			holder.mItem.setTextColor(mContext.getResources().getColor(R.color.gold));
			FontChanger.getInstance(mContext).changeFont(holder.mItem);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.mItem.setText(mEntries[position]);
		
		return convertView;
	}

}
