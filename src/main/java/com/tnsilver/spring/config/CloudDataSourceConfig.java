package com.tnsilver.spring.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.MysqlServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariDataSource;

/**
 * The Class CloudDataSourceConfig.
 */
@Configuration
@Profile({ "cloud" })
@PropertySource("classpath:hikari.properties")
public class CloudDataSourceConfig extends AbstractCloudConfig {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CloudDataSourceConfig.class);
	/** The service name. */
	@Value("${cleardb.service.name}")
	private String serviceName;
	// <spring.datasource.hikari.driver-class-name>com.mysql.jdbc.Driver</spring.datasource.hikari.driver-class-name>
	@Value("${spring.datasource.hikari.driver-class-name}")
	private String driverClassName;
	// <spring.datasource.hikari.maximum-pool-size>14</spring.datasource.hikari.maximum-pool-size>
	@Value("${spring.datasource.hikari.maximum-pool-size}")
	private Integer maximumPoolSize;
	// <spring.datasource.hikari.minimum-idle>7</spring.datasource.hikari.minimum-idle>
	@Value("${spring.datasource.hikari.minimum-idle}")
	private Integer minimumIdle;
	// <spring.datasource.hikari.idle-timeout>10000</spring.datasource.hikari.idle-timeout>
	@Value("${spring.datasource.hikari.idle-timeout}")
	private Long idleTimeout;
	// <spring.datasource.hikari.auto-commit>false</spring.datasource.hikari.auto-commit>
	@Value("${spring.datasource.hikari.auto-commit}")
	private Boolean autoCommit;
	// <spring.datasource.hikari.connection-timeout>30000</spring.datasource.hikari.connection-timeout>
	@Value("${spring.datasource.hikari.connection-timeout}")
	private Long connectionTimeout;
	// <spring.datasource.hikari.validation-timeout>10000</spring.datasource.hikari.validation-timeout>
	@Value("${spring.datasource.hikari.validation-timeout}")
	private Long validationTimeout;
	// <spring.datasource.hikari.connection-test-query>SELECT 1</spring.datasource.hikari.connection-test-query>
	@Value("${spring.datasource.hikari.connection-test-query}")
	private String connectionTestQuery;
	// <spring.datasource.hikari.connection-init-sql>SELECT 1</spring.datasource.hikari.connection-init-sql>
	@Value("${spring.datasource.hikari.connection-init-sql}")
	private String connectionInitSql;
	// <spring.datasource.hikari.connection-properties>UseUnicode=true&amp;characterEncoding=utf8&amp;tinyInt1isBit=false&amp;reconnect=true</spring.datasource.hikari.connection-properties>
	@Value("${spring.datasource.hikari.connection-properties}")
	private String connectionProperties;

	@Override
	@Bean
	public Cloud cloud() {
		return new CloudFactory().getCloud();
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		CloudFactory cloudFactory = new CloudFactory();
		Cloud cloud = cloudFactory.getCloud();
		ServiceInfo serviceInfo = cloud.getServiceInfo(serviceName);
		String serviceId = serviceInfo.getId();
		MysqlServiceInfo info = (MysqlServiceInfo) cloud.getServiceInfo(serviceId);
		String url = info.getJdbcUrl();
		String username = info.getUserName();
		String password = info.getPassword();
		HikariDataSource hikariDataSource = createHikariDataSource(url, username, password);
		logger.trace("\n\nCREATED HikariDataSource -> '{}'\n\n", hikariDataSource.getJdbcUrl());
		return hikariDataSource;
	}

	private HikariDataSource createHikariDataSource(String url, String username, String password) {
		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setDriverClassName(driverClassName);
		hikariDataSource.setUsername(username);
		hikariDataSource.setPassword(password);
		hikariDataSource.setMaximumPoolSize(maximumPoolSize);
		hikariDataSource.setMinimumIdle(minimumIdle);
		hikariDataSource.setIdleTimeout(idleTimeout);
		hikariDataSource.setAutoCommit(autoCommit);
		hikariDataSource.setConnectionTimeout(connectionTimeout);
		hikariDataSource.setConnectionTestQuery(connectionTestQuery);
		hikariDataSource.setValidationTimeout(validationTimeout);
		hikariDataSource.setConnectionInitSql(connectionInitSql);
		connectionProperties = connectionProperties.replaceAll(";", "&");
		url = url.concat("&" + connectionProperties);
		hikariDataSource.setJdbcUrl(url);
		logUrl(hikariDataSource);
		return hikariDataSource;
	}

	private void logUrl(DataSource dataSource) {
		try {
			Object url = dataSource.getClass().getMethod("getJdbcUrl", new Class<?>[0]).invoke(dataSource, new Object[0]);
			logger.trace("DATASOURCE URL {}", url);
		} catch (Exception ignore) {
		}
	}
}
