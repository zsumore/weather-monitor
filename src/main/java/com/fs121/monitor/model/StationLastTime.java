package com.fs121.monitor.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationLastTime {

	@JsonProperty("sid")
	private String stationId;

	@JsonProperty("dt")
	private Date datetime;

	public StationLastTime() {
		super();

	}

	public StationLastTime(String stationId, Date datetime) {
		super();
		this.stationId = stationId;
		this.datetime = datetime;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StationLastTime [stationId=");
		builder.append(stationId);
		builder.append(", datetime=");
		builder.append(datetime);
		builder.append("]");
		return builder.toString();
	}

}
