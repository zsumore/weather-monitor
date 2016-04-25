package com.fs121.monitor.web.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.fs121.monitor.model.IAllValueList;
import com.fs121.monitor.model.StationLastTime;
import com.fs121.monitor.model.ae.AELagValueListParam;
import com.fs121.monitor.model.ae.AEWarnPeriodValueListParam;
import com.fs121.monitor.model.ae.AEWarnPeriodValue;
import com.fs121.monitor.model.ae.AEWarnValue;
import com.fs121.monitor.model.ae.AEWarnValueListParam;
import com.fs121.monitor.model.ae.AeLagValue;

public interface AtmosphericElectricRepository {

	IAllValueList<AELagValueListParam, AeLagValue> findAELagAllStationValueList(AELagValueListParam param)
			throws DataAccessException;

	StationLastTime findSingleStationLastTime(String sid) throws DataAccessException;

	List<StationLastTime> findAllStationLastTime() throws DataAccessException;

	IAllValueList<AEWarnValueListParam, AEWarnValue> findAllStationWarnList(AEWarnValueListParam param)
			throws DataAccessException;

	IAllValueList<AEWarnValueListParam, AEWarnValue> findSingleStationNewestWarnList(AEWarnValueListParam param)
			throws DataAccessException;

	IAllValueList<AEWarnValueListParam, AEWarnValue> findAllStationNewestWarnList(AEWarnValueListParam param)
			throws DataAccessException;

	IAllValueList<AEWarnPeriodValueListParam, AEWarnPeriodValue> findAllStationWarnPeriodList(AEWarnPeriodValueListParam param)
			throws DataAccessException;

}
