package com.degamer106.serverinfo.adapter;

import com.degamer106.serverinfo.R;
import com.degamer106.serverinfo.model.ServersForBoxesTable;
import com.degamer106.serverinfo.util.FontChanger;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ServerCursorAdapter extends CursorAdapter {
	private static final String [] sHeaders = new String [] {"Game Server", "Auction House"};
	private static final int [] sStatusIcons = new int [] {R.drawable.server_offline, R.drawable.server_online};
	
	class ViewHolder {
		private TextView header;
		private TextView serverName;
		private ImageView serverStatus;
	}
	
	public ServerCursorAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();		
		View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
		FontChanger fontChanger = FontChanger.getInstance(context);
		
		holder.header = (TextView)view.findViewById(R.id.header);
		holder.serverName = (TextView)view.findViewById(R.id.server_name);
		holder.serverStatus = (ImageView)view.findViewById(R.id.server_status);
		
		fontChanger.changeFont(holder.header);
		fontChanger.changeFont(holder.serverName);
		
		view.setTag(holder);
		
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder)view.getTag();
		final String name = cursor.getString(cursor.getColumnIndex(ServersForBoxesTable.NAME));
		final int status = cursor.getInt(cursor.getColumnIndex(ServersForBoxesTable.STATUS));
		final int position = cursor.getPosition();
		
		switch (position) {
			case 0:
			case 1:
				holder.header.setText(sHeaders[position]);
				holder.header.setVisibility(View.VISIBLE);
				break;
			default:
				holder.header.setText("");
				holder.header.setVisibility(View.GONE);
		}
		
		int lightMaroon = context.getResources().getColor(R.color.light_maroon);
		int darkRed = context.getResources().getColor(R.color.dark_red);
		view.setBackgroundColor(position % 2 == 0 ? darkRed : lightMaroon);
		
		holder.serverName.setText(name);
		holder.serverStatus.setImageResource(sStatusIcons[status]);
	}
}
