/*
 * File: DispatcherServletInitializer.java
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
package com.tnsilver.spring.config.initializer;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.tnsilver.spring.config.ApplicationConfig;
import com.tnsilver.spring.config.ThemeConfig;
import com.tnsilver.spring.config.ViewConfig;
import com.tnsilver.spring.config.WebConfig;

/**
 * Class DispatcherServletInitializer is a more elaborate way to configure the application to use a
 * WebApplicationContext with zero XML configurations.
 *
 * @author T.N.Silverman
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * returns an array of classes that consist the application configuration
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { ApplicationConfig.class };
	}

	/**
	 * returns an array of classes that consist the web / dispatcher servlet configurations
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class, ViewConfig.class, ThemeConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	/**
	 * More control over the creation of the dispatcher servlet
	 */
	@Override
	protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
		final FrameworkServlet dispatcherServlet = super.createDispatcherServlet(servletAppContext);
		((DispatcherServlet) dispatcherServlet).setThrowExceptionIfNoHandlerFound(true);
		return dispatcherServlet;
	}
}
