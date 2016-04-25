package com.fs121.monitor.web.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;

@Configuration
@ConfigurationProperties(prefix = "druid.fs121")
public class Fs121DruidConfig {

	private String jdbcUrl;

	private String username;

	private String password;

	private String driverClass;

	private Integer initialSize;

	private Integer minIdle;

	private Integer maxActive;

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	@Primary
	@Bean(name = "dataSource", destroyMethod = "close", initMethod = "init")
	public DataSource dataSource() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);

		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
		// 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
		dataSource.setFilters("stat,wall");

		Slf4jLogFilter filter = new Slf4jLogFilter();
		filter.setStatementExecutableSqlLogEnable(true);

		dataSource.setProxyFilters(Lists.newArrayList(filter));

		// # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
		dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
		dataSource.setUseGlobalDataSourceStat(true);
		// dataSource.init();

		// System.out.println(dataSource.toString());
		// System.out.println(driverClass);
		return dataSource;
	}

}
