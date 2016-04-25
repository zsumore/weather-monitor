package com.fs121.monitor.web.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fs121.monitor.model.IAllValueList;
import com.fs121.monitor.model.ISingleValueList;
import com.fs121.monitor.model.StationLastTime;
import com.fs121.monitor.model.ae.AELagAllValueList;
import com.fs121.monitor.model.ae.AELagSingleValueList;
import com.fs121.monitor.model.ae.AELagValueListParam;
import com.fs121.monitor.model.ae.AEWarnAllValueList;
import com.fs121.monitor.model.ae.AEWarnPeriodAllValueList;
import com.fs121.monitor.model.ae.AEWarnPeriodSingleValueList;
import com.fs121.monitor.model.ae.AEWarnPeriodValue;
import com.fs121.monitor.model.ae.AEWarnPeriodValueListParam;
import com.fs121.monitor.model.ae.AEWarnSingleValueList;
import com.fs121.monitor.model.ae.AEWarnValue;
import com.fs121.monitor.model.ae.AEWarnValueListParam;
import com.fs121.monitor.model.ae.AeLagValue;
import com.fs121.monitor.util.Util;
import com.fs121.monitor.web.repository.AtmosphericElectricRepository;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

@ConfigurationProperties(prefix = "ae.repository")
@Repository
public class JdbcAtmosphericElectricRepositoryImpl implements AtmosphericElectricRepository {

	private DataSource dataSource;

	private DataSource meteoDataSource;

	private NamedParameterJdbcTemplate meteoJdbcTemplate;

	private JdbcTemplate jdbcTemplate;

	private Integer warnGap;

	public Integer getWarnGap() {
		return warnGap;
	}

	public void setWarnGap(Integer warnGap) {
		this.warnGap = warnGap;
	}

	private List<String> stationList = new ArrayList<>();

	public List<String> getStationList() {
		return stationList;
	}

	public void setStationList(List<String> stationList) {
		this.stationList = stationList;
	}

	@Autowired
	public JdbcAtmosphericElectricRepositoryImpl(@Qualifier("dataSource") DataSource dataSource,
			@Qualifier("meteoDataSource") DataSource meteoDataSource) {
		super();
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
		this.meteoDataSource = meteoDataSource;
		this.meteoJdbcTemplate = new NamedParameterJdbcTemplate(this.meteoDataSource);
	}

	@Override
	public IAllValueList<AELagValueListParam, AeLagValue> findAELagAllStationValueList(AELagValueListParam param)
			throws DataAccessException {
		IAllValueList<AELagValueListParam, AeLagValue> bean = new AELagAllValueList();
		bean.setParam(param);

		List<ISingleValueList<AeLagValue>> data = new ArrayList<>();

		ISingleValueList<AeLagValue> lv = new AELagSingleValueList();
		lv.setStationId(param.getStationId());
		List<AeLagValue> list = this.meteoJdbcTemplate.query(
				"SELECT datetime, intensity, diff1 FROM rt.v_aef_lag where station=:sid and (datetime between :start and :end) order by datetime;",
				new HashMap<String, Object>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 2986997958095091675L;

					{
						put("sid", param.getStationId());
						put("start", param.getStart());
						put("end", param.getEnd());
					}
				}, new RowMapper<AeLagValue>() {

					public AeLagValue mapRow(ResultSet rs, int rowNum) throws SQLException {
						AeLagValue row = new AeLagValue();
						row.setDatetime(rs.getTimestamp("datetime"));
						row.setValue(rs.getDouble("intensity"));
						row.setDiff1(rs.getDouble("diff1"));
						return row;
					}
				});
		lv.setListValue(list);
		data.add(lv);
		bean.setData(data);
		return bean;
	}

	@Override
	public StationLastTime findSingleStationLastTime(String sid) throws DataAccessException {

		Date lastTime = this.meteoJdbcTemplate.queryForObject(
				"SELECT datetime FROM rt.v_aef_lag where station = :sid order by datetime desc limit 1",
				Collections.singletonMap("sid", sid), Date.class);

		return new StationLastTime(sid, lastTime);
	}

	@Override
	public List<StationLastTime> findAllStationLastTime() throws DataAccessException {
		if (null != this.stationList) {
			return this.stationList.parallelStream().map(p -> findSingleStationLastTime(p))
					.collect(Collectors.toList());
		}
		return null;
	}

	@Override
	public IAllValueList<AEWarnValueListParam, AEWarnValue> findAllStationWarnList(AEWarnValueListParam warnParam)
			throws DataAccessException {
		IAllValueList<AEWarnValueListParam, AEWarnValue> list = new AEWarnAllValueList();
		list.setParam(warnParam);

		list.setData(getWarnListByParam(warnParam));
		return list;
	}

	private List<ISingleValueList<AEWarnValue>> getWarnListByParam(final AEWarnValueListParam param)
			throws DataAccessException {
		List<ISingleValueList<AEWarnValue>> list = new ArrayList<>();

		final List<String> stationList = genStationInClause(param.getStationId());

		List<AEWarnValue> valueList = this.meteoJdbcTemplate.query(
				"SELECT station, count(id) as count FROM rt.v_aef_lag where station in(:sList) and diff1 >= :diff1 and (datetime between :start and :end) Group by station;",

				new HashMap<String, Object>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = -3695511598521437756L;

					{
						put("sList", stationList);
						put("diff1", param.getLag());
						put("start", param.getStart());
						put("end", param.getEnd());
					}
				}, new RowMapper<AEWarnValue>() {
					public AEWarnValue mapRow(ResultSet rs, int rowNum) throws SQLException {
						AEWarnValue bean = new AEWarnValue();
						bean.setStationId(rs.getString("station"));
						bean.setCount(rs.getInt("count"));
						return bean;
					}
				});
		// System.out.println(valueList.size());

		ListMultimap<String, AEWarnValue> listMultimap = ArrayListMultimap.create();

		for (AEWarnValue v : valueList) {
			listMultimap.put(v.getStationId(), v);
		}
		for (String s : stationList) {

			AEWarnSingleValueList e = new AEWarnSingleValueList(s, listMultimap.get(s));
			if (e.getListValue() != null && e.getListValue().size() > 0) {
				list.add(e);
			}
		}

		return list;
	}

	private List<String> genStationInClause(String param) {
		if (param.equalsIgnoreCase(Util.ALLSTATION)) {
			return this.stationList;
		} else {
			String t = param.trim();

			return Arrays.asList(t.split(","));
		}

	}

	@Override
	public IAllValueList<AEWarnValueListParam, AEWarnValue> findSingleStationNewestWarnList(
			AEWarnValueListParam warnParam) throws DataAccessException {
		StationLastTime lastTime = this.findSingleStationLastTime(warnParam.getStationId());
		warnParam.setEnd(lastTime.getDatetime());
		Date start = new Date();
		start.setTime(warnParam.getEnd().getTime() - this.warnGap * 1000);
		warnParam.setStart(start);

		return this.findAllStationWarnList(warnParam);
	}

	private AEWarnValueListParam genAtmosphericElectricWarnParam(String sid, Double lag) {
		AEWarnValueListParam param = new AEWarnValueListParam();
		param.setLag(lag);
		param.setStationId(sid);

		StationLastTime lastTime = this.findSingleStationLastTime(param.getStationId());
		param.setEnd(lastTime.getDatetime());
		Date start = new Date();
		start.setTime(param.getEnd().getTime() - this.warnGap * 1000);
		param.setStart(start);
		return param;
	}

	@Override
	public IAllValueList<AEWarnValueListParam, AEWarnValue> findAllStationNewestWarnList(AEWarnValueListParam warnParam)
			throws DataAccessException {

		IAllValueList<AEWarnValueListParam, AEWarnValue> list = new AEWarnAllValueList();
		list.setParam(warnParam);

		list.setData(this.stationList.parallelStream().map(p -> genAtmosphericElectricWarnParam(p, warnParam.getLag()))
				.collect(Collectors.toList()).parallelStream().map(q -> getSingleWarnListByParam(q))
				.collect(Collectors.toList()));

		return list;

	}

	private ISingleValueList<AEWarnValue> getSingleWarnListByParam(final AEWarnValueListParam param)
			throws DataAccessException {

		List<AEWarnValue> valueList = this.meteoJdbcTemplate.query(
				"SELECT  count(id) as count FROM rt.v_aef_lag where station =:sid and diff1 >= :diff1 and (datetime between :start and :end) ;",
				new HashMap<String, Object>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = 7248898709069692844L;

					{
						put("sid", param.getStationId());
						put("diff1", param.getLag());
						put("start", param.getStart());
						put("end", param.getEnd());
					}
				}, new RowMapper<AEWarnValue>() {
					public AEWarnValue mapRow(ResultSet rs, int rowNum) throws SQLException {
						AEWarnValue bean = new AEWarnValue();
						bean.setStationId(param.getStationId());
						bean.setCount(rs.getInt("count"));
						return bean;
					}
				});

		// ListMultimap<String, AEWarnValue> listMultimap =
		// ArrayListMultimap.create();

		ISingleValueList<AEWarnValue> e = new AEWarnSingleValueList(param.getStationId(), valueList);

		return e;
	}

	@Override
	public IAllValueList<AEWarnPeriodValueListParam, AEWarnPeriodValue> findAllStationWarnPeriodList(
			AEWarnPeriodValueListParam param) throws DataAccessException {
		IAllValueList<AEWarnPeriodValueListParam, AEWarnPeriodValue> allValueList = new AEWarnPeriodAllValueList();
		allValueList.setParam(param);

		List<ISingleValueList<AEWarnPeriodValue>> listAEWarnPeriodValue = new ArrayList<>();

		final List<String> stationList = genStationInClause(param.getStationId());

		List<AEWarnPeriodValue> valueList = this.meteoJdbcTemplate.query(
				"select  station, count(id) as count, to_timestamp(floor(extract(epoch from datetime)/(60 * :period)) * 60 * :period) as period FROM rt.v_aef_lag where station in(:sList) and diff1>=:diff1 and (datetime between :start and :end) group by station,period order by station, period;",

				new HashMap<String, Object>() {

					/**
					 * 
					 */
					private static final long serialVersionUID = -3695511598521437756L;

					{
						put("period", param.getPeriod());
						put("sList", stationList);
						put("diff1", param.getLag());
						put("start", param.getStart());
						put("end", param.getEnd());
					}
				}, new RowMapper<AEWarnPeriodValue>() {
					public AEWarnPeriodValue mapRow(ResultSet rs, int rowNum) throws SQLException {
						AEWarnPeriodValue bean = new AEWarnPeriodValue();
						bean.setStationId(rs.getString("station"));
						bean.setCount(rs.getInt("count"));
						bean.setDatetime(rs.getTimestamp("period"));
						return bean;
					}
				});

		ListMultimap<String, AEWarnPeriodValue> listMultimap = ArrayListMultimap.create();

		for (AEWarnPeriodValue v : valueList) {
			listMultimap.put(v.getStationId(), v);
		}
		for (String s : stationList) {

			AEWarnPeriodSingleValueList ae = new AEWarnPeriodSingleValueList(s, listMultimap.get(s));
			if (ae.getListValue() != null && ae.getListValue().size() > 0) {
				listAEWarnPeriodValue.add(ae);
			}
		}

		allValueList.setData(listAEWarnPeriodValue);

		return allValueList;
	}

}
