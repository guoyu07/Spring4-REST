/*
 * File: NonTemplateViewController.java
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
package com.tnsilver.spring.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tnsilver.spring.data.repository.ContactRepository;
import com.tnsilver.spring.domain.Contact;

@Controller
public class NonTemplateViewController {

	private static final Logger logger = LoggerFactory.getLogger(NonTemplateViewController.class);
	@Resource
	private ContactRepository contactRepository;


	@RequestMapping("excel.htm")
    protected ModelAndView contactSpreadsheet() {
        List<Contact> contacts = contactRepository.findAll();
        logger.debug("returning model and view '{}' with {} contacts","excelView",contacts.size());
        return new ModelAndView("excelView", "contacts", contacts);
    }

    @RequestMapping("pdf.htm")
    protected ModelAndView contactPDF() {
        List<Contact> contacts = contactRepository.findAll();
        logger.debug("returning model and view '{}' with {} contacts","pdfView",contacts.size());
        return new ModelAndView("pdfView", "contacts", contacts);
    }
}