/*
 * File: HikariDataSourceConfig.java
 *
 * Copyright (c) 2017 T.N.Silverman - all rights reserved
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tnsilver.spring.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariDataSource;

/**
 * Class HikariDataSourceConfig.
 *
 * @author T.N.Silverman
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
@Profile({ "prod","!cloud" })
public class HikariDataSourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(HikariDataSourceConfig.class);
	@Value("${hikari.driver-class-name}")
	private String driverClassName;
	@Value("${hikari.username}")
	private String username;
	@Value("${hikari.password}")
	private String password;
	@Value("${hikari.jdbc-url}")
	private String jdbcUrl;
	@Value("${hikari.maximum-pool-size}")
	private Integer maximumPoolSize;
	@Value("${hikari.minimum-idle}")
	private Integer minimumIdle;
	@Value("${hikari.idle-timeout}")
	private Long idleTimeout;
	@Value("${hikari.auto-commit}")
	private Boolean autoCommit;
	@Value("${hikari.connection-timeout}")
	private Long connectionTimeout;
	@Value("${hikari.validation-timeout}")
	private Long validationTimeout;
	@Value("${hikari.connection-test-query}")
	private String connectionTestQuery;
	@Value("${hikari.connection-init-sql}")
	private String connectionInitSql;
	@Value("${hikari.connection-properties}")
	private String connectionProperties;

	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaximumPoolSize(maximumPoolSize);
		dataSource.setMinimumIdle(minimumIdle);
		dataSource.setIdleTimeout(idleTimeout);
		dataSource.setAutoCommit(autoCommit);
		dataSource.setConnectionTimeout(connectionTimeout);
		dataSource.setConnectionTestQuery(connectionTestQuery);
		dataSource.setValidationTimeout(validationTimeout);
		dataSource.setConnectionInitSql(connectionInitSql);
		dataSource.setJdbcUrl(jdbcUrl);
		logger.debug("CREATED HikariDataSource -> {}\n", dataSource.getJdbcUrl());
		return dataSource;
	}
}
