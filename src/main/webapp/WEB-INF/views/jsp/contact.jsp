<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="title" text="Spring 4 MVC Web App Example with JSP" /></title>
    <link rel="stylesheet" href="<spring:theme code='stylesheet'/>" type="text/css" />
    <script type="text/javascript" src='<c:url value="/resources/js/plugins/jquery-3.2.1.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/js/plugins/jquery.redirect.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/js/plugins/moment-with-locales.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/js/common.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/js/contact.js"/>'></script>
    <link href='<c:url value="/static/images/favicon.ico"/>' type="image/x-icon" rel="shortcut icon">
</head>
<body style="direction: '<spring:message code='dir' text='ltr' />'" dir="<spring:message code='dir' text='ltr' />"
      data-date-pattern="<spring:message code='date.pattern' text='MM/dd/yyyy' />"
      data-language="${pageContext.response.locale.language}"
      data-context-path="<%=request.getContextPath()%>"
      data-404-page="<c:url value='/error'/>"
      data-self="">
    <spring:message code='date.pattern' var="pattern" scope="page" />
    <div class="wrapper">
        <jsp:include page='../jsp/include/navbar.jsp'>
            <jsp:param name="url" value="/jsp/contact" />
            <jsp:param name="view" value="contact-jsp" />
        </jsp:include>
        <div class="content">
            <h2><spring:message code='contact.title' text='Add/Edit Contact' />&nbsp;JSP</h2>
            <c:url var="action" value="/api/contacts/" />
            <form id="contactForm"
                  data-contact-id="<%= request.getParameter("contactId") == null ? "0" : request.getParameter("contactId")%>"
                  action="${action}"
                  autocomplete="on"
                  method="post">
                <table class="vertical">
                    <tr>
                        <td><label><spring:message code='contact.id' text='ID' /></label></td>
                        <td><input id="id" type="text" readonly="readonly" /></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span id="idError" class="error">ID Error</span></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code='contact.ssn' text='SSN' /></label></td>
                        <spring:message var="ssnErrMsg" code='invalid.ssn' text='invalid name' />
                        <td><input type="text" id="contactSSN" autocomplete="on" placeholder="<spring:message code='contact.ssn.placeholder' text='123-45-6789' />" required pattern="[0-9]{3}-[0-9]{2}-[0-9]{4}" oninvalid="this.setCustomValidity('${ssnErrMsg}')" oninput="setCustomValidity('')" /></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span id="contactSSNError" class="error">SSN Error</span></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code='contact.fname' text='First Name' /></label></td>
                        <spring:message var="nameErrMsg" code='invalid.name' text='invalid name' />
                        <td><input type="text" id="firstName" autocomplete="on" required pattern=".{2,25}" oninvalid="this.setCustomValidity('${nameErrMsg}');" oninput="setCustomValidity('')" /></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span id="firstNameError" class="error">First Name Error</span></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code='contact.lname' text='Last Name' /></label></td>
                        <spring:message var="nameErrMsg" code='invalid.name' text='invalid name' />
                        <td><input type="text" id="lastName" autocomplete="on" required pattern=".{2,25}" oninvalid="this.setCustomValidity('${nameErrMsg}');" oninput="setCustomValidity('')" /></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span id="lastNameError" class="error">Last Name Error</span></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code='contact.dob' text='Birth Date' /></label></td>
                        <spring:message var="dobErrMsg" code='invalid.dob' text='invalid date' />
                        <td><input type="text" id="dateOfBirth" autocomplete="on" placeholder="<spring:message code='contact.dob.placeholder' text='12/31/1990' />" required pattern="[0-9]{2}/[0-9]{2}/[0-9]{4}" oninvalid="this.setCustomValidity('${dobErrMsg}')" oninput="setCustomValidity('')" /></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span id="dateOfBirthError" class="error">Birth Date Error</span></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code='contact.married' text='Married' /></label></td>
                        <td><input id="married" type="checkbox"/></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span id="marriedError" class="error">Married Error</span></td>
                    </tr>
                    <tr>
                        <td><label><spring:message code='contact.children' text='No. of Kids' /></label></td>
                        <spring:message var="childrenErrMsg" code='invalid.children' text='invalid num of children' />
                        <td><input type="number" autocomplete="on" min="0" id="children" required="required" oninvalid="this.setCustomValidity('${childrenErrMsg}');" oninput="setCustomValidity('')"/></td>
                    </tr>
                    <tr style="display: none;">
                        <td></td>
                        <td class="error"><span class="error" id="childrenError">Children Error</span></td>
                    </tr>
                    <tr>
                        <td>
                            <button type="submit" name="Save" value="<spring:message code='contact.btn.save' text='Save' />">
                                <spring:message code='contact.btn.save' text='Save' />
                            </button>
                        </td>
                        <td>
                            <button type="button"  class="reset" value="<spring:message code='contact.btn.reset' text='Reset' />" onclick="clearForm(this.form);">
                                <spring:message code='contact.btn.reset' text='Clear' />
                            </button>
                        </td>
                    </tr>
                    <tr class="hidden">
                        <td class="message" colspan="2"><p data-message="<spring:message code='contact.saved'/>"></p></td>
                    </tr>
                </table>
            </form>
            <%-- <p><a href="<c:url value='/source/contact'/>"><spring:message code="show.source" text="display template code" /></a></p> --%>
        </div>
        <jsp:include page='../jsp/include/footer.jsp' />
    </div>
</body>
</html>