package com.fs121.monitor.model.ae;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.model.IAllValueList;
import com.fs121.monitor.model.ISingleValueList;

public class AEWarnPeriodAllValueList implements IAllValueList<AEWarnPeriodValueListParam, AEWarnPeriodValue> {

	private AEWarnPeriodValueListParam param;

	@JsonProperty("dataArray")
	private List<ISingleValueList<AEWarnPeriodValue>> data;

	public AEWarnPeriodAllValueList(AEWarnPeriodValueListParam param, List<ISingleValueList<AEWarnPeriodValue>> data) {

		this.param = param;
		this.data = data;
	}

	public AEWarnPeriodAllValueList() {

	}

	@Override
	public AEWarnPeriodValueListParam getParam() {

		return this.param;
	}

	@Override
	public void setParam(AEWarnPeriodValueListParam param) {
		this.param = param;

	}

	@Override
	public List<ISingleValueList<AEWarnPeriodValue>> getData() {

		return this.data;
	}

	@Override
	public void setData(List<ISingleValueList<AEWarnPeriodValue>> data) {
		this.data = data;

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AEWarnPeriodAllValueList [param=");
		builder.append(param);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
