/**
 * File: contact.js Creation Date: May 24, 2017
 *
 * JavaScript AJAX support for the add / edit contact view
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
 * creates a json object out for the contact form inputs
 *
 * @returns a json object for example:  {"id":"1","contactSSN":{"ssn":"123-45-6789"},"firstName":"Homer","lastName":"Simpson","dateOfBirth":"1978-08-01","married":true,"children":"3"}
 */
function createJsonObj() {
	var json = {}; // empty json object
	$("#contactForm input[type=text], input[type=number], input[type=checkbox]").each(function() { // Iterate over inputs
		json[$(this).attr('id')] = $(this).is(':checkbox') ?  $(this).prop('checked') : $(this).val(); // distinguish checkbox from inputs
	});
	if (!isEmpty(json.contactSSN)) { // replace ssn string with object
		var ssn = {};
		ssn['ssn'] = json.contactSSN;
		json.contactSSN = ssn;
	}
	json.dateOfBirth = isEmpty(json.dateOfBirth) ? '' : reformatDate(json.dateOfBirth, true); // format date
    return json;
}

/**
 * populates the contact form from a given json object
 * @param data the json object
 */
function populateForm(data) { // TODO: replace this with $.(each)
	$('#contactForm').attr("data-self",data._links.self.href);
	$('#id').val(data.id);
	$('#contactSSN').val(isEmpty(data.contactSSN) ? '' : data.contactSSN.ssn);
	$('#firstName').val(data.firstName);
	$('#lastName').val(data.lastName);
	$('#dateOfBirth').val(isEmpty(data.dateOfBirth) ? '' : reformatDate(data.dateOfBirth, false));
	$("#married").prop('checked',data.married);
	$('#children').val(data.children);
}

/**
 * performs an ajax GET call to load a given contact id
 * The ajax event handlers will populate the contact form
 * @param contactId the contact id (passed in the request to the HTML page)
 */
function loadContact(contactId) {
	var url = $("#contactForm").attr("action");
    ajax((url + contactId),"",'GET');
}

/**
 * obtains a json data object to POST to the server for update (PATCH) or saving (POST)
 * The ajax event handlers will navigate to the contacts view for display
 */
function saveContact() {
	var dataObj = createJsonObj();
	var url = $("#contactForm").attr('action');
	var self = $("#contactForm").attr('data-self');
	var method = isEmpty(dataObj.id) ? "POST" : "PATCH";
	url = isEmpty(self) ? (isEmpty(dataObj.id) ? url : (url + dataObj.id)) : self;
	var data = JSON.stringify(dataObj);
	//console.debug("contact.js:saveContact() -> ajax: (", method, ") url -> ", url, " with data -> ", data);
	var success = function(contact, status, jqXHR) {
		console.debug("contact.js:saveContact() -> successful saved data: ", contact);
		$('#id').val(contact.id);
		var message = $('tr.message p:first-child').attr("data-message");
		message = message.replace("{0}",contact.firstName);
		message = message.replace("{1}",contact.lastName);
		$('tr.message p:first-child').text(message);
		$("tr.message").fadeIn("slow");
	};
	ajax(url, data, method, success);
}

/**
 * Distribute the errors in the response body to the corresponding error tr's and spans
 * @param errors the error response json object
 */
function displayValidationErrors(errors) {
	console.debug("contact.js:showErrors() -> showing errors: ", errors);
	$.each(errors, function(i, error) {
		$.each(error, function(j, data) {
			var target = data.property + "Error";
			$('#' + target).closest('tr').css("display", "");
			$('#' + target).text(data.message);
		});
	});
}

/**
 * Perform an AJAX asynchronous call to the given url with the given HTTP method type with the given data
 * @param url - the url to ajax
 * @param data - the data to send to the server
 * @param type - the HTTP method type (GET,POST,etc...)
 * @param success - a function for execution on ajax success (function(data, status, jqXHR))
 */
function ajax(url, data, type, success) {
	var def = function(data, status, jqXHR) {};
	$.ajax({
		type : type,
		url : url,
		data : data,
		success : typeof(success) == undefined ? def : success
	})
}

$(document).ready(function() {

	// clear the message tr
	$("tr.message").fadeOut("fast");

	/**
	 * ajax setup, properties, status codes and even handlers
	 */
	$.ajaxSetup({
			contentType : "application/json",
			dataType : "json",
			async : true,
			cache : false,
			beforeSend : function(jqXHR, settings) {
				$('#loader').toggleClass('active');
				jqXHR.setRequestHeader("Content-Type", "application/json");
			},
			complete : function(jqXHR, status) {
				$('#loader').toggleClass('active');
		    },
			statusCode: {
			    200: function(data, status, jqXHR) { // OK (GET)
			    	//console.debug("contact.js:ajaxSetup() (200) -> got: ", data);
			    	populateForm(data);
			    },
			    400: function(jqXHR, status, error) { // BAD REQUEST (validation errors)
			    	var json = $.parseJSON(jqXHR.responseText);
			    	//console.debug("contact.js:ajaxSetup() (400) -> validation errors: ", json);
					displayValidationErrors(json);
			    },
			    404: function(jqXHR, status, error) { // NOT FOUND
			        //console.debug("contact.js:ajaxSetup() (404) -> not found: ", error);
			        $.redirect($('body').attr('data-404-page'),null,"GET");
			    }
		    }
	});

	/**
	 * extract the previous request scoped contactId from the form tag
	 * and load the data in the form inputs
	 */
	var contactId = $("#contactForm").attr("data-contact-id");
	if (!isEmpty(contactId) && contactId > 0) {
		loadContact(contactId);
	}

	/**
	 * Submit handler
	 */
	$("#contactForm").submit(function(event) {
		$("tr.message").fadeOut("fast");
		event.preventDefault();
		saveContact();
	});

	/**
	 * reset click handler
	 */
	$(".reset").click(function() {
		$("tr.message").fadeOut("fast");
		$(this).closest('form').find("input[type=text], input[type=hidden], input[type=number]").val("");
		$(this).closest('form').find("input[type=text], input[type=hidden], input[type=number]")[0].setCustomValidity("");
		$(this).closest('form').find("input[type=checkbox]").prop('checked', false);
		$(this).closest('form').find("span.error").text("");
		$(this).closest('form').find("tr.error").css("display", "none");
		$('#contactForm').attr("data-self",'');
	});

});