/*
 * File: LocalDateTimeAttributeConverter.java
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
package com.tnsilver.spring.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;

/**
 * This class is not required in the application - it's a hack when hibernate-java8 is not on the classpath
 *
 * Class LocalDateTimeAttributeConverter. The attribute converter for LocalDateTime is basically the same. You need to
 * implement the AttributeConverter<LocalDateTime, Timestamp> interface and the converter needs to be annotated with
 * the @Converter annotation. Similar to the LocalDateConverter, the conversion between a LocalDateTime and an
 * java.sql.Timestamp is done with the conversion methods of Timestamp.
 *
 * @author T.N.Silverman
 */
/*@Converter(autoApply = true)*/
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		return (localDateTime == null ? null : Timestamp.valueOf(localDateTime));
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
		return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
	}
}