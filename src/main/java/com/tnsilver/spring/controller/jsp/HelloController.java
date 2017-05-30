/*
 * File: HelloController.java
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
package com.tnsilver.spring.controller.jsp;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class HelloController.
 *
 * @author T.N.Silverman
 */
@Controller("jspHelloController")
@RequestMapping("/jsp")
public class HelloController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	public static final String HELLO_VIEW_NAME = "jsp/hello";
	public static final String HELLO_REST_API_ENDPOINT = "/api/hello";
	@Resource
	private HttpServletRequest request;

	@GetMapping(path = { "/hello" })
	public String showView() {
		logger.debug("showing {}", HELLO_VIEW_NAME);
		return HELLO_VIEW_NAME;
	}

	@GetMapping(path = { "/hello/{firstName}" })
	public ModelAndView showView(@PathVariable(name = "firstName") String firstName) {
		logger.debug("showing {}", HELLO_VIEW_NAME + "/" + firstName);
		ModelMap map = new ModelMap();
		map.addAttribute("firstName", firstName);
		return new ModelAndView(HELLO_VIEW_NAME, map);
	}

	@GetMapping(path = { "/hello/{firstName}/{lastName}" })
	public ModelAndView showView(@PathVariable(name = "firstName") String firstName, @PathVariable(name = "lastName") String lastName) {
		logger.debug("showing {}", HELLO_VIEW_NAME + "/" + firstName + "/" + lastName);
		ModelMap map = new ModelMap();
		map.addAttribute("firstName", firstName);
		map.addAttribute("lastName", lastName);
		return new ModelAndView(HELLO_VIEW_NAME, map);
	}
}
