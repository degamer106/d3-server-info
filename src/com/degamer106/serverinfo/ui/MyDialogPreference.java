package com.degamer106.serverinfo.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.util.FontChanger;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MyDialogPreference extends DialogPreference implements OnActivityDestroyListener {
	private TextView mTitleTextView;
	private TextView mSummaryTextView;
	private int mColorGoldEnabled;
	private int mColorGoldDisabled;
	private int mColorOrangeEnabled;
	private int mColorOrangeDisabled;
	private Dialog mDialog;
	
	public MyDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setPositiveButton(null, null);
		builder.setNegativeButton(null, null);
		builder.setNeutralButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	@Override
	protected void onBindDialogView(View view) {
        TextView title = (TextView)view.findViewById(R.id.alertTitle);
		TextView author = (TextView)view.findViewById(R.id.author);
		TextView btag = (TextView)view.findViewById(R.id.btag);
		
		FontChanger.getInstance(getContext()).changeFont(title);
		FontChanger.getInstance(getContext()).changeFont(author);
		FontChanger.getInstance(getContext()).changeFont(btag);
		
		title.setText(getTitle());
		author.setText(R.string.pref_dialog_text_about_author);
		btag.setText(R.string.pref_dialog_text_about_btag);
	}

	@Override
	protected View onCreateView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View layout = super.onCreateView(parent);
		
		mColorGoldEnabled = getContext().getResources().getColor(R.color.gold);
		mColorGoldDisabled = getContext().getResources().getColor(R.color.gold_disabled);
		mColorOrangeEnabled = getContext().getResources().getColor(R.color.orange);
		mColorOrangeDisabled = getContext().getResources().getColor(R.color.orange_disabled);
		
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
		mSummaryTextView.setTextColor(mSummaryTextView.isEnabled() ? mColorOrangeEnabled : mColorOrangeDisabled);
	}

	@Override
	protected void showDialog(Bundle state) {
        Context context = getContext();        
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pref_dialog_about, null);

        onBindDialogView(view);
        mBuilder.setView(view);
        onPrepareDialogBuilder(mBuilder);
        
        // Create the dialog
        final Dialog dialog = mDialog = mBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (state != null) {
            dialog.onRestoreInstanceState(state);
        }
		
        registerActivityDestroyListener(this);
        dialog.setOnDismissListener(this);
        dialog.show();
        
        Button neutralButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEUTRAL);
		FontChanger.getInstance(getContext()).changeFont(neutralButton);
		neutralButton.setTextColor(mColorGoldEnabled);
		neutralButton.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ab_background_gradient_bottom));
	}
	
	private void registerActivityDestroyListener(OnActivityDestroyListener listener) {
		try {
			Class<?> c = Class.forName("android.preference.PreferenceManager");		    
			Method m = c.getDeclaredMethod("registerOnActivityDestroyListener", OnActivityDestroyListener.class);
			
    		m.setAccessible(true);
    		m.invoke(getPreferenceManager(), listener);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityDestroy() {		
        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }
        
        mDialog.dismiss();
	}
}
