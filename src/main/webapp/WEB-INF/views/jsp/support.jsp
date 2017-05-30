<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="support.title" text="Support" /></title>
    <link rel="stylesheet" href="<spring:theme code='stylesheet'/>" type="text/css" />
    <script type="text/javascript" src='<c:url value="/resources/js/plugins/jquery-3.2.1.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/js/common.js"/>'></script>
    <link href='<c:url value="/static/images/favicon.ico"/>' type="image/x-icon" rel="shortcut icon">
</head>
<body style="direction: '<spring:message code='dir' text='ltr' />'" dir="<spring:message code='dir' text='ltr' />" data-date-pattern="<spring:message code='date.pattern' text='MM/dd/yyyy' />" data-language="${pageContext.response.locale.language}" data-context-path="<%=request.getContextPath()%>">
    <div class="wrapper">
        <jsp:include page='../jsp/include/navbar.jsp'>
           <jsp:param name="url" value="/html/home"/>
           <jsp:param name="view" value="support"/>
        </jsp:include>
        <div class="content">
            <h1><spring:message code="support.header" text="Support friendly error page" /> (JSP)</h1>

            <c:if test="${url ne null}"><p><b><spring:message code="support.page" text="Page" />:</b> <span>${url}</span></p></c:if>

            <c:if test="${timestamp ne null}"><p><b><spring:message code="support.occured" text="Occured" />:</b> <span>${timestamp}</span></p></c:if>

            <c:if test="${status ne null}"><p><b><spring:message code="support.status" text="Status" />:</b> <span>${status}</span></p></c:if>

            <p><spring:message code="support.contact" text="Application has encountered an error. Please contact support on ..." /></p>

            <p><spring:message code="support.notice" text="Support may ask you to right click to view page source." /></p>

            <!--
              // Hidden Exception Details  - this is not a recommendation, but here is
              // how you hide an exception in the page using JSP's
              -->
            <!--
            <c:out value="Failed URL: ${url}"/>
            <c:out value="Exception: ${exception.message}"/>
            <c:forEach var="ste" items="${exception.stackTrace}"><c:out value="${ste}"/></c:forEach>
            -->
        </div>
        <jsp:include page='../jsp/include/footer.jsp'/>
    </div>
</body>
</html>
