/*
 * File: ContactValidator.java
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
package com.tnsilver.spring.validator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tnsilver.spring.domain.BaseEntity;
import com.tnsilver.spring.domain.Contact;

/**
 * Class ContactValidator.
 *
 * @author T.N.Silverman
 */
@Component("beforeCreateContactValidator")
public class ContactValidator implements Validator {

	private static final Logger logger = LoggerFactory.getLogger(ContactValidator.class);
	private static final Object[] noArgs = new Object[] {};

	@Override
	public boolean supports(Class<?> clazz) {
		return BaseEntity.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		if (object == null) {
			errors.reject("invalid.contact", noArgs, "contact is null");
			logger.warn("rejected null contact");
		}
		if (object instanceof Contact) {
			validateContact(object, errors);
		}
	}

	private void validateContact(Object object, Errors errors) {
		Contact contact = (Contact) object;
		logger.debug("validating {}", contact);
		if (contact.getContactSSN() == null || !contact.getContactSSN().isValid()) {
			errors.rejectValue("contactSSN", "invalid.ssn", noArgs, "invalid ssn");
			logger.warn("rejected invalid ssn");
		}
		if (StringUtils.isEmpty(contact.getFirstName()) || contact.getFirstName().length() < 2 || contact.getFirstName().length() > 25) {
			errors.rejectValue("firstName", "invalid.name", noArgs, "invalid name");
			logger.warn("rejected invalid first name");
		}
		if (StringUtils.isEmpty(contact.getLastName()) || contact.getLastName().length() < 2 || contact.getLastName().length() > 25) {
			errors.rejectValue("lastName", "invalid.name", noArgs, "invalid name");
			logger.warn("rejected invalid last name");
		}
		if (contact.getDateOfBirth() == null) {
			errors.rejectValue("dateOfBirth", "invalid.dob", noArgs, "invalid birth date");
			logger.warn("rejected invalid birth date");
		}
		if (contact.getMarried() == null) {
			errors.rejectValue("married", "invalid.married", noArgs, "invalid maritial status");
			logger.warn("rejected invalid maritial status");
		}
		if (contact.getChildren() == null || contact.getChildren() < 0) {
			errors.rejectValue("children", "invalid.children", noArgs, "invalid number of children");
			logger.warn("rejected invalid number of children");
		}
	}
}
