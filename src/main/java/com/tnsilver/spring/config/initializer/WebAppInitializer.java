/*
 * File: WebAppInitializer.java
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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.tnsilver.spring.config.WebConfig;

/**
 * Class WebAppInitializer is another way of avoiding the older web.xml standard
 * In order to demonstrate this initializer we must:
 *
 * 1. uncomment the WebApplicationInitializer implementation
 *
 * 2. uncomment the @Import({ ApplicationConfig.class, ViewConfig.class, ThemeConfig.class }) line in WebConfig
 *
 * 3. Disable DispatcherServletInitializer class
 *
 * @author T.N.Silverman
 */
public class WebAppInitializer /*implements WebApplicationInitializer*/ {

	/*@Override*/
	public void onStartup(ServletContext container) throws ServletException {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(WebConfig.class);
		context.setServletContext(container);
		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(context));
		servlet.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}
}
