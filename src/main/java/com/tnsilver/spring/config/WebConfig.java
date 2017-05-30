/*
 * File: WebConfig.java
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
package com.tnsilver.spring.config;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

import com.tnsilver.spring.controller.Controllers;
import com.tnsilver.spring.converter.ContactSSNConverter;
import com.tnsilver.spring.converter.DateConverter;
import com.tnsilver.spring.validator.Validators;
import com.tnsilver.spring.view.Views;

/**
 * Class WebConfig.
 *
 * @author T.N.Silverman
 */
@Configuration
@ComponentScan(basePackageClasses = { Controllers.class, Views.class, RestConfig.class, Validators.class })
@EnableSpringDataWebSupport
@PropertySource({ "classpath:application.properties", "classpath:jdbc.properties" })
/**
 * The @EnableWebMvc is equivalent to <mvc:annotation-driven /> in XML. It enables support for @Controller-annotated
 * classes that use @RequestMapping or @GetMapping to map incoming requests to certain methods.
 */
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private DateConverter dateConverter;
	@Autowired
	private ContactSSNConverter contactSSNConverter;

	/**
	 * The configureDefaultServletHandling() method is overridden and we enable default servlet handler. This will let
	 * other http request such as .css, .js slip through the usual DispatcherServlet and let the container process them.
	 * So now we can serve the static files css and javascript from our WebApp folder.
	 */
	/*
	 * @Override public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	 * configurer.enable("myCustomServlet"); }
	 */
	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {
		formatterRegistry.addConverter(dateConverter);
		formatterRegistry.addConverter(contactSSNConverter);
	}

	/**
	 * addResourceHandler("/css/**") = how you write URL's in your app addResourceLocations("/resources/css/") = where
	 * the resources really are (relative to the root context)
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/html/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/jsp/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/html/hello/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/jsp/hello/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/themes/**").addResourceLocations("/themes/");
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("en"));
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public ThemeChangeInterceptor themeChangeInterceptor() {
		ThemeChangeInterceptor interceptor = new ThemeChangeInterceptor();
		interceptor.setParamName("theme");
		return interceptor;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("WEB-INF/i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(themeChangeInterceptor());
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//registry.addViewController("/").setViewName("redirect:/html/home");
		registry.addRedirectViewController("/", "/html/home");
		registry.addViewController("/html/home").setViewName("html/home");
		registry.addViewController("/html/contact").setViewName("html/contact.html");
		registry.addViewController("/html/contacts").setViewName("html/contacts.html");
		registry.addViewController("/jsp/contact").setViewName("jsp/contact");
		registry.addViewController("/jsp/contacts").setViewName("jsp/contacts");
	}

	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(null);
        argumentResolvers.add(resolver);
        super.addArgumentResolvers(argumentResolvers);
    }

}
