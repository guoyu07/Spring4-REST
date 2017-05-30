/*
 * File: RestAPIMockTest.java
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.tnsilver.spring.config.ApplicationConfig;
import com.tnsilver.spring.config.WebConfig;
import com.tnsilver.spring.domain.Contact;
import com.tnsilver.spring.domain.ContactSSN;

/**
 * Class RestAPIMockTest - Spring MVC provides great support for unit testing HTTP endpoints. It provides a very nice
 * middle ground between unit-testing and integration-testing in that it lets you stand up the entire Spring MVC
 * DispatcherServlet-based machinery - including validators, HttpMessageConverter s, and more - and then run tests
 * against them without actually starting up a real HTTP service: the best of both worlds! It’s an integration-test in
 * that the logic you care about is actually being exercised, but it’s a unit-test in that you’re not actually waiting
 * for a web server to initialize and start servicing requests.
 *
 * @author T.N.Silverman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class, WebConfig.class })
// The @WebAppConfiguration annotation tells JUnit that this is a unit test for Spring MVC web components and should
// thus run under a WebApplicationContext variety, not a standard ApplicationContext implementation.
@WebAppConfiguration
@ActiveProfiles({ "test" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class RestAPIMockTest {

	private static final Logger logger = LoggerFactory.getLogger(RestAPIMockTest.class);
	private static final String restApiEndPoint = "/api/contacts";
	private String contentType = "application/hal+json;charset=UTF-8";

	@Rule
	public TestName testName = new TestName();

	@Resource
	private Environment env;

	@Resource
	private WebApplicationContext webApplicationContext;

	private HttpMessageConverter<Contact> mappingJackson2HttpMessageConverter;

	private MockMvc mockMvc;


	@SuppressWarnings("unchecked")
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		// initialize the mappingJackson2HttpMessageConverter
		this.mappingJackson2HttpMessageConverter = (HttpMessageConverter<Contact>) Arrays.asList(converters).stream().filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@BeforeClass
	public static void setProfile() {
		String profile = ContactRepositoryTest.class.getAnnotation(ActiveProfiles.class).value()[0];
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
	}

	@Before
	public void setUp() throws Exception {
		// build the mock
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		String profile = Stream.of(env.getActiveProfiles()).findAny().orElse("unknown");
		logger.debug("Entering {}() with profile '{}'\n", testName.getMethodName(), profile);
	}

	@Test
	public void testConfig() {
		// sanity test
	}

	/**
	 * perform a get to the rest api end point and expect a response containing
	 * 20 (default page size) contacts, status 200 and content type
	 * application/hal+json;charset=UTF-8
	 */
	@Test
	public void readContacts() throws Exception {
		 mockMvc.perform(get(restApiEndPoint))
				.andDo(print())
				.andExpect(jsonPath("_embedded.contacts", hasSize(20)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType));
	}

	/**
	 * perform a get to the rest api end point and expect a response containing
	 * 1 contact (Homer Simpson), status 200 and content type
	 * application/hal+json;charset=UTF-8
	 */
	@Test
	public void readSingleContact() throws Exception {
		/*MvcResult result =*/
		 mockMvc.perform(get(restApiEndPoint + "/1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(new Integer(1))))
				.andExpect(jsonPath("$.firstName", is("Homer")))
				.andExpect(jsonPath("$.lastName", is("Simpson")))
				.andExpect(jsonPath("$.dateOfBirth", equalTo("1978-06-21")))
				.andExpect(jsonPath("$.married", is(true)))
				.andExpect(jsonPath("$.children", is(3)))
				.andExpect(content().contentType(contentType))
				/*.andReturn()*/;
		/*String content = result.getResponse().getContentAsString();
		logger.debug("content: {}",content);*/
	}

	/**
	 * perform a post to the rest api end point supplying a contact json and
	 * expect a status 201 and content type application/hal+json;charset=UTF-8
	 * with the created contact in the response body
	 */
	@Test
	public void saveContact() throws Exception {
		 Contact contact = new Contact(new ContactSSN("123-45-6789"), "Test", "Contact", java.sql.Date.valueOf(LocalDate.parse("1980-02-06")), Boolean.TRUE, 3);
		 String json = json(contact);
		 logger.debug("saving contact ",json);
		 mockMvc.perform(post(restApiEndPoint).contentType(contentType).content(json))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.firstName", is("Test")))
				.andExpect(jsonPath("$.lastName", is("Contact")));
	}

	/**
	 * perform a patch to the rest api end point supplying a contact json and
	 * expect a status 200 and content type application/hal+json;charset=UTF-8
	 * with the updated contact in the response body
	 */
	@Test
	public void patchContact() throws Exception {
		 String homer = "{\"id\" : 1, "
		 		       + "\"contactSSN\" : {\"ssn\" : \"123-45-6789\"},"
		 		       + "\"firstName\" : \"Homer\","
		 		       + "\"lastName\" : \"Simpson\","
		 		       + "\"dateOfBirth\" : \"1978-06-21\","
		 		       + "\"married\" : false,"
		 		       + "\"children\" : 0}";
		 mockMvc.perform(patch(restApiEndPoint + "/1").contentType(contentType).content(homer))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.married", is(false)))
				.andExpect(jsonPath("$.children", is(0)))
				.andExpect(content().contentType(contentType));
	}

	/**
	 * perform a get to the end point with a none existent resource
	 * and expect a status 404 - Not Found
	 */
	@Test
    public void readNotFound() throws Exception {
		 mockMvc.perform(get(restApiEndPoint + "/0"))
			.andDo(print())
			.andExpect(status().isNotFound());
    }

	/**
	 * Utility to convert Contact objects to json string
	 * @param contact the contact object
	 * @return a json representation of the contact object
	 * @throws IOException if anything goes kaput
	 */
	private String json(Contact contact) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(contact, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
