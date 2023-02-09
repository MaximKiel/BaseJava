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
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" required></dd>
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
                    <input type='text' name='${type}' size=210 value='<%=((TextSection)resume.getSection(OBJECTIVE)).get()%>'>
                </c:when>

                <c:when test="${type=='PERSONAL'}">
                    <input type='text' name='${type}' size=210 value='<%=((TextSection)resume.getSection(PERSONAL)).get()%>'>
                </c:when>

                <c:when test="${type=='QUALIFICATIONS'}">
                    <textarea name='${type}' cols=210
                              rows=10><%=String.join("\n", ((ListSection) resume.getSection(QUALIFICATIONS)).get())%></textarea>
                </c:when>

                <c:when test="${type=='ACHIEVEMENT'}">
                    <textarea name='${type}' cols=210
                              rows=10><%=String.join("\n", ((ListSection) resume.getSection(ACHIEVEMENT)).get())%></textarea>
                </c:when>

                <c:when test="${type.name().equals('EXPERIENCE')}">
                    <c:forEach var="orgExp" items="<%=((OrganizationSection)resume.getSection(EXPERIENCE)).get()%>"
                    varStatus="counter">
                        <br/>
                        <jsp:useBean id="orgExp" type="ru.javawebinar.basejava.model.Organization"/>
                        <dd>Название: <input type="text" name="${type.name()}" size=50 value="<%=orgExp.getName()%>"></dd><br/>
                        <dd>Сайт: <input type="text" name="${type.name()}" size=50 value="<%=orgExp.getWebsite()%>"></dd><br/>
                        <div style="margin-left: 25px">
                            <c:forEach var="periodExp" items="${orgExp.periods}">
                                <jsp:useBean id="periodExp" type="ru.javawebinar.basejava.model.Organization.Period"/>
                                <dd>Начало периода: <input type="text" name="${type.name()}" size=50 value="<%=periodExp.getStartDate()%>"></dd><br/>
                                <dd>Окончание периода: <input type="text" name="${type.name()}" size=50 value="<%=periodExp.getEndDate()%>"></dd><br/>
                                <dd>Подзаголовок: <input type="text" name="${type.name()}" size=100 value="<%=periodExp.getTitle()%>"></dd><br/>
                                <dd>Описание: <input type="text" name="${type.name()}" size=175 value="<%=periodExp.getDescription()%>"></dd><br/>
                            </c:forEach>
                        </div>
                    </c:forEach>
                    <br/>
                </c:when>

                <c:when test="${type.name().equals('EDUCATION')}">
                    <c:forEach var="orgEdu" items="<%=((OrganizationSection)resume.getSection(EDUCATION)).get()%>">
                        <br/>
                        <jsp:useBean id="orgEdu" type="ru.javawebinar.basejava.model.Organization"/>
                        <dd>Название: <input type="text" name="${type.name()}" size=50 value="<%=orgEdu.getName()%>"></dd><br/>
                        <dd>Сайт: <input type="text" name="${type.name()}" size=50 value="<%=orgEdu.getWebsite()%>"></dd><br/>
                        <c:forEach var="periodEdu" items="${orgEdu.periods}">
                            <jsp:useBean id="periodEdu" type="ru.javawebinar.basejava.model.Organization.Period"/>
                            <dd>Начало периода: <input type="text" name="${type.name()}" size=50 value="<%=periodEdu.getStartDate()%>"></dd><br/>
                            <dd>Окончание периода: <input type="text" name="${type.name()}" size=50 value="<%=periodEdu.getEndDate()%>"></dd><br/>
                            <dd>Подзаголовок: <input type="text" name="${type.name()}" size=100 value="<%=periodEdu.getTitle()%>"></dd><br/>
                            <dd>Описание: <input type="text" name="${type.name()}" size=175 value="<%=periodEdu.getDescription()%>"></dd><br/>
                        </c:forEach>
                    </c:forEach>
                    <br/>
                </c:when>
            </c:choose>
        </dl>
        </c:forEach>
        </p>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
