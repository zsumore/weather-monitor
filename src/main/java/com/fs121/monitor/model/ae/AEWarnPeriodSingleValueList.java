package com.fs121.monitor.model.ae;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.model.ISingleValueList;

public class AEWarnPeriodSingleValueList implements ISingleValueList<AEWarnPeriodValue> {

	@JsonProperty("sid")
	private String stationId;

	@JsonProperty("data")
	private List<AEWarnPeriodValue> listValue;

	public AEWarnPeriodSingleValueList(String stationId, List<AEWarnPeriodValue> listValue) {

		this.stationId = stationId;
		this.listValue = listValue;
	}

	public AEWarnPeriodSingleValueList() {

	}

	@Override
	public String getStationId() {

		return this.stationId;
	}

	@Override
	public void setStationId(String sid) {
		this.stationId = sid;

	}

	@Override
	public void setListValue(List<AEWarnPeriodValue> l) {
		this.listValue = l;

	}

	@Override
	public List<AEWarnPeriodValue> getListValue() {

		return this.listValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AEWarnPeriodSingleValueList [stationId=");
		builder.append(stationId);
		builder.append(", listValue=");
		builder.append(listValue);
		builder.append("]");
		return builder.toString();
	}

}
