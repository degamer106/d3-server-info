package com.degamer106.serverinfo.ui;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.util.FontChanger;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPreferenceCategory extends PreferenceCategory {
	private TextView mTitleTextView;
	private int mColorOrangeEnabled;

	public MyPreferenceCategory(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = super.onCreateView(parent);
				
		mColorOrangeEnabled = getContext().getResources().getColor(R.color.orange);
		mTitleTextView = (TextView)layout.findViewById(android.R.id.title);		
		FontChanger.getInstance(getContext()).changeFont(mTitleTextView);
		
		return layout;
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
		mTitleTextView.setTextColor(mColorOrangeEnabled);
	}
}
