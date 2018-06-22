package com.aek.ebey.cms.core.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.aek.common.core.aspect.ChooseDataSource;
import com.aek.common.core.aspect.DataSourceAspect;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@ConfigurationProperties("db")
public class DataSourceConfig {

	private String driverClassName;
	private int initialSize;
	private int maxActive;
	private int maxIdle;
	private int minIdle;
	private long maxWaitMillis;
	private long timeBetweenEvictionRunsMillis;
	private long minEvictableIdleTimeMillis;
	private int maxPoolPreparedStatementPerConnectionSize;

	private ConnectionInfo reader;
	private ConnectionInfo writer;

	@Value("${druid.filters}")
	private String filters;

	@Value("${druid.logType}")
	private String druidLogType;

	@Bean
	public StatFilter createStatFilter() {
		// 状态过滤器
		StatFilter statFilter = new StatFilter();
		long slowSqlMillis = 3000;
		statFilter.setSlowSqlMillis(slowSqlMillis);
		statFilter.setLogSlowSql(true);
		statFilter.setMergeSql(true);
		return statFilter;
	}

	@Bean
	public Log4j2Filter createLog4j2Filter() {
		Log4j2Filter log4j2Filter = new Log4j2Filter();
		log4j2Filter.setDataSourceLogEnabled(true);
		log4j2Filter.setStatementExecutableSqlLogEnable(true);
		return log4j2Filter;
	}

	@SuppressWarnings("deprecation")
	@Bean(name = "readDataSource", destroyMethod = "close", initMethod = "init")
	@Lazy
	public DruidDataSource createDruidDataSourceRead(StatFilter statFilter) throws SQLException {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(this.getReader().getUrl());
		druidDataSource.setUsername(this.getReader().getUsername());
		druidDataSource.setPassword(this.getReader().getPassword());
		druidDataSource.setInitialSize(initialSize);
		druidDataSource.setMaxActive(maxActive);
		druidDataSource.setMaxIdle(maxIdle);
		druidDataSource.setMinIdle(minIdle);
		druidDataSource.setMaxWait(maxWaitMillis);
		druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		druidDataSource.setDefaultReadOnly(true);
		List<Filter> proxyFilters = new ArrayList<Filter>();
		proxyFilters.add(statFilter);
		druidDataSource.setProxyFilters(proxyFilters);

		druidDataSource.setFilters(filters);
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(true);
		druidDataSource.setTestOnReturn(true);
		druidDataSource.setValidationQuery("SELECT 'x'");
		druidDataSource.setTimeBetweenLogStatsMillis(60000);
		return druidDataSource;
	}

	@SuppressWarnings("deprecation")
	@Bean(name = "writeDataSource", destroyMethod = "close", initMethod = "init")
	@Lazy
	public DruidDataSource createDruidDataSourceWrite(StatFilter statFilter) throws SQLException {
		DruidDataSource druidDataSource = new DruidDataSource();
		druidDataSource.setDriverClassName(driverClassName);
		druidDataSource.setUrl(this.getWriter().getUrl());
		druidDataSource.setUsername(this.getWriter().getUsername());
		druidDataSource.setPassword(this.getWriter().getPassword());
		druidDataSource.setInitialSize(initialSize);
		druidDataSource.setMaxActive(maxActive);
		druidDataSource.setMaxIdle(maxIdle);
		druidDataSource.setMinIdle(minIdle);
		druidDataSource.setMaxWait(maxWaitMillis);
		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		druidDataSource.setDefaultReadOnly(false);
		List<Filter> proxyFilters = new ArrayList<Filter>();
		proxyFilters.add(statFilter);
		druidDataSource.setProxyFilters(proxyFilters);

		druidDataSource.setFilters(filters);
		druidDataSource.setTestWhileIdle(true);
		druidDataSource.setTestOnBorrow(true);
		druidDataSource.setTestOnReturn(true);
		druidDataSource.setValidationQuery("SELECT 'x'");
		druidDataSource.setTimeBetweenLogStatsMillis(60000);
		return druidDataSource;
	}

	@Bean(name = "dataSource")
	@Lazy
	@Primary
	public ChooseDataSource createChooseDataSource() {
		ChooseDataSource chooseDataSource = new ChooseDataSource();
		return chooseDataSource;
	}

	@Bean
	public DataSourceAspect createDataSourceAspect() {
		return new DataSourceAspect();
	}

	@Bean
	public JdbcTemplate createJdbcTemplate(ChooseDataSource chooseDataSource) {
		return new JdbcTemplate(chooseDataSource);
	}

	@PostConstruct
	private void initSystemProperty() {
		System.setProperty("druid.logType", druidLogType);
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public int getMaxPoolPreparedStatementPerConnectionSize() {
		return maxPoolPreparedStatementPerConnectionSize;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public ConnectionInfo getReader() {
		return reader;
	}

	public void setReader(ConnectionInfo reader) {
		this.reader = reader;
	}

	public ConnectionInfo getWriter() {
		return writer;
	}

	public void setWriter(ConnectionInfo writer) {
		this.writer = writer;
	}

	public static class ConnectionInfo {
		private String url;
		private String username;
		private String password;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getDruidLogType() {
		return druidLogType;
	}

	public void setDruidLogType(String druidLogType) {
		this.druidLogType = druidLogType;
	}
}
