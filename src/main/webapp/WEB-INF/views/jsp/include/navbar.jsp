<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="org.apache.commons.io.FilenameUtils"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <c:url var="url" value='<%= request.getParameter("url") %>'/>
    <c:set var="view" value='<%= request.getParameter("view") %>'/>
    <%-- <c:out value="${url}"/> <c:out value="${view}"/> --%>
    <div class="nav">
        <ul>
            <li><a href='<c:url value="/html/home"/>'><spring:message code='page.home' text="Home"/></a></li>
            <li><a href='<c:url value="/jsp/hello"/>' <c:if test="${view eq 'hello-jsp'}">class="active"</c:if>><spring:message code='page.hello.jsp' text="Hello JSP"/></a></li>
            <li><a href='<c:url value="/html/hello"/>' <c:if test="${view eq 'hello-html'}">class="active"</c:if>><spring:message code='page.hello.html' text="Hello HTML"/></a></li>
            <li><a href='<c:url value="/jsp/contacts"/>' <c:if test="${view eq 'contacts-jsp'}">class="active"</c:if>><spring:message code='page.contacts.jsp' text="Contacts JSP"/></a></li>
            <li><a href='<c:url value="/jsp/contact"/>' <c:if test="${view eq 'contact-jsp'}">class="active"</c:if>><spring:message code='page.contact.jsp' text="Contact JSP"/></a></li>
            <li><a href='<c:url value="/html/contacts"/>' <c:if test="${view eq 'contacts-html'}">class="active"</c:if>><spring:message code='page.contacts.html' text="Contacts HTML"/></a></li>
            <li><a href='<c:url value="/html/contact"/>' <c:if test="${view eq 'contact-html'}">class="active"</c:if>><spring:message code='page.contact.html' text="Contact HTML"/></a></li>
            <li class="dropdown">
                <a href="javascript:void(0)" class="dropbtn"><spring:message code='page.language' text="Language"/></a>
                <div class="dropdown-content">
                    <a href='<c:out value="${url}"/>?lang=iw_IL'><img src='<c:url value="/resources/images/flags/iw.png"/>'>&nbsp;<span><spring:message code='lang.iw' text="Hebrew"/></span></a>
                    <a href='<c:out value="${url}"/>?lang=en_US'><img src='<c:url value="/resources/images/flags/en.png"/>'>&nbsp;<span><spring:message code='lang.en' text="English"/></span></a>
                </div>
            </li>
            <li class="dropdown">
                <a href="javascript:void(0)" class="dropbtn"><spring:message code='theme.change' text="Theme"/></a>
                <div class="dropdown-content">
                    <a href='<c:out value="${url}"/>?theme=spring'><img src='<c:url value="/resources/images/spring.png"/>'>&nbsp;<span><spring:message code='theme.spring' text="Spring"/></span></a>
                    <a href='<c:out value="${url}"/>?theme=wheat'><img src='<c:url value="/resources/images/wheat.png"/>'>&nbsp;<span><spring:message code='theme.wheat' text="Wheat"/></span></a>
                </div>
            </li>
            <li><a href='<%= application.getContextPath() + "/source/" + FilenameUtils.getBaseName(request.getParameter("url")) %>'><spring:message code='page.source' text="Source"/></a></li>

        </ul>
    </div>
