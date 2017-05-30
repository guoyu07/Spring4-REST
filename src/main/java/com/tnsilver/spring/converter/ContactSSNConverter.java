/*
 * File: ContactSSNConverter.java
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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tnsilver.spring.domain.ContactSSN;

/**
 * Class ContactSSNConverter.
 *
 * @author T.N.Silverman
 */
@Component("contactSSNConverter")
public class ContactSSNConverter implements Converter<String, ContactSSN> {

	private static final Logger logger = LoggerFactory.getLogger(ContactSSNConverter.class);
	// public static final String REGEX = "^\\d{3}-?\\d{2}-?\\d{4}$";

	@PostConstruct
	public void init() {
		logger.info("initialized!");
	}

	@Override
	public ContactSSN convert(String text) {
		logger.debug("converting '{}' to {}...\n", text, ContactSSN.class.getSimpleName());
		if (StringUtils.isEmpty(text)) {
		  logger.warn("returning empty ssn");
		  return new ContactSSN();
		}
		try {
			int area = Integer.parseInt(text.substring(0, 3));
			int group = Integer.parseInt(text.substring(4, 6));
			int serial = Integer.parseInt(text.substring(7, 11));
			return new ContactSSN(area, group, serial);
		} catch (Exception ignore) {
			logger.warn("returning malformed ssn");
			return new ContactSSN(text);
		}
	}
}
