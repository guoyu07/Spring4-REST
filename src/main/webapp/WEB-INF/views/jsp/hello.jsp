<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="title" text="Spring 4 MVC Web App Example with JSP" /></title>
    <link rel="stylesheet" href="<spring:theme code='stylesheet'/>" type="text/css" />
    <script type="text/javascript" src='<c:url value="/resources/js/plugins/jquery-3.2.1.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/js/hello.js"/>'></script>
    <link href='<c:url value="/static/images/favicon.ico"/>' type="image/x-icon" rel="shortcut icon">
</head>
<body style="direction: '<spring:message code='dir' text='ltr' />'" dir="<spring:message code='dir' text='ltr' />" data-date-pattern="<spring:message code='date.pattern' text='MM/dd/yyyy' />" data-language="${pageContext.response.locale.language}" data-context-path="<%=request.getContextPath()%>">
    <spring:message code='date.pattern' var="pattern" scope="page"/>
    <div class="wrapper">
        <jsp:include page='../jsp/include/navbar.jsp'>
           <jsp:param name="url" value="/jsp/hello"/>
           <jsp:param name="view" value="hello-jsp"/>
        </jsp:include>
        <div class="content">
            <h2><spring:message code="hello.jsp.title" text="Hello World, Spring JSP MVC" /></h2>
            <p id="greeting" data-url="<c:url value='/api/hello'/>" data-firstName="${firstName}" data-lastName="${lastName}">Welcome Static</p>
            <p><spring:message code="hello.locale" text="Current locale:" />&nbsp;${pageContext.response.locale}</p>
        </div>
        <jsp:include page='../jsp/include/footer.jsp'/>
    </div>
</body>
</html>