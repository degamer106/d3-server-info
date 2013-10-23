package com.degamer106.serverinfo.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "server_status.db";
	private static final int DB_VERSION = 2;
	private static final QueryHelper sQueryHelper = new QueryHelper();
	private static SQLiteDatabaseHelper mInstance;
	
	public static SQLiteDatabaseHelper getInstance(Context context) {
		if (mInstance == null)
			mInstance = new SQLiteDatabaseHelper(context);
		
		return mInstance;
	}
	
	private SQLiteDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create Tables
		for (Pair<String, String> item : sQueryHelper.getTableList()) 
			db.execSQL(item.second);
		
		// Create Views
		for (Pair<String, String> item : sQueryHelper.getViewsList())
			db.execSQL(item.second);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (Pair<String, String> item : sQueryHelper.getTableList()) 
			db.execSQL("DROP TABLE IF EXISTS " + item.first);
		
		for (Pair<String, String> item : sQueryHelper.getViewsList())
			db.execSQL("DROP VIEW IF EXISTS " + item.first);
		
		onCreate(db);
	}

}
