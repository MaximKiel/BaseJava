<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="static ru.javawebinar.basejava.model.SectionType.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.javawebinar.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <p>
            <c:forEach var="type" items="<%=ContactType.values()%>">
        <dl>
            <dt>${type.title}</dt>
            <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
        </dl>
        </c:forEach>
        </p>
        <h3>Секции:</h3>
        <p>
        <p>
        <c:forEach var="type" items="<%=SectionType.values()%>">
        <dl>
            <dt>${type.title}</dt>
            <c:choose>
                <c:when test="${type=='OBJECTIVE'}">
                    <input type='text' name='${type}' size=75 value='<%=((TextSection)resume.getSection(OBJECTIVE)).get()%>'>
                </c:when>

                <c:when test="${type=='PERSONAL'}">
                    <input type='text' name='${type}' size=75 value='<%=((TextSection)resume.getSection(PERSONAL)).get()%>'>
                </c:when>

                <c:when test="${type=='QUALIFICATIONS'}">
                    <textarea name='${type}' cols=75
                              rows=5><%=String.join("\n", ((ListSection) resume.getSection(QUALIFICATIONS)).get())%></textarea>
                </c:when>

                <c:when test="${type=='ACHIEVEMENT'}">
                    <textarea name='${type}' cols=75
                              rows=5><%=String.join("\n", ((ListSection) resume.getSection(ACHIEVEMENT)).get())%></textarea>
                </c:when>

                <c:when test="${type.name().equals('EXPERIENCE')}">
                    <dd><input type="text" name="${type.name()}" size=120 value="<%=((OrganizationSection)resume.getSection(EXPERIENCE)).get()%>"></dd>
                </c:when>

                <c:when test="${type.name().equals('EDUCATION')}">
                    <dd><input type="text" name="${type.name()}" size=120 value="<%=((OrganizationSection)resume.getSection(EDUCATION)).get()%>"></dd>
                </c:when>
            </c:choose>
        </dl>
        </c:forEach>


<%--            <c:forEach var="type" items="<%=SectionType.values()%>">--%>
<%--        <dl>--%>
<%--            <dt>${type.title}</dt>--%>
<%--            <c:choose>--%>
<%--                <c:when test="${type.name().equals('PERSONAL') || type.name().equals('OBJECTIVE')}">--%>
<%--                    <c:set var="textSection" value="${resume.getSection(type)}"/>--%>
<%--                    <jsp:useBean id="textSection" class="ru.javawebinar.basejava.model.TextSection"/>--%>
<%--                    <c:if test="${textSection != null}">--%>
<%--                        <dd><input type="text" name="${type.name()}" size=210 value="<%=textSection.get()%>"></dd>--%>
<%--                    </c:if>--%>
<%--                </c:when>--%>

<%--                <c:when test="${type.name().equals('ACHIEVEMENT') || type.name().equals('QUALIFICATIONS')}">--%>
<%--                    <c:set var="listSection" value="${resume.getSection(type)}"/>--%>
<%--                    <jsp:useBean id="listSection" class="ru.javawebinar.basejava.model.ListSection"/>--%>
<%--                    <c:if test="${listSection != null}">--%>
<%--                <textarea name='${type}' cols=75--%>
<%--                          rows=5><%=String.join("\n", ((ListSection) resume.getSection(ACHIEVEMENT)).get())%>--%>
<%--                </textarea>--%>
<%--                    </c:if>--%>
<%--                </c:when>--%>

<%--                <c:when test="${type.name().equals('EXPERIENCE') || type.name().equals('EDUCATION')}">--%>
<%--                    <c:set var="orgSection" value="${resume.getSection(type)}"/>--%>
<%--                    <jsp:useBean id="orgSection" class="ru.javawebinar.basejava.model.OrganizationSection"/>--%>
<%--                    <c:if test="${orgSection != null}">--%>
<%--                        <dd><input type="text" name="${type.name()}" size=120 value="<%=orgSection.get()%>"></dd>--%>
<%--                    </c:if>--%>
<%--                </c:when>--%>
<%--            </c:choose>--%>
<%--        </dl>--%>
<%--        </c:forEach>--%>
        </p>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
