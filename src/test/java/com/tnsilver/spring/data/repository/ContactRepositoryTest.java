/*
 * File: ContactRepositoryTest.java
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
package com.tnsilver.spring.data.repository;

import static org.junit.Assert.assertFalse;

import java.time.LocalDate;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.tnsilver.spring.config.ApplicationConfig;
import com.tnsilver.spring.domain.Contact;
import com.tnsilver.spring.domain.ContactSSN;

/**
 * Class ContactRepositoryTest.
 *
 * @author T.N.Silverman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@ActiveProfiles({ "dev" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class ContactRepositoryTest {

	private static final Logger logger = LoggerFactory.getLogger(ContactRepositoryTest.class);
	@Rule
	public TestName testName = new TestName();
	@Resource
	private Environment env;
	@Resource(name = "contactRepository")
	private ContactRepository contactRepository;

	@BeforeClass
	public static void setProfile() {
		String profile = ContactRepositoryTest.class.getAnnotation(ActiveProfiles.class).value()[0];
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
	}

	@Before
	public void setUp() throws Exception {
		String profile = Stream.of(env.getActiveProfiles()).findAny().orElse("unknown");
		logger.debug("Entering {}() with profile '{}'\n", testName.getMethodName(), profile);
	}

	@Test
	public void testGetFilteredContactsByPartialSSN() {
		Contact filter = new Contact(new ContactSSN("123-45"), null, null, null, null, null);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetFilteredContactsByPartialFirstName() {
		Contact filter = new Contact(new ContactSSN("123-45"), "Home", null, null, null, null);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetFilteredContactsByPartialLastName() {
		Contact filter = new Contact(new ContactSSN("123-45"), "Home", "Simp", null, null, null);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetFilteredContactsByDob() {
		Contact filter = new Contact(new ContactSSN("123-45"), "Home", "Simp", java.sql.Date.valueOf(LocalDate.parse("1978-06-21")), null, null);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetFilteredContactsByMarried() {
		Contact filter = new Contact(new ContactSSN("123-45"), "Home", "Simp", java.sql.Date.valueOf(LocalDate.parse("1978-06-21")), true, null);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetFilteredContactsByChildren() {
		Contact filter = new Contact(new ContactSSN("123-45"), "Home", "Simp", java.sql.Date.valueOf(LocalDate.parse("1978-06-21")), true, 3);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetFilteredContactsByNothing() {
		Contact filter = new Contact();
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getFilteredContacts(filter, pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

	@Test
	public void testGetByParams() {
		Contact filter = new Contact(new ContactSSN("123"), null, null, null, null, null);
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.ASC, "id"));
		Page<Contact> actual = contactRepository.getByParams(filter.getContactSSN().getSsn(), filter.getFirstName(), filter.getLastName(), filter.getDateOfBirth(), filter.getMarried(), filter.getChildren(), pageable);
		assertFalse(actual.getContent().isEmpty());
		actual.getContent().forEach(c -> logger.debug("{}\n", c));
	}

}
