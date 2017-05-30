/*
 * File: AuditEntry.java
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Class AuditEntry represents an entry in the audit table
 *
 * @author T.N.Silverman
 */
@Entity
@Table(name = "audit")
@EntityListeners(AuditingEntityListener.class)
public class AuditEntry implements BaseEntity {

	private static final long serialVersionUID = -8078049537370237707L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id = null;
	@Size(max = 25)
	@Column(name = "action")
	private String actionType;
	@Column(name = "inception_date", length = 40)
	@CreatedDate
	private LocalDateTime inceptionDate;
	@CreatedBy
	@Column(name = "inceptor", nullable = false, updatable = false)
	private String inceptor;
	@Column(name = "entity_id")
	private Long entityId = null;
	@Size(max = 25)
	@Column(name = "entity_type")
	private String entityType;
	@Column(name = "entity", columnDefinition = "TEXT CHARACTER SET utf8 NOT NULL")
	private String entity;

	public AuditEntry() {
		super();
	}

	public AuditEntry(Long entityId, String entityType, String actionType, String entity) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
		this.actionType = actionType;
		this.entity = entity;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public LocalDateTime getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(LocalDateTime inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

	public String getInceptor() {
		return inceptor;
	}

	public void setInceptor(String inceptor) {
		this.inceptor = inceptor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionType == null) ? 0 : actionType.hashCode());
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
		result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inceptionDate == null) ? 0 : inceptionDate.hashCode());
		result = prime * result + ((inceptor == null) ? 0 : inceptor.hashCode());
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
		AuditEntry other = (AuditEntry) obj;
		if (actionType == null) {
			if (other.actionType != null)
				return false;
		} else if (!actionType.equals(other.actionType))
			return false;
		if (entity == null) {
			if (other.entity != null)
				return false;
		} else if (!entity.equals(other.entity))
			return false;
		if (entityId == null) {
			if (other.entityId != null)
				return false;
		} else if (!entityId.equals(other.entityId))
			return false;
		if (entityType == null) {
			if (other.entityType != null)
				return false;
		} else if (!entityType.equals(other.entityType))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inceptionDate == null) {
			if (other.inceptionDate != null)
				return false;
		} else if (!inceptionDate.equals(other.inceptionDate))
			return false;
		if (inceptor == null) {
			if (other.inceptor != null)
				return false;
		} else if (!inceptor.equals(other.inceptor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuditEntry [id=");
		builder.append(id);
		builder.append(", entityId=");
		builder.append(entityId);
		builder.append(", entityType=");
		builder.append(entityType);
		builder.append(", actionType=");
		builder.append(actionType);
		builder.append(", entity=");
		builder.append(entity);
		builder.append(", inceptionDate=");
		builder.append(inceptionDate);
		builder.append(", inceptor=");
		builder.append(inceptor);
		builder.append("]");
		return builder.toString();
	}
}
