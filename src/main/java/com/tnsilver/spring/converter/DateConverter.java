/*
 * File: DateConverter.java
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
package com.tnsilver.spring.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Class DateConverter.
 *
 * @author T.N.Silverman
 */
@Component("dateConverter")
public class DateConverter implements Converter<String, Date> {

	private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);
	public static final String LOCAL_PATTERN_KEY = "date.pattern";
	public static final String GLOBAL_PATTERNS_KEY = "date.patterns";

	@Value("#{'${" + GLOBAL_PATTERNS_KEY + "}'.split(',')}")
	private List<String> patterns;
	@Resource
	private ApplicationContext applicationContext;

	public DateConverter() {
		super();
	}

	@PostConstruct
	public void init() {
		logger.debug("date patterns are {}", patterns);
	}

	@Override
	public Date convert(String text) {
		Date result = parseDateByLocalePattern(text);
		if (result == null) {
			result = parseDateByAllPatterns(text);
		}
		return result;
	}

	private Date parseDateByLocalePattern(String text) {
		Locale locale = LocaleContextHolder.getLocale();
		String pattern = applicationContext.getMessage(LOCAL_PATTERN_KEY, new Object[] {}, patterns.get(0), locale);
		Date result = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
			LocalDate parsedDate = LocalDate.parse(text, formatter);
			result = java.sql.Date.valueOf(parsedDate);
			logger.debug("parsed '{}' with locale '{}' pattern '{}'...", text, locale, pattern);
		} catch (DateTimeParseException | NullPointerException ex) {
			logger.warn("cannot parse '{}' with locale '{}' pattern '{}' b/c {}", text, locale, pattern, ex.getMessage());
		}
		return result;
	}

	private Date parseDateByAllPatterns(String text) {
		return patterns.stream().map(pattern -> {
			Date result = null;
			try {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
				LocalDate parsedDate = LocalDate.parse(text, formatter);
				// result = Date.from(parsedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				result = java.sql.Date.valueOf(parsedDate);
				logger.debug("parsed '{}' with global pattern '{}'", text, pattern);
			} catch (DateTimeParseException | NullPointerException ex) {
				logger.warn("cannot parse '{}' b/c {}", text == null ? "null" : text, ex.getMessage());
			}
			return result;
		}).filter(date -> date != null).findFirst().orElse(null);
	}
}
