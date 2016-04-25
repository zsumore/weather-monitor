package com.fs121.monitor.model.ae;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.model.IAllValueList;
import com.fs121.monitor.model.ISingleValueList;

public class AEWarnAllValueList implements IAllValueList<AEWarnValueListParam, AEWarnValue> {

	private AEWarnValueListParam param;

	@JsonProperty("dataArray")
	private List<ISingleValueList<AEWarnValue>> data;

	public AEWarnAllValueList() {

	}

	public AEWarnAllValueList(AEWarnValueListParam param, List<ISingleValueList<AEWarnValue>> data) {

		this.param = param;
		this.data = data;
	}

	@Override
	public AEWarnValueListParam getParam() {

		return param;
	}

	@Override
	public void setParam(AEWarnValueListParam param) {
		this.param = param;

	}

	@Override
	public List<ISingleValueList<AEWarnValue>> getData() {

		return this.data;
	}

	@Override
	public void setData(List<ISingleValueList<AEWarnValue>> data) {
		this.data = data;

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AEWarnAllValueList [param=");
		builder.append(param);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
