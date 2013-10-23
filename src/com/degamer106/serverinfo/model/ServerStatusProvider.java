package com.degamer106.serverinfo.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ServerStatusProvider extends ContentProvider {
	private static final String AUTHORITY = "com.degamer106.serverinfo.model";
	private static final String PATH = String.format("%s/%s", AUTHORITY, ServersForBoxesTable.TABLE_NAME);
	private static final int SERVERS_FOR_BOXES_ID = 1;
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sUriMatcher.addURI(AUTHORITY, PATH, SERVERS_FOR_BOXES_ID);
	}
	
	public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", ServerStatusProvider.AUTHORITY, ServerStatusProvider.PATH));
	
	@Override
	public boolean onCreate() {		
		return false;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String [] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = SQLiteDatabaseHelper.getInstance(getContext()).getReadableDatabase();
		Cursor cursor = null;
		
		switch (sUriMatcher.match(uri)) {
			case SERVERS_FOR_BOXES_ID:
				cursor = db.query(ServersForBoxesTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		}
		
		if (cursor != null)
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
