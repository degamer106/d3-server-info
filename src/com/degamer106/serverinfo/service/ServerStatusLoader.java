package com.degamer106.serverinfo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.degamer106.serverinfo.model.Box;
import com.degamer106.serverinfo.model.SQLiteDatabaseHelper;
import com.degamer106.serverinfo.model.SQLiteExecutor;
import com.degamer106.serverinfo.model.Server;
import com.degamer106.serverinfo.model.ServerStatusProvider;
import com.degamer106.serverinfo.model.ServersForBoxesTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class ServerStatusLoader {
	private static final String SERVER_STATUS_URL = "http://us.battle.net/d3/en/status";
	
	public static final int OLD_DATA = 0;
	public static final int NEW_DATA = 1;
	public static final int ERROR = -1;
	
	private Context mContext;
	
	public ServerStatusLoader(Context context) {
		mContext = context;
	}
	
	public int load() {
		int status = ERROR;
		
		try {
			Document doc = Jsoup.connect(SERVER_STATUS_URL).get();		
			List<Box> newBoxList = getNewBoxList(doc);
			List<Box> oldBoxList = getOldBoxList();
				
			if (oldBoxList.isEmpty()) {
				saveToDb(newBoxList);
				status = NEW_DATA;
			}
			else {
				if (!oldBoxList.equals(newBoxList)) {
					saveToDb(newBoxList);
					status = compareBoxesForRegion(oldBoxList, newBoxList);
				}
				else {
					status = OLD_DATA;
				}
			}
		}
		catch (IOException e) {
		}
		
		return status;
	}
	
	private int compareBoxesForRegion(List<Box> oldBoxList, List<Box> newBoxList) {		
		String region = PreferenceManager.getDefaultSharedPreferences(mContext).getString("pref_key_notification_region", null);
		Box oldBox = null;
		Box newBox = null;
		
		for (int i = 0; i < oldBoxList.size(); i++)
		
		for (Box box : oldBoxList) {
			if (box.getRegion().equals(region))
				oldBox = box;
		}
		
		for (Box box : newBoxList) {
			if (box.getRegion().equals(region))
				newBox = box;
		}
			
		if (oldBox.equals(newBox))
			return OLD_DATA;
		
		return NEW_DATA;
	}
	
	private ArrayList<Box> getNewBoxList(Document doc) {
		ArrayList<Box> boxList = new ArrayList<Box>();
		Elements boxes = doc.select("div.box");
		
		for (Element boxElem : boxes) {
			Box box = new Box();
			
			for (Element header3 : boxElem.getElementsByClass("header-3")) {
				box.setRegion(header3.text());
			}
			
			for (Element serverListElem : boxElem.getElementsByClass("server-list")) {				
				for (Element serverElem : serverListElem.getElementsByClass("server")) {
					if (serverElem.className().equals("server empty")) {
						break;
					}
					
					Server server = new Server();
					
					for (Element child : serverElem.children()) {
						if (child.attr("class").contains("status-icon")) {
							server.setStatus(child.attr("data-tooltip").equals("Available") ? 1 : 0);
						}
						else if (child.attr("class").equals("server-name")) {
							server.setName(child.text());
						}
					}
					
					box.addServer(server);
				}
			}
			
			boxList.add(box);
		}
		
		return boxList;
	}
	
	/**
	 * @param newBoxList the new box list data from the webpage.
	 * @return true if newBoxList.equals(oldBoxList). false if they are not equal.
	 */
	private List<Box> getOldBoxList() {
		Cursor cursor = mContext.getContentResolver().query(ServerStatusProvider.CONTENT_URI, null, null, null, null);
		List<Box> boxList = new ArrayList<Box>();
		int index = 0;
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Box box = new Box();
			
			box.setRegion(cursor.getString(cursor.getColumnIndex(ServersForBoxesTable.REGION)));
			while (!cursor.isAfterLast()) {
				if (cursor.getInt(cursor.getColumnIndex(ServersForBoxesTable.POSITION)) != index) {
					// move back one position. otherwise, we will skip a record.
					cursor.moveToPrevious();
					break;
				}
				
				Server server = new Server();
				
				server.setName(cursor.getString(cursor.getColumnIndex(ServersForBoxesTable.NAME)));
				server.setStatus(cursor.getInt(cursor.getColumnIndex(ServersForBoxesTable.STATUS)));
				box.addServer(server);
				
				cursor.moveToNext();
			}			
			boxList.add(box);
			
			index++;
			cursor.moveToNext();
		}
		cursor.close();
		
		return boxList;
	}
	
	private void saveToDb(List<Box> boxList) {		
		SQLiteDatabase db = SQLiteDatabaseHelper.getInstance(mContext).getWritableDatabase();		
		SQLiteExecutor executor = new SQLiteExecutor(db);
		int boxIndex = 0;
		
		clearDatabase(db);
		
		for (Box box : boxList) {
			boxSQLHelper(box, boxIndex, executor);
			
			for (Server server : box.getServerList()) 
				serverSQLHelper(server, boxIndex, executor);
			
			boxIndex++;
		}
		executor.executeSQLInsert();
	}
	
	private void clearDatabase(SQLiteDatabase db) {
		db.delete("box", null, null);
		db.delete("server", null, null);
	}
	
	private void boxSQLHelper(Box box, int boxIndex, SQLiteExecutor executor) {
		ContentValues boxValues = new ContentValues();
		
		boxValues.put("region", box.getRegion());
		boxValues.put("position", boxIndex);
		
		executor.addSQLInsert("box", boxValues);
	}
	
	private void serverSQLHelper(Server server, int boxIndex, SQLiteExecutor executor) {
		ContentValues serverValues = new ContentValues();
		
		serverValues.put("name", server.getName());
		serverValues.put("status", server.getStatus());
		serverValues.put("box_position", boxIndex);
		
		executor.addSQLInsert("server", serverValues);
	}
	

}
