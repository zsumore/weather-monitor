package com.fs121.monitor.model.ae;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fs121.monitor.util.CustomDoubleSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AeLagValue {

	@JsonProperty("dt")
	private Date datetime;

	@JsonProperty("v")
	private Double value;

	@JsonProperty("d1")
	private Double diff1;

	public AeLagValue() {

	}

	public AeLagValue(Date datetime, Double value, Double diff1) {

		this.datetime = datetime;
		this.value = value;
		this.diff1 = diff1;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@JsonSerialize(using = CustomDoubleSerialize.class)
	public Double getDiff1() {
		return diff1;
	}

	public void setDiff1(Double diff1) {
		this.diff1 = diff1;
	}

	@Override
	public String toString() {
		return "DatetimeValue [datetime=" + datetime + ", value=" + value + ", diff1=" + diff1 + "]";
	}

}
