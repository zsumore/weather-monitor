package com.fs121.monitor.model.ae;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.model.ISingleValueList;

public class AEWarnSingleValueList implements ISingleValueList<AEWarnValue> {

	@JsonProperty("sid")
	private String stationId;

	@JsonProperty("data")
	private List<AEWarnValue> listValue;

	public AEWarnSingleValueList() {

	}

	public AEWarnSingleValueList(String stationId, List<AEWarnValue> listValue) {

		this.stationId = stationId;
		this.listValue = listValue;
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
	public void setListValue(List<AEWarnValue> l) {
		this.listValue = l;

	}

	@Override
	public List<AEWarnValue> getListValue() {

		return this.listValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AEWarnSingleValueList [stationId=");
		builder.append(stationId);
		builder.append(", listValue=");
		builder.append(listValue);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listValue == null) ? 0 : listValue.hashCode());
		result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AEWarnSingleValueList other = (AEWarnSingleValueList) obj;
		if (listValue == null) {
			if (other.listValue != null)
				return false;
		} else if (!listValue.equals(other.listValue))
			return false;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
			return false;
		return true;
	}

}
