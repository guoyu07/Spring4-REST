/*
 * File: LocalDateAttributeConverter.java
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

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;

/**
 * This class is not required in the application - it's a hack when hibernate-java8 is not on the classpath
 *
 * Class LocalDateAttributeConverter. You need to implement the AttributeConverter<LocalDate, Date> interface with its
 * two methods convertToDatabaseColumn and convertToEntityAttribute. As you can see on the method names, one of them
 * defines the conversion from the type of the entity attribute (LocalDate) to the database column type (Date) and the
 * other one the inverse conversion. The conversion itself is very simple because java.sql.Date already provides the
 * methods to do the conversion to and from a LocalDate.
 *
 * Additionally the attribute converter needs to be annotated with the @Converter annotation. Due to the optional
 * autoApply=true property, the converter will be applied to all attributes of type LocalDate.
 *
 * The conversion of the attribute is transparent to the developer and the LocalDate attribute can be used as any other
 * entity attribute.
 *
 * @author T.N.Silverman
 */
/*@Converter(autoApply = true)*/
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return (localDate == null ? null : Date.valueOf(localDate));
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		return (sqlDate == null ? null : sqlDate.toLocalDate());
	}
}