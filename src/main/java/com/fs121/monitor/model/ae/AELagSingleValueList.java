package com.fs121.monitor.model.ae;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.model.ISingleValueList;

public class AELagSingleValueList implements ISingleValueList<AeLagValue> {

	@JsonProperty("sid")
	private String stationId;

	@JsonProperty("data")
	private List<AeLagValue> listValue;

	public AELagSingleValueList() {

	}

	public AELagSingleValueList(String stationId, List<AeLagValue> listValue) {

		this.stationId = stationId;
		this.listValue = listValue;
	}

	@Override
	public String getStationId() {

		return stationId;
	}

	@Override
	public List<AeLagValue> getListValue() {

		return listValue;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public void setListValue(List<AeLagValue> listValue) {
		this.listValue = listValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AELagStationListValue [stationId=");
		builder.append(stationId);
		builder.append(", listValue=");
		builder.append(listValue);
		builder.append("]");
		return builder.toString();
	}

}
