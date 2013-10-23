package com.degamer106.serverinfo.ui;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.util.FontChanger;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyCheckBoxPreference extends CheckBoxPreference {
	private TextView mTitleTextView;
	private TextView mSummaryTextView;
	private int mColorGoldEnabled;
	private int mColorGoldDisabled;
	private int mOrangeEnabled;
	private int mOrangeDisabled;

	public MyCheckBoxPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    public MyCheckBoxPreference(Context context) {
        this(context, null);
    }
    
	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = super.onCreateView(parent);
		
		mColorGoldEnabled = getContext().getResources().getColor(R.color.gold);
		mColorGoldDisabled = getContext().getResources().getColor(R.color.gold_disabled);
		mOrangeEnabled = getContext().getResources().getColor(R.color.orange);
		mOrangeDisabled = getContext().getResources().getColor(R.color.orange_disabled);
		
		mTitleTextView = (TextView)layout.findViewById(android.R.id.title);
		mSummaryTextView = (TextView)layout.findViewById(android.R.id.summary);
		
		FontChanger.getInstance(getContext()).changeFont(mTitleTextView);
		FontChanger.getInstance(getContext()).changeFont(mSummaryTextView);
		
		return layout;
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
		

		mTitleTextView.setTextColor(mTitleTextView.isEnabled() ? mColorGoldEnabled : mColorGoldDisabled);
		mSummaryTextView.setTextColor(mSummaryTextView.isEnabled() ? mOrangeEnabled : mOrangeDisabled);
	}
}
