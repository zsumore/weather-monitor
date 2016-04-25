package com.fs121.monitor.model.ae;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.util.Util;

public class AEWarnPeriodValueListParam {

	@JsonProperty("sid")
	private String stationId = Util.ALLSTATION;

	private Date start, end;

	private Double lag;

	private Integer period;

	public AEWarnPeriodValueListParam() {

	}

	public AEWarnPeriodValueListParam(String sid, Date start, Date end, Double lag, Integer period) {

		this.stationId = sid;
		this.start = start;
		this.end = end;
		this.lag = lag;
		this.period = period;
	}

	public AEWarnPeriodValueListParam(Date start, Date end, Double lag, Integer period) {

		this.start = start;
		this.end = end;
		this.lag = lag;
		this.period = period;
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

	public Double getLag() {
		return lag;
	}

	public void setLag(Double lag) {
		this.lag = lag;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AEWarnPeriodParam [stationId=");
		builder.append(stationId);
		builder.append(", start=");
		builder.append(start);
		builder.append(", end=");
		builder.append(end);
		builder.append(", lag=");
		builder.append(lag);
		builder.append(", period=");
		builder.append(period);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((lag == null) ? 0 : lag.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
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
		AEWarnPeriodValueListParam other = (AEWarnPeriodValueListParam) obj;
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
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
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
