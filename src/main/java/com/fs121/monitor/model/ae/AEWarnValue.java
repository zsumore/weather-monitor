package com.fs121.monitor.model.ae;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AEWarnValue {

	private Integer count;

	@JsonIgnore
	private String stationId;

	public AEWarnValue() {

	}

	public AEWarnValue(Integer count, String stationId) {
		super();
		this.count = count;
		this.stationId = stationId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

}
