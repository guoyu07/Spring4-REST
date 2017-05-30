/*
 * File: EntityListener.java
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
package com.tnsilver.spring.listener;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tnsilver.spring.domain.BaseEntity;

/**
 * This listener is used to debug RESTful DML events on {@link BaseEntity} sub-classes.
 * Since there are no controllers to use for this purpose, this is one comfortable alternative.
 *
 * @author T.N.Silverman
 * @see EntityEvent
 */
public class EntityListener {

	private static final Logger logger = LoggerFactory.getLogger(EntityListener.class);

	@PrePersist
    private void prePersist(BaseEntity entity) {
       logger.debug("saving {}",entity.toString());
	}

    @PreUpdate
    private void preUpdate(BaseEntity entity) {
       logger.debug("updating {}",entity.toString());
	}

    @PreRemove
    private void preRemove(BaseEntity entity) {
    	logger.debug("deleting {}",entity.toString());
	}
}
