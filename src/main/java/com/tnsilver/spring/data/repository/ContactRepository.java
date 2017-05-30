/*
 * File: ContactRepository.java
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

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.tnsilver.spring.domain.Contact;

/**
 * Interface ContactRepository - Spring Data REST exposes a collection resource at api/contact. The path is derived from
 * the uncapitalized, pluralized, simple class name of the domain class being managed. It also exposes an item resource
 * for each of the items managed by the repository under the URI template api/contacts/{id}
 * <pre>
 * export HOST=localhost:8080/spring-rest
 * Then:
 *
 * query contacts profile:
 *
 * curl -v -G $HOST/api/profile/contacts | jq
 *
 * query paged contacts:
 *
 * curl -v -G $HOST/api/contacts | jq
 *
 * query search functions
 *
 * curl -X "GET" -v -H "Content-Type: application/json" -H "Cache-Control: no-cache" -k "$HOST/api/contacts/search" | jq
 *
 * execute a query by example:
 *
 * curl -X "GET" -v -H "Content-Type: application/json" -H "Cache-Control: no-cache" -k "$HOST/api/contacts/search/byExample?ssn=123&firstName=&lastName=&dateOfBirth=06/21/1978&married=true&children=&page=0&size=10&sort=id,asc" | jq
 *
 * save a new contact
 *
 * curl -X "POST" -v -H "Content-Type: application/json" -H "Accept: application/json" -H "Cache-Control: no-cache" -d '{"contactSSN": {"ssn":"123-45-6789"},"firstName":"Lucky","lastName":"Luke","dateOfBirth":"1954-08-21","married":true,"children":2}' -k "$HOST/api/contacts"
 *
 * update a contact:
 *
 * curl -X "POST" -v -H "Content-Type: application/json" -H "Accept: application/json" -H "Cache-Control: no-cache" -d '{"id":37,"contactSSN": {"ssn":"123-45-6789"},"firstName":"Lucky","lastName":"Luchiano","dateOfBirth":"1954-08-21","married":false,"children":0}' -k "$HOST/api/contacts"
 *
 * delete a contact
 *
 * curl -X "DELETE" -v -H "Content-Type: application/json" -H "Cache-Control: no-cache" -k "$HOST/api/contacts/37"
 *
 *
 * @author T.N.Silverman
 */

//@CrossOrigin(maxAge = 3600)
@RepositoryRestResource(path = "contacts", collectionResourceRel = "contacts")
public interface ContactRepository extends CrudRepository<Contact, Long> {

	public Page<Contact> findAll(Pageable pageable);

	@Override
	public List<Contact> findAll();

	@SuppressWarnings("unchecked")
	@Override
	public Contact save(Contact entity);

	/**
	 * This is only here for AOP aspect auditing purpose
	 */
	@Override
	public void delete(Contact entity);


	@RestResource(exported = false)
	@Query("SELECT c from Contact c WHERE "
	        + "CASE WHEN c.contactSSN is not null THEN c.contactSSN.ssn ELSE c.contactSSN END "
			+ "LIKE CONCAT(COALESCE(:#{#example.contactSSN?.ssn},''),'%') "
			+ "AND c.firstName LIKE CONCAT(COALESCE(:#{#example.firstName},''),'%') "
			+ "AND c.lastName LIKE CONCAT(COALESCE(:#{#example.lastName},''),'%') "
			+ "AND c.dateOfBirth = COALESCE(:#{#example.dateOfBirth},c.dateOfBirth) "
			+ "AND COALESCE(c.married,FALSE) = COALESCE(:#{#example.married != null ? (!#example.married ? NULL : #example.married) : NULL},COALESCE(c.married,FALSE)) "
			+ "AND COALESCE(c.children,0) = COALESCE(:#{#example.children},COALESCE(c.children,0)) ")
	public Page<Contact> getFilteredContacts(@Param(value = "example") Contact example, Pageable pageable);

	@RestResource(path="byExample",rel="byExample")
	@Query("SELECT c from Contact c WHERE "
			+ "CASE WHEN c.contactSSN is not null THEN c.contactSSN.ssn ELSE c.contactSSN END "
	        + "LIKE CONCAT(COALESCE(:ssn,''),'%') "
			+ "AND c.firstName LIKE CONCAT(COALESCE(:firstName,''),'%') "
			+ "AND c.lastName LIKE CONCAT(COALESCE(:lastName,''),'%') "
			+ "AND c.dateOfBirth = COALESCE(:dateOfBirth,c.dateOfBirth) "
			+ "AND (COALESCE(c.married,FALSE) = COALESCE(:married,c.married) OR COALESCE(c.married,FALSE) = TRUE)"
			+ "AND COALESCE(c.children,0) = COALESCE(:children,COALESCE(c.children,0)) ")
	public Page<Contact> getByParams(@Param(value = "ssn") String ssn,
			                         @Param("firstName") String firstName,
			                         @Param("lastName") String lastName,
			                         @Param("dateOfBirth") Date dateOfBirth,
			                         @Param("married") Boolean married,
			                         @Param("children") Integer children,
			                         Pageable pageable);

	@RestResource(path = "byName", rel = "byName")
	public List<Contact> findByFirstNameStartsWith(@Param("firstName") String firstName);

	@RestResource(path = "byNames", rel = "byNames")
	public List<Contact> findDistinctContactsByFirstNameStartsWithAndLastNameStartsWith(@Param("firstName") String firstName, @Param("lastName") String lastName);

	@RestResource(path = "byMatch", rel = "byMatch")
	@Query("SELECT c FROM Contact c WHERE c.firstName LIKE Concat(Coalesce(:firstName,''),'%') AND c.lastName LIKE Concat(Coalesce(:lastName,''),'%')")
	public List<Contact> getMatchingContacts(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
