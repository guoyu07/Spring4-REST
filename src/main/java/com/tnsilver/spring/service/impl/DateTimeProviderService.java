package com.tnsilver.spring.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Service;

@Service("dateTimeProvider")
public class DateTimeProviderService implements DateTimeProvider{

	@Override
	public Calendar getNow() {
		LocalDateTime dateTime = LocalDateTime.now();
		ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
	    return GregorianCalendar.from(zonedDateTime);
	}

}
