<%@ page import="ru.javawebinar.basejava.model.ContactType" %>
<%@ page import="ru.javawebinar.basejava.model.SectionType" %>
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
    <c:forEach var="type" items="<%=SectionType.values()%>">
    <dl>
        <dt>${type.title}</dt>
    <c:choose>
        <c:when test="${type.name().equals('PERSONAL') || type.name().equals('OBJECTIVE')}">
            <dd><input type="text" name="${type.name()}" size=210 value="${resume.getSection(type).get()}"></dd>
        </c:when>
        <c:when test="${type.name().equals('ACHIEVEMENT') || type.name().equals('QUALIFICATIONS')}">
            <c:forEach var="section" items="${resume.getSection(type).get()}">
                <jsp:useBean id="section"
                             type="java.lang.String"/>
                <dd><input type="text" name="${type.name()}" size=210 value="${section}"></dd>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <dd><input type="text" name="${type.name()}" size=120 value="${resume.getSection(type).get()}"></dd>
        </c:otherwise>
    </c:choose>
    </dl>
    </c:forEach>

<%--<c:forEach var="sectionEntry" items="${resume.sections}">--%>
<%--    <jsp:useBean id="sectionEntry"--%>
<%--                 type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>--%>
<%--    <dl>--%>
<%--        <dt>${sectionEntry.getKey().getTitle()}</dt><br/>--%>
<%--        <c:choose>--%>
<%--            <c:when test="${sectionEntry.key.name().equals('PERSONAL') || sectionEntry.key.name().equals('OBJECTIVE')}">--%>
<%--                <dd><input type="text" name="${sectionEntry.key.name()}" size=210 value="${sectionEntry.getValue().toString()}"></dd>--%>
<%--            </c:when>--%>
<%--            <c:when test="${sectionEntry.key.name().equals('ACHIEVEMENT') || sectionEntry.key.name().equals('QUALIFICATIONS')}">--%>
<%--                <c:forEach var="section" items="${sectionEntry.value.get()}">--%>
<%--                    <jsp:useBean id="section"--%>
<%--                                 type="java.lang.String"/>--%>
<%--                    <dd><input type="text" name="${sectionEntry.key.name()}" size=210 value="${section}"></dd>--%>
<%--                </c:forEach>--%>
<%--            </c:when>--%>
<%--            <c:otherwise>--%>
<%--                <dd><input type="text" name="${sectionEntry.key.name()}" size=120 value="${sectionEntry.getValue().toString()}"></dd>--%>
<%--            </c:otherwise>--%>
<%--        </c:choose>--%>
<%--    </dl>--%>
<%--    </c:forEach>--%>
    </p>
    <hr>
    <button type="submit">Сохранить</button>
    <button onclick="window.history.back()">Отменить</button>
    </form>
    </section>
    <jsp:include page="fragments/footer.jsp"/>
    </body>
    </html>
