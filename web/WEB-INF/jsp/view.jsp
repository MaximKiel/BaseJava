<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="ru.javawebinar.basejava.model.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<ru.javawebinar.basejava.model.SectionType, ru.javawebinar.basejava.model.AbstractSection>"/>
            <c:set var="type" value="${sectionEntry.key}"/>

        <c:if test="${sectionEntry.value != '' && sectionEntry.value != '[]'}">
    <h3>${type.title}<br/></h3>
    </c:if>

    <c:choose>
        <c:when test="${(type.name().equals('PERSONAL') || type.name().equals('OBJECTIVE')) && sectionEntry.value != ''}">
            <ul>
                <li><%=((TextSection) sectionEntry.getValue()).get()%><br/></li>
            </ul>
        </c:when>

        <c:when test="${(type.name().equals('ACHIEVEMENT') || type.name().equals('QUALIFICATIONS')) && sectionEntry.value != '[]'}">
            <c:forEach var="list" items="<%=((ListSection) sectionEntry.getValue()).get()%>">
                <ul>
                    <li>${list}</li>
                </ul>
            </c:forEach>
        </c:when>

        <c:when test="${(type.name().equals('EXPERIENCE') || type.name().equals('EDUCATION'))}">
            <c:forEach var="org" items="<%=((OrganizationSection) sectionEntry.getValue()).get()%>">
                <jsp:useBean id="org"
                             type="ru.javawebinar.basejava.model.Organization"/>
                <%=org.nameToHtml()%><br/>

                <c:forEach var="period" items="${org.getPeriods()}">
                    <jsp:useBean id="period"
                                 type="ru.javawebinar.basejava.model.Organization.Period"/>
                    <tags:localDate date="${period.startDate}"/> -
                    <c:choose>
                        <c:when test="${period.endDate.isAfter(LocalDate.now())}">
                            Сейчас
                        </c:when>
                        <c:otherwise>
                            <tags:localDate date="${period.endDate}"/>
                        </c:otherwise>
                    </c:choose>
                    <b><c:out value="${period.title}"/></b><br/>
                    <c:if test="${period.description != ''}">
                        <c:out value="${period.description}"/><br/>
                    </c:if>
                </c:forEach>
            </c:forEach>
        </c:when>
    </c:choose>
    </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
