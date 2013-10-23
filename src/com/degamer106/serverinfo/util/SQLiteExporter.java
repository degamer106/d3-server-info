package com.degamer106.serverinfo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Environment;

public class SQLiteExporter {
	private static final String CURRENT_DB_PATH = "//data//com.degamer106.d3companion//databases//server_status.db";
	private static final String BACKUP_DB_PATH = "server_status.db";
	
	@SuppressWarnings("resource")
	public static void export() {
		try {
	        File sd = Environment.getExternalStorageDirectory();
	        File data = Environment.getDataDirectory();

            String currentDBPath = CURRENT_DB_PATH;
            String backupDBPath = BACKUP_DB_PATH;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}
