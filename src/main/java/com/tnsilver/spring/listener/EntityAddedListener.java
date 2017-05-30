/*
 * File: EntityAddedListener.java
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

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tnsilver.spring.event.EntitySaveEvent;

/**
 * The listener interface for receiving entityAdded events.
 * The class that is interested in processing a entityAdded
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addEntityAddedListener<code> method. When
 * the entityAdded event occurs, that object's appropriate
 * method is invoked.
 *
 * @author T.N.Silverman
 * @see EntityAddedEvent
 */
@Component
public class EntityAddedListener implements ApplicationListener<EntitySaveEvent> {

	private static final Logger logger = LoggerFactory.getLogger(EntityAddedListener.class);
	@Value("${useSwingAlert}")
	private Boolean useSwingAlert;

	public EntityAddedListener() {
		super();
	}

	public void setUseSwingAlert(Boolean useSwingAlert) {
		this.useSwingAlert = useSwingAlert;
	}

	@Override
	public void onApplicationEvent(EntitySaveEvent event) {
		String message = event.getSource() + " was just added!";
		if (useSwingAlert)
			JOptionPane.showMessageDialog(null, message, "New entity added", JOptionPane.INFORMATION_MESSAGE);
		else
			logger.debug("Listener: {}", message);
	}
}
