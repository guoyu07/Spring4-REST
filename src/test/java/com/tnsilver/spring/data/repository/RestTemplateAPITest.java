/*
 * File: RestTemplateAPITest.java
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
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
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnsilver.spring.config.ApplicationConfig;
import com.tnsilver.spring.config.TestConfig;
import com.tnsilver.spring.config.WebConfig;
import com.tnsilver.spring.domain.Contact;
import com.tnsilver.spring.domain.ContactSSN;

/**
 * Class RestTemplateAPITest demonstrates the broad range of operations where the Spring REST Client – RestTemplate –
 * can be used
 *
 * SERVER MUST BE RUNNING FOR THIS TEST!!!
 *
 * @author patricia
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class, WebConfig.class, TestConfig.class })
@WebAppConfiguration
@ActiveProfiles({ "test" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Transactional
public class RestTemplateAPITest {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateAPITest.class);
	private static final String restApiEndPoint = "http://localhost:8080/spring-rest/api/contacts";
	@Rule
	public TestName testName = new TestName();
	@Resource
	private Environment env;
	// template obtained from the TestConfig class
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private HttpHeaders headers;
	private Contact homer;
	private Contact gomer;

	@BeforeClass
	public static void setProfile() {
		String profile = ContactRepositoryTest.class.getAnnotation(ActiveProfiles.class).value()[0];
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
	}

	@Before
	public void setUp() throws Exception {
		homer = new Contact(new ContactSSN("123-45-6789"), "Homer", "Simpson", java.sql.Date.valueOf(LocalDate.parse("1978-06-21")), Boolean.TRUE, 3);
		gomer = new Contact(new ContactSSN("123-45-6789"), "Gomer", "Pyle", java.sql.Date.valueOf(LocalDate.parse("1978-06-21")), Boolean.FALSE, 0);
		String profile = Stream.of(env.getActiveProfiles()).findAny().orElse("unknown");
		logger.debug("Entering {}() with profile '{}'\n", testName.getMethodName(), profile);
	}

	/**
	 * RestTemplate GET requests: a with a quick example using the getForEntity() API
	 */
	@Test
	public void simpleRead() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity(restApiEndPoint, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
	}

	/**
	 * A workaround the HAL standard '_embedded' node in json response. We use {@link Resources} to wrap our entities
	 * and use the RestTemplate getForObject API. Contact entities are now maps inside the Resources .getContent().
	 */
	@Test
	public void readContacts() throws Exception {
		@SuppressWarnings("unchecked")
		Resources<Contact> pagedResponse = restTemplate.getForObject(restApiEndPoint, Resources.class);
		assertThat(pagedResponse, notNullValue());
		assertThat(pagedResponse.getContent(), notNullValue());
		assertThat(pagedResponse.getContent().size(), is(20));
	}

	/**
	 * using an OPTIONS request and exploring the allowed operations on a specific URI using the optionsForAllow API
	 */
	@Test
	public void getApiOptions() throws Exception {
		Set<HttpMethod> optionsForAllow = restTemplate.optionsForAllow(restApiEndPoint);
		HttpMethod[] supportedMethods = { HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS, HttpMethod.HEAD };
		assertTrue(optionsForAllow.containsAll(Arrays.asList(supportedMethods)));
	}

	/**
	 * using an OPTIONS request and exploring the allowed operations on a specific URI using the optionsForAllow API
	 */
	@Test
	public void getContactOptions() throws Exception {
		Set<HttpMethod> optionsForAllow = restTemplate.optionsForAllow(restApiEndPoint + "/1");
		HttpMethod[] supportedMethods = { HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS, HttpMethod.HEAD, HttpMethod.GET, HttpMethod.PATCH };
		assertTrue(optionsForAllow.containsAll(Arrays.asList(supportedMethods)));
	}

	/**
	 * Doing a HEAD request by using the headForHeaders() API
	 */
	@Test
	public void getHead() throws Exception {
		HttpHeaders httpHeaders = restTemplate.headForHeaders(restApiEndPoint + "/1");
		httpHeaders.entrySet().forEach(e -> logger.debug("{} -> {}", e.getKey(), e.getValue()));
		assertTrue(httpHeaders.get("Link") != null);
	}

	/**
	 * Using postForLocation API -> instead of returning the full Resource, just returns the Location of that newly
	 * created Resource:
	 */
	@Test(expected = HttpClientErrorException.class)
	public void postForLocation() throws Exception {
		homer.setId(1L);
		HttpEntity<Contact> request = new HttpEntity<>(homer);
		URI location = restTemplate.postForLocation(restApiEndPoint + "/1", request);
		logger.debug("homer's location URI: {}", location.toString());
		assertThat(location, notNullValue());
	}

	/**
	 * map the response directly to a Resource DTO with the getForObject API
	 */
	@Test
	public void readSingleContact() throws Exception {
		Contact contact = restTemplate.getForObject(restApiEndPoint + "/1", Contact.class);
		assertThat(contact.getFirstName(), notNullValue());
		assertThat(contact.getId(), is(1L));
	}

	/**
	 * In order to create a new Resource in the API – we can make use of the postForLocation(), postForObject() or
	 * postForEntity() APIs.
	 *
	 * The postForLocation returns the URI of the newly created Resource while the second returns the Resource itself.
	 */
	@Test
	public void saveContact() throws Exception {
		Contact test = new Contact(new ContactSSN("123-45-6789"), "Test", "Contact", java.sql.Date.valueOf(LocalDate.parse("1980-02-06")), Boolean.TRUE, 3);
		HttpEntity<Contact> request = new HttpEntity<>(test);
		Contact contact = restTemplate.postForObject(restApiEndPoint, request, Contact.class);
		assertThat(contact, notNullValue());
		assertThat(contact.getId(), notNullValue());
		assertThat(contact.getContactSSN().getSsn(), is("123-45-6789"));
		assertThat(contact.getFirstName(), is("Test"));
		assertThat(contact.getLastName(), is("Contact"));
		assertThat(contact.getDateOfBirth(), notNullValue());
		assertThat(contact.getMarried(), is(Boolean.TRUE));
		assertThat(contact.getChildren(), is(3));
	}

	/**
	 * doing a POST with the more generic exchange API and using patchForObject to update an entity
	 */
	@Test
	public void patchContact() throws Exception {
		// request
		HttpEntity<Contact> entityToCreate = new HttpEntity<>(homer, headers);
		// Create Resource
		ResponseEntity<Contact> createResponse = restTemplate.exchange(restApiEndPoint, HttpMethod.POST, entityToCreate, Contact.class);
		assertThat(createResponse.getStatusCode(), is(HttpStatus.CREATED));
		// contact to update (patch)
		gomer.setId(createResponse.getBody().getId());
		String resourceUrl = restApiEndPoint + "/" + createResponse.getBody().getId();
		HttpEntity<Contact> entityToUpdate = new HttpEntity<>(gomer, headers);
		// Update Resource
		restTemplate.patchForObject(resourceUrl, entityToUpdate, Void.class);
		// Check that Resource was updated
		ResponseEntity<Contact> updateResponse = restTemplate.exchange(resourceUrl, HttpMethod.GET, new HttpEntity<>(headers), Contact.class);
		Contact actual = updateResponse.getBody();
		assertThat(actual.getId(), is(gomer.getId()));
		assertThat(actual.getContactSSN(), is(gomer.getContactSSN()));
		assertThat(actual.getFirstName(), is(gomer.getFirstName()));
		assertThat(actual.getLastName(), is(gomer.getLastName()));
		assertThat(actual.getDateOfBirth(), notNullValue());
		assertThat(actual.getMarried(), is(gomer.getMarried()));
		assertThat(actual.getChildren(), is(gomer.getChildren()));
	}

	/**
	 * The template throws an exception if the resource is not found
	 */
	@Test(expected = HttpClientErrorException.class)
	public void readNotFound() throws Exception {
		restTemplate.getForEntity(restApiEndPoint + "/0", Contact.class);
	}

	/**
	 * remove an existing Resource with the delete() API:
	 */
	@Test(expected = HttpClientErrorException.class)
	public void delete() throws Exception {
		Contact test = new Contact(new ContactSSN("123-45-6789"), "Test", "Contact", java.sql.Date.valueOf(LocalDate.parse("1980-02-06")), Boolean.TRUE, 3);
		HttpEntity<Contact> request = new HttpEntity<>(test);
		Contact created = restTemplate.postForObject(restApiEndPoint, request, Contact.class);
		String entityUrl = restApiEndPoint + "/" + created.getId();
		restTemplate.delete(entityUrl);
		restTemplate.getForEntity(restApiEndPoint + "/" + created.getId(), Contact.class);
	}

	/**
	 * ADVANCED: patching with with a callback
	 */
	@Test
	public void patchWithCallBack() throws Exception {
		gomer.setId(1L);
		String resourceUrl = restApiEndPoint + "/1";
		restTemplate.execute(resourceUrl, HttpMethod.PATCH, requestCallback(gomer), clientHttpResponse -> null);
	}

	/**
	 * A {@link RequestCallback} is a SAM with the method:
	 *
	 * void doWithRequest(ClientHttpRequest request) throws IOException;
	 *
	 * Allows to manipulate the request headers, and write to the request body.
	 *
	 * In this case it will write the response body to a file named 'contacts' in the project directory
	 */
	private RequestCallback requestCallback(final Contact updatedInstance) {
		return clientHttpRequest -> {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(clientHttpRequest.getBody(), updatedInstance);
			clientHttpRequest.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		};
	}
}
