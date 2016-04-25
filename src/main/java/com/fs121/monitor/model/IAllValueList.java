package com.fs121.monitor.model;

import java.util.List;

public interface IAllValueList<P, E> {

	P getParam();

	void setParam(P param);

	List<ISingleValueList<E>> getData();

	void setData(List<ISingleValueList<E>> data);
	
	

}
