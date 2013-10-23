package com.degamer106.serverinfo.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontChanger {
	private static final String FONT_PATH = "fonts/exocet_light.ttf";
	private static FontChanger sInstance;
	private static Typeface sTypeface;
	
	private FontChanger(Context context) {
		sTypeface = Typeface.createFromAsset(context.getAssets(), FONT_PATH);
	}
	
	public static FontChanger getInstance(Context context) {
		if (sInstance == null) 
			sInstance = new FontChanger(context);
		
		return sInstance;
	}
	
	public void changeFont(TextView tv) {
		tv.setTypeface(sTypeface);
	}
}
