package com.degamer106.serverinfo.model;

public class ServersForBoxesTable {
	public static final String TABLE_NAME = "ServersForBoxes";
	public static final String _ID = "_id";
	public static final String POSITION = "position";
	public static final String REGION = "region";
	public static final String NAME = "name";
	public static final String STATUS = "status";
	public static final String CREATE_STRING = "CREATE VIEW IF NOT EXISTS ServersForBoxes AS SELECT server._id, box.position, box.region, server.name, server.status FROM box INNER JOIN server ON box.position = server.box_position ORDER BY server._id";
}
