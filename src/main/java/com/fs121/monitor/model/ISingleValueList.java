package com.fs121.monitor.model;

import java.util.List;

public interface ISingleValueList<E> {

	String getStationId();

	void setStationId(String sid);

	void setListValue(List<E> l);

	List<E> getListValue();

}
