package com.degamer106.serverinfo.model;

import java.util.ArrayList;
import java.util.List;

public class Box {
	private String mRegion;
	private List<Server> mServerList;
	
	public Box() {
		mServerList = new ArrayList<Server>();
	}
	
	public void setRegion(String region) {
		mRegion = region;
	}
	public void addServer(Server server) {
		mServerList.add(server);
	}
	
	public String getRegion() {
		return mRegion;
	}
	public List<Server> getServerList() {
		return mServerList;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		Box other = (Box)o;
		return (this.mRegion.equals(mRegion) && this.mServerList.equals(other.mServerList));
	}
}
