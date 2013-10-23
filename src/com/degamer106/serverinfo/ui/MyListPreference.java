package com.degamer106.serverinfo.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.adapter.PreferenceListAdapter;
import com.degamer106.serverinfo.util.FontChanger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyListPreference extends ListPreference implements OnActivityDestroyListener {
	private TextView mTitleTextView;
	private TextView mSummaryTextView;
	private int mColorGoldEnabled;
	private int mColorGoldDisabled;
	private int mOrangeEnabled;
	private int mOrangeDisabled;
	private Dialog mDialog;

	public MyListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
	
    private int getValueIndex() {
        return findIndexOfValue(getValue());
    }

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setPositiveButton(null, null);
		builder.setNegativeButton(null, null);
		builder.setNeutralButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	
	@Override
	protected void onBindDialogView(View view) {
        TextView title = (TextView)view.findViewById(R.id.alertTitle);
		FontChanger.getInstance(getContext()).changeFont(title);
		title.setText(getTitle());
	}
	
	@Override
	protected void showDialog(Bundle state) {
        Context context = getContext();        
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pref_dialog_list, null);		
      
        onBindDialogView(view);
        mBuilder.setView(view);
        onPrepareDialogBuilder(mBuilder);
        
        // Create the dialog
        final Dialog dialog = mDialog = mBuilder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        ListView lv = (ListView)view.findViewById(R.id.listview);
//        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.select_dialog_singlechoice, getEntries());
        PreferenceListAdapter adapter = new PreferenceListAdapter(getContext(), getEntries());
		lv.setAdapter(adapter);
		
		lv.setClickable(true);
		lv.setEnabled(true);
		lv.setSelector(R.drawable.tab_indicator_ab_example);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setItemChecked(getValueIndex(), true);
		lv.setOnItemClickListener(new OnItemClickListener() {
		      @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        setValueIndex(position);
		        dialog.dismiss();
		    }
		});
		
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
