/*
 * File: RestConfig.java
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

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.tnsilver.spring.domain.Contact;
import com.tnsilver.spring.validator.ContactValidator;

/**
 * Class RestConfig - To customize the configuration, register a RepositoryRestConfigurer (or extend
 * RepositoryRestConfigurerAdapter) and implement or override the configure…-methods relevant to your use case.
 *
 * To install Spring Data REST alongside your existing Spring MVC application, you need to include the appropriate MVC
 * configuration. Spring Data REST configuration is defined in a class called RepositoryRestMvcConfiguration and that
 * class can just be imported into your applications configuration.
 *
 * Alternatively just register a custom implementation of RepositoryRestConfigurer as Spring bean and make sure it gets
 * picked up by component scanning:
 *
 * <pre>
 * &#64;Component
 * public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {
 *
 * 	&#64;Override
 * 	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
 * 		config.setBasePath("/api");
 * 	}
 * }
 * </pre>
 *
 * @author T.N.Silverman
 *
 * @see https://docs.spring.io/spring-data/rest/docs/current/reference/html/#getting-started.maven
 */
@Configuration
public class RestConfig extends RepositoryRestMvcConfiguration {

	@Resource
	private ContactValidator contactValidator;

	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return new RepositoryRestConfigurerAdapter() {

			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
				config.setBasePath("/api");
				config.exposeIdsFor(Contact.class);
				config.setReturnBodyForPutAndPost(true);
			}

			@Override
			public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
				validatingListener.addValidator("beforeCreate", contactValidator);
				validatingListener.addValidator("beforeSave", contactValidator);
			}
		};
	}
}