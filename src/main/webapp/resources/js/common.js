/**
 * File: common.js Creation Date: May 20, 2017
 *
 * JavaScript utility functions to support UI and form related actions
 *
 * Copyright (c) 2016 T.N.Silverman - all rights reserved
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author T.N.Silverman
 */

/**
 * gets the context path of the application based on the value of the body's 'data-context-path' attribute
 */
function getContextPath() {
	var ctx = $("body").attr("data-context-path");
	if (isEmpty(ctx)) {
		console.debug("common.js:getContextPath() -> no 'data-context-path' attribute found on body element!");
		return "/";
	}
	return ctx;
}

/**
 * gets the context path of the application based on the value of the body's 'data-image-path' attribute
 */
function getImagePath() {
	var path = $("body").attr("data-image-path");
	if (isEmpty(path)) {
		console.debug("common.js:getImagePath() -> no 'data-image-path' attribute found on body element!");
		return "resources/images";
	}
	return path;
}

/**
 *
 * @param dateString -
 *            the date string from DB or from user input
 * @param toJson
 *            if true, format the date string to json ("YYYY-MM-DD") otherwise
 *            the date string is already in json format and needs formatting in
 *            locale specific pattern for display in the UI
 * @returns a re-formatted date string
 */
function reformatDate(dateString, toJson) {
	var language = $("body").attr('data-language');
	var pattern = $("body").attr('data-date-pattern');
	if (isEmpty(language)) {
		console.warn("common.js:reformatDate() -> no 'data-language' attribute found on body element!");
		langauge = "en";
	} else {
		language = language == "iw" ? "he" : language; // swap java 'iw' to
														// standard 'he'
	}
	if (isEmpty(pattern)) {
		console.warn("common.js:reformatDate() -> no 'data-date-pattern' attribute found on body element!");
		pattern = "MM/DD/YYYY";
	} else {
		pattern = pattern.toUpperCase(); // upper case pattern to conform
											// with moment
	}
	moment.locale(language);
	var result = "";
	if (toJson) {
		result = moment(dateString, pattern, false).format("YYYY-MM-DD");
	} else {
		result = moment(dateString, "YYYY-MM-DD", false).format(pattern);
	}
	if (isEmpty(result) || result === 'Invalid date') {
		console.warn("invalid date ", dateString);
		result = "";
	}
	return result;
}

/**
 * Test if object is empty
 * @param obj - any JS object
 * @returns true if the given object is empty
 */
function isEmpty(obj) {
	if (typeof obj == 'undefined')
		return true;
	if (obj == null)
		return true;
	if (typeof obj == 'string' && obj.length > 0 && obj.trim() === "0")
		return false;
	if (typeof obj == 'number' && obj == 0)
		return false;
	if (obj == false)
		return true;
	if ((obj.length == 0) || (obj == "") || (typeof obj == 'string' && obj.replace(/\s/g, "") == "")
			|| (!/[^\s]/.test(obj)) || (/^\s*$/.test(obj)))
		return true;
	if ((typeof obj == 'string' && obj.trim().length == 0) || obj.length == 0)
		return true;
	return false;
}

/**
 * Sets the given source on the image element inside the given btn element
 */
function swapBtnImage(btn, source) {
	//console.debug("swapping btn image with source ", source);
	var img = btn.children[0];
	if (img != null && typeof img !== 'undefined') {
		img.src = source;
	}
}

