<%@ page contentType="text/html;charset=UTF-8" %>
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
    <h3><%=sectionEntry.getKey().getTitle() + ": "%><br/></h3>
    <c:choose>
        <c:when test="${sectionEntry.key.name().equals('PERSONAL') || sectionEntry.key.name().equals('OBJECTIVE')}">
            <ul>
                <li><%=sectionEntry.getValue().toString()%><br/></li>
            </ul>
        </c:when>
        <c:when test="${sectionEntry.key.name().equals('ACHIEVEMENT') || sectionEntry.key.name().equals('QUALIFICATIONS')}">
            <c:forEach var="section" items="${sectionEntry.value.get()}">
                <jsp:useBean id="section"
                             type="java.lang.String"/>
                <ul>
                    <li><%=section%><br/></li>
                </ul>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach var="org" items="${sectionEntry.value.get()}">
                <jsp:useBean id="org"
                             type="ru.javawebinar.basejava.model.Organization"/>
                <%=org.nameToHtml()%><br/>
                <c:forEach items="${org.getPeriods()}" var="period">
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
        </c:otherwise>
    </c:choose>
    </c:forEach>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
