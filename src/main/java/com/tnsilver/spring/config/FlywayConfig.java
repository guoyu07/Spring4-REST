/*
 * File: FlywayConfig.java
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

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class FlywayConfig.
 *
 * @author T.N.Silverman
 */
@Configuration
public class FlywayConfig {

	private static final Logger logger = LoggerFactory.getLogger(FlywayConfig.class);
	@Value("${flyway.vendor}")
	private String flywayVendor;

	@Value("${flyway.locations}")
	private String flywayLocations;

	@Bean(name = "flyway", initMethod = "migrate")
	public Flyway flyway(@Autowired DataSource dataSource) {
		Flyway fly = new Flyway();
		fly.setDataSource(dataSource);
		logger.info("DATASOURCE INSTANCE OF {}", dataSource.getClass().getName());
		logUrl(dataSource);
		fly.setLocations(flywayLocations.replace("{vendor}", flywayVendor));
		return fly;
	}

	private void logUrl(DataSource dataSource) {
		try {
			Object url = dataSource.getClass().getMethod("getUrl", new Class<?>[0]).invoke(dataSource, new Object[0]);
			logger.trace("DATASOURCE URL {}", url);
		} catch (Exception ignore) {
		}
	}
}
