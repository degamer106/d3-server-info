package com.degamer106.serverinfo.model;

public class Server {
	private String mName;
	private int mStatus;
	
	// Mutators
	public void setName(String name) {
		mName = name;
	}
	public void setStatus(int status) {
		mStatus = status;
	}
	// Accessors
	public String getName() {
		return mName;
	}
	public int getStatus() {
		return mStatus;
	}
	@Override
	public boolean equals(Object o) {
		Server other = (Server)o;		
		return (this.mName.equals(other.mName)) && (this.mStatus == other.mStatus);
	}
}
