package com.andmap.ipicsta;

import java.io.Serializable;

public class Station implements Serializable  {
	private static final long serialVersionUID = 3605942306087194731L;
	private int _id;
	private int clockid;
	private String stationName;
	private String stationImagePath;
	
	public Station() {
	}
	
	public Station(int clockid, String stationName, String stationImagePath) {
		super();
		this.clockid = clockid;
		this.stationName = stationName;
		this.stationImagePath = stationImagePath;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getClockid() {
		return clockid;
	}
	public void setClockid(int clockid) {
		this.clockid = clockid;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationImagePath() {
		return stationImagePath;
	}
	public void setStationImagePath(String stationImagePath) {
		this.stationImagePath = stationImagePath;
	}

	@Override
	public String toString() {
		return "Station [_id=" + _id + ", clockid=" + clockid
				+ ", stationName=" + stationName + ", stationImagePath="
				+ stationImagePath + "]";
	}
	
}
