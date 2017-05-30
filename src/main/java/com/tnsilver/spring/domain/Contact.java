/*
 * File: Contact.java
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
package com.tnsilver.spring.domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tnsilver.spring.listener.EntityListener;

/**
 * Class Contact.
 *
 * @author T.N.Silverman
 */
@Entity
@Table(name = "contact")
@EntityListeners({ AuditingEntityListener.class, EntityListener.class })
@JsonIgnoreProperties({ "createdDate", "modifiedDate", "createdBy", "empty" })
public class Contact implements BaseEntity {

	private static final long serialVersionUID = 7170899345644124253L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id = null;
	@Valid
	@Embedded
	private ContactSSN contactSSN;
	@NotNull
	@Size(min = 2, max = 25)
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;
	@Size(min = 2, max = 25)
	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;
	@Past
	// @NotNull
	@Column(name = "date_of_birth")
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	// @NotNull
	@Column(name = "married")
	private Boolean married;
	@Min(0)
	@Column(name = "children")
	private Integer children;
	@Column(name = "created_date", nullable = false, updatable = false, length = 40)
	@CreatedDate
	private LocalDateTime createdDate;
	@Column(name = "modified_date", length = 40)
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	@CreatedBy
	@Column(name = "created_by", nullable = false, updatable = false)
	private String createdBy;
	@Version
	@Column(name = "version")
	private Long version;

	public Contact() {
		super();
		this.version = 0L;
	}

	public Contact(ContactSSN contactSSN, String firstName, String lastName, Date dateOfBirth, Boolean married, Integer children) {
		super();
		this.contactSSN = contactSSN;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.married = married;
		this.children = children;
		this.version = 0L;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContactSSN getContactSSN() {
		return contactSSN;
	}

	public void setContactSSN(ContactSSN contactSSN) {
		this.contactSSN = contactSSN;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Boolean getMarried() {
		return married;
	}

	public void setMarried(Boolean married) {
		this.married = married;
	}

	public Integer getChildren() {
		return children;
	}

	public void setChildren(Integer children) {
		this.children = children;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public boolean isEmpty() {
		return Objects.isNull(getId()) && Objects.isNull(getContactSSN()) && Objects.isNull(getFirstName()) && Objects.isNull(getLastName()) && Objects.isNull(getDateOfBirth())
				&& Objects.isNull(getMarried()) && Objects.isNull(getChildren());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((contactSSN == null) ? 0 : contactSSN.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((married == null) ? 0 : married.hashCode());
		result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (contactSSN == null) {
			if (other.contactSSN != null)
				return false;
		} else if (!contactSSN.equals(other.contactSSN))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (married == null) {
			if (other.married != null)
				return false;
		} else if (!married.equals(other.married))
			return false;
		if (modifiedDate == null) {
			if (other.modifiedDate != null)
				return false;
		} else if (!modifiedDate.equals(other.modifiedDate))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Contact [id=");
		builder.append(id);
		builder.append(", contactSSN=");
		builder.append(contactSSN);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", dateOfBirth=");
		builder.append(dateOfBirth);
		builder.append(", married=");
		builder.append(married);
		builder.append(", children=");
		builder.append(children);
		if (createdDate != null) {
			builder.append(", createdDate=");
			builder.append(createdDate);
		}
		if (modifiedDate != null) {
			builder.append(", modifiedDate=");
			builder.append(modifiedDate);
		}
		if (createdBy != null) {
			builder.append(", createdBy=");
			builder.append(createdBy);
		}
		builder.append("]");
		return builder.toString();
	}
}
