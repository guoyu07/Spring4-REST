/**
 * File: contacts.js Creation Date: May 27, 2017
 *
 * JavaScript AJAX support for the contacts view
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
 * assemble and return the paging sort parameter value (e.g. 'firstName,DESC')
 * this is achieved by collecting the spans with the 'sort_xxx' class and
 * reading their data-sort-property and data-sort-direction properties.
 * Once assembled, all but the sorting span classes are reset to 'sort_both'
 */
function getSort() {
	var def = "id,ASC";
	var sort;
	$('span[class^=sort_]').not('span[class=sort_both]').each(function(){
		var property = $(this).attr("data-sort-property");
		var direction = $(this).attr("data-sort-direction");
		//console.debug("contacts.js:getSort() -> property: ", property, " direction: ", direction);
		if (!isEmpty(direction)) {
			sort = property + "," + direction;
		}
	})
	return isEmpty(sort) ? def : sort;
}

/**
 * Toggles the sort class on the sorting spans and fetches search results
 * @param span the span clicked
 */
function toggleSort(span) {
	var clazz = $(span).attr("class");
	$('span[class^=sort_]').each(function(){ // rest all
		$(this).attr("data-sort-direction","");
		$(this).attr("class","sort_both");
	})
	switch (clazz) {
		case 'sort_asc':
			$(span).attr("class","sort_desc");
			$(span).attr("data-sort-direction","DESC");
			break;
		case 'sort_desc':
			$(span).attr("class","sort_asc");
			$(span).attr("data-sort-direction","ASC");
			break;
		case 'sort_both':
			$(span).attr("class","sort_asc");
			$(span).attr("data-sort-direction","ASC");
			break;
		default:
			break;
	}
	//console.debug("contacts.js:toggleSort() -> property ",$(span).attr("data-sort-property"), " direction: ", $(span).attr("data-sort-direction"));
	$('.error').css("display", "none");
	getFilteredContacts();
}

/**
 * parse the filter form inputs and assemble a query string
 * @returns the query string
 */
function buildFilterQueryString() {
	var query = "?"; // initial query string
	$("#filterForm input[type=text], input[type=number], input[type=checkbox]").each(function() { // Iterate over inputs
		var name = $(this).attr('id');
		var val = $(this).is(':checkbox') ?  $(this).prop('checked') : $(this).val();
		if ((name === 'dateOfBirth' && isEmpty(val)) || isEmpty(name))  return true;
		query = query + name + "=" + decodeURIComponent(val) + "&";
	});
	return query.substr(0,query.length -1); // remove last '&'
}

/**
 * redirects to the editing url as found on the body's data-edit-url attribute
 * @param contactId the contact id to edit.
 */
function editContact(contactId) {
   var url = $('body').attr("data-edit-url");
   $.redirect(url,{"contactId": contactId },"GET");
}

/**
 * deletes a contact by issuing the DELETE verb on the contacts REST api end point
 * @param btn the button clicked where the attribute 'data-self' is the REST api end point
 */
function deleteContact(btn) {
   ajax($(btn).attr("data-self"),"","DELETE");
}

/**
 * builder function to assemble an HTML form in the data section of the HTML.
 * Each form is a row containing one contact data. This utility is managed
 * from the director buildContactsTable() function.
 * @param index - the current row index
 * @param contact the contact to display in the form
 * @returns an HTML string representing a contact form
 */
function buildContactForm(index,contact) {
    var clazz = index % 2 == 0 ? 'tr' : "tr odd";
    var dateOfBirth = isEmpty(contact.dateOfBirth) ? contact.dateOfBirth : reformatDate(contact.dateOfBirth,false);
    var checked = contact.married ? 'checked=\'checked\' ' : '';
    var theme = $("body").attr("data-theme");
    var confirm = $("body").attr("data-confirm");
    confirm = confirm.replace("@",contact.firstName);
    confirm = confirm.replace("@",contact.lastName);
    var form = "<form id='contactForm" + contact.id + "' class='" + clazz + "' method='post' action='#'>";
	form = form + "<div><span>&nbsp;</span><input class='td' name='id' type='text' value='" + contact.id + "' readonly='readonly' /></div>";
	form = form + "<div><span>&nbsp;</span><input class='td' name='contactSSN' type='text' value='" + (isEmpty(contact.contactSSN) ? '' : contact.contactSSN.ssn) + "' readonly='readonly' /></div>"
	form = form + "<div><span>&nbsp;</span><input class='td' name='firstName' type='text' value='" + contact.firstName + "' readonly='readonly' /></div>";
	form = form + "<div><span>&nbsp;</span><input class='td' name='lastName' type='text' value='" + contact.lastName + "' readonly='readonly' /></div>"
	form = form + "<div><span>&nbsp;</span><input class='td' name='dateOfBirth' type='text' value='" + dateOfBirth + "' readonly='readonly' /></div>"
	form = form + "<div><span>&nbsp;</span><input class='td fixed-width' type='checkbox' disabled='disabled' " + checked + " /><input type='hidden' name='married' value='" + contact.married + "' /></div>";
	form = form + "<div><span>&nbsp;</span><input class='td' name='children' type='text' value='" + contact.children + "' readonly='readonly' /></div>";
    form = form + "<button class='td fixed-width control' type='button' name='Edit' onmouseover='swapBtnImage(this,&#39;" + getImagePath() + "themes/" + theme + "/edit_hover.png&#39;);' onmouseout='swapBtnImage(this,&#39;" + getImagePath() + "themes/" + theme + "/edit.png&#39;);' onclick='editContact(" + contact.id + ");'>";
    form = form + "<img src='" + getImagePath() + "themes/" + theme + "/edit.png'>";
    form = form + "</button>";
    form = form + "<button class='td fixed-width control' type='button' name='Delete' data-self='" + contact._links.self.href + "' onmouseover='swapBtnImage(this,&#39;" + getImagePath() + "themes/" + theme + "/delete_hover.png&#39;);' onmouseout='swapBtnImage(this,&#39;" + getImagePath() + "themes/" + theme + "/delete.png&#39;);' onclick='if (confirm(&#39;" + confirm + "&#39;)) { return deleteContact(this); } else { return false; }'>";
    form = form + "<img align='left' src='" + getImagePath() + "themes/" + theme + "/delete.png'/>";
    form = form + "</button>";
    form = form + "<div><span>&nbsp;</span></div>";
 	form = form + "</form>";
	return form;
}

/**
 * builds the contacts table where each row is a form displaying contact details.
 * This method is triggered by an HTTP response 200, indicating there's data to display.
 * @param data the Json data obtained from the contacts REST api end point.
 */
function buildContactsTable(data) {
    var totalElements = data.page.totalElements;
    if (!isEmpty(totalElements) && totalElements === 0) {
    	console.warn("contacts.js:buildContactsTable() -> no data found!");
    	$('.error').css("display", "");
    	return;
    }
    var contacts = data._embedded.contacts;
    $.each(contacts, function(index, contact) {
        $('#data').append(buildContactForm(index,contact));
	});
}

/**
 * builds the select and options in the pagination section allowing the user
 * to select the number of rows in each page for display
 * @param data the Json data obtained from the contacts REST api end point.
 * @returns an HTML string representing a select form with options
 */
function buildSizeSelectForm(data) {
	var all = $('#paginationTable').attr("data-pag-all");
	var size = data.page.size;
	var number = data.page.number;
	var sort = $('#paginationTable').attr("data-pag-sort"); // TODO: append the sort param to this attribute
	var form = "<form>";
	form += "<select id='size' onchange='getFilteredContacts()'>";
	form += "<option label='5' value='5'"   + (size === 5 ? " selected='selected'" : "") + ">5</option>";
	form += "<option label='10' value='10'" + (size === 10 ? " selected='selected'" : "") + ">10</option>";
	form += "<option label='15' value='15'" + (size === 15 ? " selected='selected'" : "") + ">15</option>";
	form += "<option label='20' value='20'" + (size === 20 ? " selected='selected'" : "") + ">20</option>";
	form += "<option label='25' value='25'" + (size === 25 ? " selected='selected'" : "") + ">25</option>";
	form += "<option label='" + all + "' value='2000'" + (size === 2000 ? " selected='selected'" : "") + ">" + all + "</option>";
	form += "</select>";
	form += "</form>";
	return form;
}

/**
 * builds a <TR> in the pagination table with a first page link
 * @param data the Json data obtained from the contacts REST api end point.
 * @returns an HTML string representing a tr (table row) in the pagination table
 */
function buildPagingFirstLink(data) {
	var undef;
	var first = isEmpty(data._links.first) ? undef : data._links.first.href;
	var firstText = $('#paginationTable').attr("data-pag-first");
	var tr = "<td class='paginationFirst'>";
    if (!isEmpty(first))
    	tr += "<a id='firstPageLink' title='" + firstText + "' href='#' data-href='" + first + "' onclick='gotoPageLink(this);'><span class='paginationFirstCharacter'>&laquo;</span></a>";
    else
    	tr += "<span class='paginationFirstCharacter' title='" + firstText + "'>&laquo;</span>";
    tr += "</td>";
	return tr;
}

/**
 * builds a <TR> in the pagination table with a previous page link
 * @param data the Json data obtained from the contacts REST api end point.
 * @returns an HTML string representing a tr (table row) in the pagination table
 */
function buildPagingPrevLink(data) {
	var prev = isEmpty(data._links.prev) ? null : data._links.prev.href;
	var prevText = $('#paginationTable').attr("data-pag-prev");
	var tr = "<td class='paginationPrevious'>";
    if (!isEmpty(prev))
    	tr += "<a id='prevPageLink' title='" + prevText + "' href='#' data-href='" + prev + "' onclick='gotoPageLink(this);'><span class='paginationPreviousCharacter'>&lsaquo;</span></a>";
    else
    	tr += "<span class='paginationPreviousCharacter' title='" + prevText + ">&lsaquo;</span>";
    tr += "</td>";
    return tr;
}

/**
 * builds a <TR> in the pagination table with a next page link
 * @param data the Json data obtained from the contacts REST api end point.
 * @returns an HTML string representing a tr (table row) in the pagination table
 */
function buildPagingNextLink(data) {
	var undef;
	var next = isEmpty(data._links.next) ? undef : data._links.next.href;
	var nextText = $('#paginationTable').attr("data-pag-next");
    var tr = "<td class='paginationNext'>";
    if (!isEmpty(next))
    	tr += "<a id='nextPageLink' title='" + nextText + "' href='#' data-href='" + next + "' onclick='gotoPageLink(this);'><span class='paginationNextCharacter'>&rsaquo;</span></a>";
    else
    	tr += "<span class='paginationNextCharacter' title='" + nextText + ">&rsaquo;</span>";
    tr += "</td>";
    return tr;
}

/**
 * builds a <TR> in the pagination table with a last page link
 * @param data the Json data obtained from the contacts REST api end point.
 * @returns an HTML string representing a tr (table row) in the pagination table
 */
function buildPagingLastLink(data) {
	var undef;
	var last = isEmpty(data._links.last) ? undef : data._links.last.href;
	var lastText = $('#paginationTable').attr("data-pag-last");
    var tr = "<td class='paginationLast'>";
    if (!isEmpty(last))
    	tr += "<a id='lastPageLink' title='" + lastText + "' href='#' data-href='" + last + "' onclick='gotoPageLink(this);'><span class='paginationLastCharacter'>&raquo;</span></a>";
    else
    	tr += "<span class='paginationLastCharacter' title='" + lastText + ">&raquo;</span>";
    tr += "</td>";
	return tr;
}

/**
 * builds a <TR> in the pagination table with a links to each of the existing pages
 * @param data the Json data obtained from the contacts REST api end point.
 * @returns an HTML string representing a <tr> elements (table rows) in the pagination table
 */
function buildPageLinks(data) {
    var tr = "";
	var totalPages = data.page.totalPages;
	var currentPage = data.page.number;
	var action = $('#filterForm').attr("action");
	var query = buildFilterQueryString();
    for (var i = 0;i < totalPages;i++) {
    	var url = action;
    	url += query;
    	tr += "<td class='paginationPages'>";
        if (currentPage !== i) {
        	url += "&page=" + i;
        	url += "&size=" + data.page.size;
        	url += "&sort=" + getSort();
        	//console.debug("contacts.js:buildPageLinks() url: ", url);
        	tr += "<a id='page" + i + "Link' href='#' data-href='" + url + "' onclick='gotoPageLink(this);'><span class='paginationPagesCharacter'>" + (i+1) + "</span></a>";
        } else {
        	tr += "<span class='paginationActivePageCharacter'>" + (i+1) + "</span>";
        }
    	tr += "</td>";
    	tr += "<td class='paginationBetweenPages'></td>";
    }
	return tr;
}

/**
 * director method to build the pagination table
 * @param data the Json data obtained from the contacts REST api end point.
 */
function buildPagingTable(data) {
	var undef;
	var numOfRowsText = $('#paginationTable').attr("data-pag-record-num");
	var tr = "<tr>";
    tr += "<td class='paginationFirst'><span>" + numOfRowsText + "</span></td>";
    tr += "<td>";
    tr += "" + buildSizeSelectForm(data) + "";
    tr += "</td>";
    tr += "<td class='paginationBetweenPages'></td>";
    tr += buildPagingFirstLink(data);
    tr += "<td class='paginationBetweenPages'></td>";
    tr += buildPagingPrevLink(data);
    tr += "<td class='paginationBetweenPages'></td>";
    tr += buildPageLinks(data);
    tr += buildPagingNextLink(data);
    tr += "<td class='paginationBetweenPages'></td>";
    tr += buildPagingLastLink(data);
    tr += "<td class='paginationBetweenPages'></td>";
	tr += "</tr>";
    $("#paginationTable").append(tr);
}


/**
 * Perform an AJAX asynchronous call to the given url with the given HTTP method type with the given data
 * @param url - the url to ajax to
 * @param data - the data to send to the server
 * @param type - the HTTP method type (GET,POST,etc...)
 */
function ajax(url, data, type) {
	$.ajax({
		type : type,
		url : url,
		data : data,
	});
}

/**
 * empties the data and pagination table divs
 */
function clear() {
	$('#data').empty();
	$('#paginationTable').empty();
}

/**
 * Navigates to a pagination link
 * @param a the a:href anchor that triggered the call and contains the 'data-href' URL attribute
 */
function gotoPageLink(a) {
	var link = $(a).attr("data-href");
	console.log("contacts.js:gotoPageLink() -> link: ", link);
	ajax(link,{},"GET");
}
/**
 * builds a url with paging parameters
 * @param href - the URL
 * @returns a modified URL with paging parameters to send to the paginating REST api end point
 */
function buildPagingUrl(href) {
	var url = href + buildFilterQueryString();
	var page = $('span[class=paginationActivePageCharacter]').text();
	var size = $('#size option:selected').val();
	url += !isEmpty(page) ? ("&page=" + (page - 1)) : "&page=0"
	url += !isEmpty(size) ? ("&size=" + size) : "&size=10";
	url += "&sort=" + getSort();
	return url;
}

/**
 * calls the REST api end point with requests to find data matching the filter form inputs
 */
function getFilteredContacts() {
	var url = buildPagingUrl($("#filterForm").attr('action'));
	//console.debug("contacts.js:getFilteredContacts() -> ajax: (GET) url -> ", url);
	clear();
	ajax(url, "", "GET");
}

$(document).ready(function() {
	/**
	 * ajax setup, properties, status codes and response status event handlers
	 */
	$.ajaxSetup({
			contentType : "application/json",
			dataType : "json",
			async : true,
			cache : false,
			beforeSend : function(jqXHR, settings) {
				$('#loader').toggleClass('active');
				$('.error').css("display", "none");
				jqXHR.setRequestHeader("Content-Type", "application/json");
			},
			statusCode: {
			    200: function(data, status, jqXHR) { // OK (GET)
			    	//console.debug("contacts.js:ajaxSetup() (200) -> got: ", data);
			    	$('#loader').toggleClass('active');
			    	clear();
			    	buildContactsTable(data);
			    	buildPagingTable(data);
			    },
			    204: function(data, status, jqXHR) { // DELETE (success)
			    	//console.debug("contacts.js:ajaxSetup() (204) -> DELETE succcess");
			    	$("#filterForm").submit(); // reload data
			    	$('#loader').toggleClass('active');
			    },
			    404: function(jqXHR, status, error) { // NOT FOUND
				    console.warn("contacts.js:ajaxSetup() (404) -> not found: ", error);
				    $.redirect($('body').attr('data-404-page'),{},"GET");
			    }
		    }
	});


	/**
	 * filter form submit handler
	 */
	$("#filterForm").submit(function(event) {
		event.preventDefault();
		$('div.pagination').removeClass("no-pagination");
		getFilteredContacts();
	});

	/**
	 * reset click handler
	 */
	$(".reset").click(function() {
		$(this).closest('form').find("input[type=text], input[type=hidden], input[type=number]").val("");
		$(this).closest('form').find("input[type=text], input[type=hidden], input[type=number]")[0].setCustomValidity("");
		$(this).closest('form').find("input[type=checkbox]").prop('checked', false);
		$(this).closest('form').find("span.error").text("");
		$('.error').css("display", "none");
		$('div.pagination').addClass("no-pagination");
		clear();
	});

	/**
	 * Initial call to load whatever matches the filter (even if empty)
	 */
	getFilteredContacts();

});