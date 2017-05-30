/*
 * File: ApplicationConfig.java
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.tnsilver.spring.aspect.Aspects;
import com.tnsilver.spring.converter.ContactSSNConverter;
import com.tnsilver.spring.converter.Converters;
import com.tnsilver.spring.converter.DateConverter;
import com.tnsilver.spring.data.repository.Repositories;
import com.tnsilver.spring.listener.Listeners;
import com.tnsilver.spring.service.impl.Services;

/**
 * Class ApplicationConfig.
 *
 * @author T.N.Silverman
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = { Repositories.class })
@Import({ CloudDataSourceConfig.class, DBCPDataSourceConfig.class, HikariDataSourceConfig.class, FlywayConfig.class, PersistenceConfig.class })
@ComponentScan(basePackageClasses = { Services.class, Listeners.class, Converters.class, Aspects.class })
@PropertySource({ "classpath:application.properties", "classpath:jdbc.properties" })
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "conversionService")
	@Autowired
	public ConversionService conversionService(DateConverter dateConverter, ContactSSNConverter contactSSNConverter) {
		ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
		conversionService.setConverters(Stream.of(dateConverter, contactSSNConverter).collect(Collectors.toSet()));
		conversionService.afterPropertiesSet();
		return conversionService.getObject();
	}
}
