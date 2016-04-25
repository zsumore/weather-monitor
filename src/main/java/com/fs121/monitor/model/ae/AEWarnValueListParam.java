package com.fs121.monitor.model.ae;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fs121.monitor.util.CustomDoubleSerialize;
import com.fs121.monitor.util.Util;

public class AEWarnValueListParam {
	@JsonProperty("sid")
	private String stationId = Util.ALLSTATION;

	private Date start, end;

	private Double lag;

	public AEWarnValueListParam() {

	}

	public AEWarnValueListParam(String sid, Date start, Date end, Double lag) {
		this.stationId = sid;
		this.start = start;
		this.end = end;
		this.lag = lag;
	}

	public AEWarnValueListParam(Date start, Date end, Double lag) {

		this.start = start;
		this.end = end;
		this.lag = lag;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getLag() {
		return lag;
	}

	public void setLag(Double lag) {
		this.lag = lag;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AEWarnAllValueListParam [stationId=");
		builder.append(stationId);
		builder.append(", start=");
		builder.append(start);
		builder.append(", end=");
		builder.append(end);
		builder.append(", lag=");
		builder.append(lag);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((lag == null) ? 0 : lag.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		AEWarnValueListParam other = (AEWarnValueListParam) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (lag == null) {
			if (other.lag != null)
				return false;
		} else if (!lag.equals(other.lag))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
			return false;
		return true;
	}

}
