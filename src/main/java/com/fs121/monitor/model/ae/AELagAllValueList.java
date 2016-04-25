package com.fs121.monitor.model.ae;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fs121.monitor.model.IAllValueList;
import com.fs121.monitor.model.ISingleValueList;

public class AELagAllValueList
		implements IAllValueList<AELagValueListParam, AeLagValue> {
	private AELagValueListParam param;
	
	@JsonProperty("dataArray")
	private List<ISingleValueList<AeLagValue>> data;

	public AELagAllValueList() {

	}

	public AELagAllValueList(AELagValueListParam param,
			List<ISingleValueList<AeLagValue>> data) {
		this.param = param;
		this.data = data;
	}

	@Override
	public AELagValueListParam getParam() {

		return param;
	}

	@Override
	public List<ISingleValueList<AeLagValue>> getData() {

		return data;
	}

	public void setParam(AELagValueListParam param) {
		this.param = param;
	}

	public void setData(List<ISingleValueList<AeLagValue>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AELagArrayStationListValue [param=");
		builder.append(param);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
