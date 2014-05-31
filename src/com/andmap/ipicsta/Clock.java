package com.andmap.ipicsta;

import java.io.Serializable;

public class Clock implements Serializable {
	private static final long serialVersionUID = 8811433915820317007L;
	private int _id;
	private String clockName;
	private String clockImagePath;
	
	public Clock(){}

	public Clock(String clockName, String clockImagePath) {
		super();
		this.clockName = clockName;
		this.clockImagePath = clockImagePath;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getClockName() {
		return clockName;
	}

	public void setClockName(String clockName) {
		this.clockName = clockName;
	}

	public String getClockImagePath() {
		return clockImagePath;
	}

	public void setClockImagePath(String clockImagePath) {
		this.clockImagePath = clockImagePath;
	}

	@Override
	public String toString() {
		return "Clock [_id=" + _id + ", clockName=" + clockName
				+ ", clockImagePath=" + clockImagePath + "]";
	}
	
}
