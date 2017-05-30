/*
 * File: HelloRestController.java
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
package com.tnsilver.spring.controller.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
/**
 * Class HelloRestController.
 *
 * @author T.N.Silverman
 */
@RestController
@RequestMapping("/api")
public class HelloRestController {

	private static final String JsonType = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8";
	@Resource
	private ApplicationContext ctx;
	private static final Gson gson = new Gson();

	/**
	 * Spring (via ResponseEntity, RestController, and/or ResponseBody) will use the contents of the string as the raw
	 * response value, rather than treating the string as JSON value to be encoded. This is true even when the
	 * controller method uses produces = MediaType.APPLICATION_JSON_VALUE
	 *
	 * One way to handle this would be to embed your string inside an object or list, so that you're not passing a raw
	 * string to Spring. However, that changes the format of your output, and really there's no good reason not to be
	 * able to return a properly-encoded JSON string if that's what you want to do. If that's what you want, the best
	 * way to handle it is via a JSON formatter such as Json or Google Gson
	 */
	@RequestMapping(value = "/hello", produces = { JsonType }, method = GET)
	public ResponseEntity<String> greetStranger() {
		Locale locale = LocaleContextHolder.getLocale();
		String greeting = ctx.getMessage("hello.greeting", new Object[] {}, "Welcome", locale);
		String stranger = ctx.getMessage("hello.stranger", new Object[] {}, "stranger", locale);
		String result = greeting.concat(" ").concat(stranger);
		return new ResponseEntity<String>(gson.toJson(result), HttpStatus.OK);
	}

	@RequestMapping(value = "/hello/{firstName}", produces = { JsonType }, method = GET)
	public ResponseEntity<String> greetFirstName(@PathVariable(name = "firstName") String firstName) {
		Locale locale = LocaleContextHolder.getLocale();
		String greeting = ctx.getMessage("hello.greeting", new Object[] {}, "Welcome", locale);
		String result = greeting.concat(" ").concat(firstName);
		return new ResponseEntity<String>(gson.toJson(result), HttpStatus.OK);
	}

	@RequestMapping(value = "/hello/{firstName}/{lastName}", produces = { JsonType }, method = GET)
	public ResponseEntity<String> greetFullName(@PathVariable(name = "firstName") String firstName, @PathVariable(name = "lastName") String lastName) {
		Locale locale = LocaleContextHolder.getLocale();
		String greeting = ctx.getMessage("hello.greeting", new Object[] {}, "Welcome", locale);
		String result = greeting.concat(" ").concat(firstName).concat(" ").concat(lastName);
		return new ResponseEntity<String>(gson.toJson(result), HttpStatus.OK);
	}

	/**
	 * ResponseEntity will give you some added flexibility in defining arbitrary HTTP response headers.
	 *
	 * See the 4th constructor at
	 * {@link http://docs.spring.io/spring/docs/3.0.x/api/org/springframework/http/ResponseEntity.html}
	 *
	 * Some commonly-used ones are Status, Content-Type and Cache-Control. If you don't need that, using @ResponseBody
	 * will be a tiny bit more concise.
	 *
	 */
	@RequestMapping(value = "/greet", produces = { JsonType }, method = GET)
	@ResponseBody
	public String greet(/* HttpServletResponse response */) {
		Locale locale = LocaleContextHolder.getLocale();
		String greeting = ctx.getMessage("hello.greeting", new Object[] {}, "Welcome", locale);
		String stranger = ctx.getMessage("hello.stranger", new Object[] {}, "stranger", locale);
		String result = greeting.concat(" ").concat(stranger);
		// response.setStatus(HttpStatus.OK.value());
		return gson.toJson(result);
	}
}
