package com.degamer106.serverinfo.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

public class SQLiteExecutor {
	private static final String TAG = SQLiteExecutor.class.getSimpleName();
	private SQLiteDatabase mDb;
	private List<Pair<String, ContentValues>> mSQLItems;
	
	public SQLiteExecutor(SQLiteDatabase db) {
		mDb = db;
		mSQLItems = new ArrayList<Pair<String, ContentValues>>();
	}
	
	public void addSQLInsert(String tableName, ContentValues values) {
		mSQLItems.add(new Pair<String, ContentValues>(tableName, values));
	}
	
	public void executeSQLInsert() {
		mDb.beginTransaction();
		
		try {	
			for (Pair<String, ContentValues> item : mSQLItems) {
				long rowId = mDb.insert(item.first, null, item.second);
				Log.i(TAG, "rowId=" + rowId);
			}
			mDb.setTransactionSuccessful();
		}
		catch (SQLException e) {
			Log.e(TAG, "Error with SQL insert", e);
		}
		finally {
			mDb.endTransaction();
		}
		
	}
}
