/*
 * File: ContactSSN.java
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class ContactSSN represents a social security number
 *
 * @author T.N.Silverman
 */
@Embeddable
@JsonIgnoreProperties({ "asString", "valid" })
public class ContactSSN implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(ContactSSN.class);
	private static final long serialVersionUID = 6239698134349222445L;
	public static final String REGEX = "^\\d{3}-?\\d{2}-?\\d{4}$";
	@Transient
	private Integer area;
	@Transient
	private Integer group;
	@Transient
	private Integer serial;
	// @NotNull
	@Pattern(regexp = "^\\d{3}-?\\d{2}-?\\d{4}$")
	@Column(name = "contact_ssn")
	private String ssn;

	public String getAsString() {
		return area + "-" + group + "-" + serial;
	}

	public boolean isValid() {
		try {
			boolean validArea = (area != null && area.toString().length() == 3);
			boolean validGroup = (group != null && group.toString().length() == 2);
			boolean validSerial = (serial != null && serial.toString().length() == 4);
			return validArea && validGroup && validSerial;
		} catch (Exception ex) {
			return false;
		}
	}

	public ContactSSN() {
		super();
	}

	public ContactSSN(String ssn) {
		super();
		try {
			area = Integer.parseInt(ssn.substring(0, 3));
			group = Integer.parseInt(ssn.substring(4, 6));
			serial = Integer.parseInt(ssn.substring(7, 11));
			this.ssn = getAsString();
		} catch (Exception ignore) {
			logger.warn("invalid ssn {}", ssn);
			this.ssn = ssn;
		}
	}

	public ContactSSN(Integer area, Integer group, Integer serial) {
		super();
		this.area = area;
		this.group = group;
		this.serial = serial;
		this.ssn = getAsString();
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		try {
			area = Integer.parseInt(ssn.substring(0, 3));
			group = Integer.parseInt(ssn.substring(4, 6));
			serial = Integer.parseInt(ssn.substring(7, 11));
			this.ssn = getAsString();
		} catch (Exception ignore) {
			logger.warn("invalid ssn {}", ssn);
			this.ssn = ssn;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ssn == null) ? 0 : ssn.hashCode());
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
		ContactSSN other = (ContactSSN) obj;
		if (ssn == null) {
			if (other.ssn != null)
				return false;
		} else if (!ssn.equals(other.ssn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ssn;
	}
}
