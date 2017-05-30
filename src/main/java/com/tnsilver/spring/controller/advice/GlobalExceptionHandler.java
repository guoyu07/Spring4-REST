/*
 * File: GlobalExceptionHandler.java
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
package com.tnsilver.spring.controller.advice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.NestedServletException;

import com.google.gson.Gson;
import com.tnsilver.spring.domain.Error;
import com.tnsilver.spring.validator.ContactValidator;

/**
 * Class GlobalExceptionHandler is a global error handler
 *
 * @author T.N.Silverman
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@Value("${date.time.pattern}")
	private String pattern;
	private static final Gson gson = new Gson();

	/**
	 *  MVC Global Error Handling
	 */

	@ResponseStatus(HttpStatus.NOT_FOUND) // 404
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNotFound(NoHandlerFoundException ex) {
		logger.debug("Handling exception  {} -> {}", ex.getClass().getName(), ex.getMessage());
		return "forward:/static/pages/404.html";
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
	@ExceptionHandler(value = { NestedServletException.class, IllegalArgumentException.class, ServletException.class, IOException.class })
	public ModelAndView handleServerError(HttpServletRequest request, Throwable ex) {
		logger.debug("Handling exception  {} -> {}", ex.getClass().getName(), ex.getMessage());
		logger.error("Request: {} raised {}", request.getRequestURL(), ex.getMessage());
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", ex);
		mav.addObject("url", request.getRequestURL());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		formatter.format(LocalDateTime.now(ZoneId.systemDefault()));
		mav.addObject("timestamp", formatter.format(LocalDateTime.now(ZoneId.systemDefault())));
		mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR);
		mav.setViewName("html/support");
		return mav;
	}

	/**
	 *  RESTful Global Error Handling
	 */

	/**
	 * This method is an alternative to using the {@link ContactValidator} and relying on JSR-303 validation annotations.
	 * It does not hurt to use it conjunction with a REST validators.
	 */
	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleJSR303ConstraintViolations(ConstraintViolationException cvex, WebRequest request) {
		Set<ConstraintViolation<?>> constraintViolations = cvex.getConstraintViolations();
		List<Error> errors = new ArrayList<>();
		constraintViolations.forEach(cv -> {
			String error = cv.getMessage();
			logger.warn("{} -> error: {}","handleJSR303ConstraintViolations", error);
			errors.add(new Error(cv.getRootBeanClass().getSimpleName(), cv.getPropertyPath().toString(), cv.getInvalidValue().toString(), error));
		});
		Map<String, List<Error>> errMap = new HashMap<>();
		errMap.put("errors", errors);
		String responseBody = gson.toJson(errMap); // convert to json
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("Message-Type", "Error");
		return new ResponseEntity<Object>(responseBody, headers, HttpStatus.BAD_REQUEST);
	}

	/**
	 * This method handles a HttpMessageNotReadableException that is raised when a malformed json is posted to the server
	 *
	 */
	@ExceptionHandler(value = { HttpMessageNotReadableException.class })
	protected ResponseEntity<Object> handleUnreadableMessages(HttpMessageNotReadableException ex, WebRequest request) {
		List<Error> errors = new ArrayList<>();
		try (Scanner scanner = new Scanner(ex.getMessage())) {
			scanner.useDelimiter("\n"); // JsonParserEception uses "\n" as a main message separator
			String error = scanner.hasNext() ? scanner.next() : ex.getMessage();
			logger.warn("{} -> error: {}","handleUnreadableMessages", error);
			errors.add(new Error("Error", "", "", error));
		}
		Map<String, List<Error>> errMap = new HashMap<>();
		errMap.put("errors", errors);
		String responseBody = gson.toJson(errMap); // convert to json
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("Message-Type", "Error");
		return new ResponseEntity<Object>(responseBody, headers, HttpStatus.BAD_REQUEST);
	}


}