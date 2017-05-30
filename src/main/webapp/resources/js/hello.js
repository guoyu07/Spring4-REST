/**
 * File: hello.js Creation Date: May 24, 2017
 *
 * JavaScript AJAX support for the hello view
 *
 * Copyright (c) 2016 T.N.Silverman - all rights reserved
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * @author T.N.Silverman
 */

function greet() {
	var url = $('#greeting').attr("data-url");
	var firstName = $('#greeting').attr("data-firstName");
	var lastName = $('#greeting').attr("data-lastName");
	console.debug("ajaxing url: ",url," firstName: ", firstName, " lastName: ", lastName);
	if (typeof firstName != 'undefined') {
		url = url + "/" + firstName;
		if (typeof lastName != 'undefined') {
			url = url + "/" + lastName;
		}
	}
	$.ajax({
        url: url
    }).then(function(data) {
       console.log("data ", data);
       //$('#greeting').text(decodeURIComponent(escape(data)));
       $('#greeting').text(decodeURIComponent(data));
    });
}

$(document).ready(function() {
	greet();
});