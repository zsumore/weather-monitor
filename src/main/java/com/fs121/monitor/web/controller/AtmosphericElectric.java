package com.fs121.monitor.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fs121.monitor.model.IAllValueList;
import com.fs121.monitor.model.StationLastTime;
import com.fs121.monitor.model.ae.AELagAllValueList;
import com.fs121.monitor.model.ae.AELagValueListParam;
import com.fs121.monitor.model.ae.AEWarnAllValueList;
import com.fs121.monitor.model.ae.AEWarnPeriodValueListParam;
import com.fs121.monitor.model.ae.AEWarnPeriodValue;
import com.fs121.monitor.model.ae.AEWarnValue;
import com.fs121.monitor.model.ae.AEWarnValueListParam;
import com.fs121.monitor.model.ae.AeLagValue;
import com.fs121.monitor.model.ae.AEWarnPeriodAllValueList;
import com.fs121.monitor.web.repository.AtmosphericElectricRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@EnableScheduling
@CrossOrigin(origins = "*", maxAge = 3600)
@ConfigurationProperties(prefix = "ae.controller")
@Controller
@RequestMapping("/AE")
public class AtmosphericElectric {

	public static final String ALL_STATION_LASTTIME = "all_station_lasttime";
	public static final String STATION_LASTTIME = "station_lasttime";

	private final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

	private final AtmosphericElectricRepository atmosphericElectricRepository;

	private final ObjectMapper jsonMapper;

	final Logger logger = LoggerFactory.getLogger(AtmosphericElectric.class);

	private Cache<Object, Object> cache;

	private Integer cacheMaximumSize, cacheExpire;

	private Cache<Object, Object> shortTimeCache;

	private Integer shortTimeCacheMaximumSize, shortTimeCacheExpire;

	public Cache<Object, Object> getCache() {
		if (null == cache) {
			cache = CacheBuilder.newBuilder().maximumSize(cacheMaximumSize)
					.expireAfterWrite(cacheExpire, TimeUnit.SECONDS).build();
		}
		return cache;
	}

	public Integer getCacheMaximumSize() {
		return cacheMaximumSize;
	}

	public void setCacheMaximumSize(Integer cacheMaximumSize) {
		this.cacheMaximumSize = cacheMaximumSize;
	}

	public Integer getCacheExpire() {
		return cacheExpire;
	}

	public void setCacheExpire(Integer cacheExpire) {
		this.cacheExpire = cacheExpire;
	}

	public Integer getShortTimeCacheMaximumSize() {
		return shortTimeCacheMaximumSize;
	}

	public void setShortTimeCacheMaximumSize(Integer shortTimeCacheMaximumSize) {
		this.shortTimeCacheMaximumSize = shortTimeCacheMaximumSize;
	}

	public Integer getShortTimeCacheExpire() {
		return shortTimeCacheExpire;
	}

	public void setShortTimeCacheExpire(Integer shortTimeCacheExpire) {
		this.shortTimeCacheExpire = shortTimeCacheExpire;
	}

	public Cache<Object, Object> getShortTimeCache() {
		if (null == shortTimeCache) {
			shortTimeCache = CacheBuilder.newBuilder().maximumSize(shortTimeCacheMaximumSize)
					.expireAfterWrite(shortTimeCacheExpire, TimeUnit.SECONDS).build();
		}
		return shortTimeCache;
	}

	@Autowired
	public AtmosphericElectric(AtmosphericElectricRepository atmosphericElectricRepository) {

		this.atmosphericElectricRepository = atmosphericElectricRepository;
		this.jsonMapper = new ObjectMapper();
		this.jsonMapper.configure(Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		this.jsonMapper.setDateFormat(new SimpleDateFormat(DATETIME_PATTERN));
	}

	/**
	 * http://localhost:8080/AtmosphericElectric/LastTime/59828
	 * 
	 * @param stationId
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/LastTime/{stationId}", method = RequestMethod.GET)
	public void findLastTimeByStationId(@PathVariable final String stationId, HttpServletRequest request,
			HttpServletResponse response) {

		StationLastTime result = this.atmosphericElectricRepository.findSingleStationLastTime(stationId);

		writeValue(response, result, "AtmosphericElectricSingleList is Null");

	}

	/**
	 * http://localhost:8080/AtmosphericElectric/LastTime/AllStation
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/LastTime/AllStation", method = RequestMethod.GET)
	public void findAllStationLastTime(HttpServletRequest request, HttpServletResponse response) {
		// logger.debug("Spring Load!--------------------------------");
		writeValue(response, findAllStationLastTimeFromShortTimeCache(), "AllStationLastTime is Null");

	}

	/**
	 * http://localhost:8080/AtmosphericElectric/Warning?_start=2016-04-14T16:00
	 * :00&_end=2016-04-14T16:05:00&_lag=0.1&_sid=59828,AE7019,,AE2224
	 * 
	 * @param sid
	 * @param start
	 * @param end
	 * @param lag
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/Warning", method = RequestMethod.GET)
	public void findAllStationWarnList(@RequestParam(value = "_sid", required = false) String sid,
			@RequestParam(value = "_start", required = true) @DateTimeFormat(pattern = DATETIME_PATTERN) Date start,
			@RequestParam(value = "_end", required = true) @DateTimeFormat(pattern = DATETIME_PATTERN) Date end,
			@RequestParam(value = "_lag", required = true) Double lag, HttpServletRequest request,
			HttpServletResponse response) {

		AEWarnValueListParam param = new AEWarnValueListParam(start, end, lag);
		if (sid != null) {
			param.setStationId(sid);
		}

		writeValue(response, findWarnListFromCache(param), "WarnList is Null");
	}

	/**
	 * http://localhost:8080/AtmosphericElectric/WarningPeriod?_start=2016-04-
	 * 14T16:00:00&_end=2016-04-14T17:00:00&_lag=0.1&_period=5
	 * http://localhost:8080/AtmosphericElectric/WarningPeriod?_sid=59828&_start
	 * =2016-04- 01T00:00:00&_end=2016-05-01T00:00:00&_lag=3&_period=60
	 * 
	 * @param sid
	 * @param start
	 * @param end
	 * @param lag
	 * @param period
	 * @param request
	 * @param response
	 */

	@RequestMapping(path = "/WarningPeriod", method = RequestMethod.GET)
	public void findAllStationWarnPeriodList(@RequestParam(value = "_sid", required = false) String sid,
			@RequestParam(value = "_start", required = true) @DateTimeFormat(pattern = DATETIME_PATTERN) Date start,
			@RequestParam(value = "_end", required = true) @DateTimeFormat(pattern = DATETIME_PATTERN) Date end,
			@RequestParam(value = "_lag", required = true) Double lag,
			@RequestParam(value = "_period", required = true) Integer period, HttpServletRequest request,
			HttpServletResponse response) {

		AEWarnPeriodValueListParam param = new AEWarnPeriodValueListParam(start, end, lag, period);
		if (sid != null) {
			param.setStationId(sid);
		}

		writeValue(response, findWarnPeriodListFromCache(param), "AEWarnPeriodValueList is Null");
	}

	private IAllValueList<AEWarnPeriodValueListParam, AEWarnPeriodValue> findWarnPeriodListFromCache(
			AEWarnPeriodValueListParam param) {

		IAllValueList<AEWarnPeriodValueListParam, AEWarnPeriodValue> result = (AEWarnPeriodAllValueList) this
				.getShortTimeCache().getIfPresent(param);
		if (null == result) {
			result = (AEWarnPeriodAllValueList) this.getCache().getIfPresent(param);
		}

		if (null == result) {
			result = this.atmosphericElectricRepository.findAllStationWarnPeriodList(param);
			if (null != result) {
				Cache<Object, Object> tempCache = getSuitableListCache(param.getEnd());
				tempCache.put(param, result);
			}

			logger.debug("------------Get Data from ##DB##find AEWarnPeriodAllValueList");
		} else {
			logger.debug("------------Get Data from ##Cache#find AEWarnPeriodAllValueList");
		}

		return result;
	}

	/**
	 * http://localhost:8080/AtmosphericElectric/NewestWarning/59828?_lag=0.1
	 * 
	 * @param stationId
	 * @param lag
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/NewestWarning/{stationId}", method = RequestMethod.GET)
	public void findSingleStationNewestWarnList(@PathVariable final String stationId,
			@RequestParam(value = "_lag", required = true) Double lag, HttpServletRequest request,
			HttpServletResponse response) {

		AEWarnValueListParam param = new AEWarnValueListParam();
		param.setLag(lag);
		param.setStationId(stationId);

		writeValue(response, this.atmosphericElectricRepository.findSingleStationNewestWarnList(param),
				"WarnBean is Null");

	}

	/**
	 * http://localhost:8080/AtmosphericElectric/NewestWarning/AllStation?_lag=0
	 * .1
	 * 
	 * @param lag
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/NewestWarning/AllStation", method = RequestMethod.GET)
	public void findAllStationNewestWarnList(@RequestParam(value = "_lag", required = true) Double lag,
			HttpServletRequest request, HttpServletResponse response) {

		AEWarnValueListParam param = new AEWarnValueListParam();
		param.setLag(lag);

		writeValue(response, findAllStationNewestWarnListFromCache(param), "WarnList is Null");
	}

	/**
	 * http://localhost:8080/AtmosphericElectric/LagValueList/59828?_start=2016-
	 * 04- 14T16:00:00&_end=2016-04-14T16:02:00
	 * 
	 * @param sid
	 * @param start
	 * @param end
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "/LagValueList/{stationId}", method = RequestMethod.GET)
	public void findAELagArrayStationListValue(@PathVariable final String stationId,
			@RequestParam(value = "_start", required = true) @DateTimeFormat(pattern = DATETIME_PATTERN) Date start,
			@RequestParam(value = "_end", required = true) @DateTimeFormat(pattern = DATETIME_PATTERN) Date end,
			HttpServletRequest request, HttpServletResponse response) {

		writeValue(response, findAtmosphericElectricSingleListFromCache(new AELagValueListParam(stationId, start, end)),
				"AtmosphericElectricSingleList is Null");

	}

	// TODO
	@Scheduled(cron = "0/20 1 * * * ?") // 每20秒执行一次
	public void schedulerResetStationLastTime() {

		getShortTimeCache().put(ALL_STATION_LASTTIME, this.atmosphericElectricRepository.findAllStationLastTime());

		logger.info(">>>>>>>>>>>>> schedulerResetStationLastTime ... ");
	}

	private Cache<Object, Object> getSuitableCache(String sid, Date date) {
		// List<StationLastTime> list=
		// findAllStationLastTimeFromShortTimeCache();

		Date lasttime = this.atmosphericElectricRepository.findSingleStationLastTime(sid).getDatetime();

		if (lasttime.getTime() - date.getTime() >= 0) {
			return getCache();
		}

		return getShortTimeCache();
	}

	private void writeValue(HttpServletResponse response, Object result, String errorMessage) {

		PrintWriter writer = null;

		try {
			writer = response.getWriter();

			if (null == result) {
				response.sendError(400, errorMessage);
			} else {
				jsonMapper.writeValue(writer, result);
			}
		} catch (JsonGenerationException e) {
			logError(writer, e);
		} catch (JsonMappingException e) {
			logError(writer, e);
		} catch (IOException e) {
			logError(writer, e);
		} finally {
			if (null != writer) {
				writer.flush();
				writer.close();
			}
		}

	}

	private void logError(PrintWriter writer, Throwable t) {
		if (null != writer) {
			writer.write("##ERROR##" + t.getClass().getName());
		}
		logger.error(t.getMessage());

	}

	private IAllValueList<AEWarnValueListParam, AEWarnValue> findWarnListFromCache(AEWarnValueListParam param) {

		IAllValueList<AEWarnValueListParam, AEWarnValue> result = (AEWarnAllValueList) this.getShortTimeCache()
				.getIfPresent(param);
		if (null == result) {
			result = (AEWarnAllValueList) this.getCache().getIfPresent(param);
		}

		if (null == result) {
			result = this.atmosphericElectricRepository.findAllStationWarnList(param);
			if (null != result) {
				Cache<Object, Object> tempCache = getSuitableListCache(param.getEnd());
				tempCache.put(param, result);
			}

			logger.debug("------------Get Data from ##DB##findWarnListFromCache");
		} else {
			logger.debug("------------Get Data from ##Cache#findWarnListFromCache");
		}

		return result;
	}

	private IAllValueList<AELagValueListParam, AeLagValue> findAtmosphericElectricSingleListFromCache(
			AELagValueListParam param) {

		IAllValueList<AELagValueListParam, AeLagValue> result = (AELagAllValueList) this.getShortTimeCache()
				.getIfPresent(param);
		if (null == result) {
			result = (AELagAllValueList) this.getCache().getIfPresent(param);
		}
		if (null == result) {
			result = this.atmosphericElectricRepository.findAELagAllStationValueList(param);
			if (null != result) {
				Cache<Object, Object> tempCache = getSuitableCache(param.getStationId(), param.getEnd());
				tempCache.put(param, result);
			}

			logger.debug("------------Get Data from ##DB##findAtmosphericElectricSingleListFromCache");
		} else {
			logger.debug("------------Get Data from ##Cache#findAtmosphericElectricSingleListFromCache");
		}

		return result;
	}

	private Object findAllStationNewestWarnListFromCache(AEWarnValueListParam param) {

		IAllValueList<AEWarnValueListParam, AEWarnValue> result = (AEWarnAllValueList) getShortTimeCache()
				.getIfPresent(param);

		if (null == result) {
			result = this.atmosphericElectricRepository.findAllStationNewestWarnList(param);
			if (null != result) {
				getShortTimeCache().put(param, result);
			}
		}
		return result;
	}

	private Cache<Object, Object> getSuitableListCache(Date date) {
		Date lasttime = new Date();

		if (lasttime.getTime() - date.getTime() < 1200000) {
			return getShortTimeCache();
		}

		return getCache();
	}

	private List<StationLastTime> findAllStationLastTimeFromShortTimeCache() {
		@SuppressWarnings("unchecked")
		List<StationLastTime> result = (List<StationLastTime>) getShortTimeCache().getIfPresent(ALL_STATION_LASTTIME);

		if (null == result) {
			result = this.atmosphericElectricRepository.findAllStationLastTime();
			if (null != result) {

				getShortTimeCache().put(ALL_STATION_LASTTIME, result);
			}

			logger.debug("------------Get Data from ##DB##findAllStationLastTimeFromShortTimeCache");
		} else {
			logger.debug("------------Get Data from ##Cache##findAllStationLastTimeFromShortTimeCache");
		}

		return result;
	}

}
