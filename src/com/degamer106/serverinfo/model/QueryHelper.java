package com.degamer106.serverinfo.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public class QueryHelper {
	private static class BoxTable {
		public static final String TABLE_NAME = "box";
		public static final String _ID = "_id";
		public static final String REGION = "region";
		public static final String POSITION = "position";
		public static final String CREATE_STRING = 
				String.format("CREATE TABLE IF NOT EXISTS %s (" +
							  "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
							  "%s TEXT," + 
							  "%s INTEGER);",
							  TABLE_NAME,
							  _ID,
							  REGION,
							  POSITION);
	}
	
	private static class ServerTable {
		public static final String TABLE_NAME = "server";
		public static final String _ID = "_id";
		public static final String NAME = "name";
		public static final String STATUS = "status";
		public static final String BOX_POSITION = "box_position";
		public static final String CREATE_STRING =
				String.format("CREATE TABLE IF NOT EXISTS %s (" +
							  "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
							  "%s TEXT," +
							  "%s INTEGER," +
							  "%s INTEGER," +
							  "FOREIGN KEY (box_position) REFERENCES box(position));",
							  TABLE_NAME,
							  _ID,
							  NAME,
							  STATUS,
							  BOX_POSITION);
	}
	
	private static final List<Pair<String, String>> sTablesList = new ArrayList<Pair<String, String>>();
	private static final List<Pair<String, String>> sViewsList = new ArrayList<Pair<String, String>>();
	
	static {
		// Tables
		sTablesList.add(new Pair<String, String>(BoxTable.TABLE_NAME, BoxTable.CREATE_STRING));
		sTablesList.add(new Pair<String, String>(ServerTable.TABLE_NAME, ServerTable.CREATE_STRING));
		
		// Views
		sViewsList.add(new Pair<String, String>(ServersForBoxesTable.TABLE_NAME, ServersForBoxesTable.CREATE_STRING));
	}
	
	public List<Pair<String, String>> getTableList() {
		return sTablesList;
	}
	
	public List<Pair<String, String>> getViewsList() {
		return sViewsList;
	}
}
