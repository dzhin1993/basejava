<%@ page import="model.ContactType" %>
<%@ page import="model.ListSection" %>
<%@ page import="model.CompanySection" %>
<%@ page import="model.TextSection" %>
<%@ page import="model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <h1>Имя:</h1>
        <dl>
            <input type="text" name="fullName" size=55 value="${resume.fullName}">
        </dl>
        <h2>Контакты:</h2>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h2>Секции:</h2>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.getSection(type)}"/>
            <jsp:useBean id="section" type="model.Section"/>
            <c:choose>
                <c:when test="${type=='PERSONAL'|| type=='OBJECTIVE'}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><input type="text" name="${type.name()}" size=30 value="<%=((TextSection)section).getContent()%>"></dd>
                    </dl>
                </c:when>
                <c:when test="${type=='ACHIEVEMENT'|| type=='QUALIFICATIONS'}">
                    <dl>
                        <dt>${type.title}</dt>
                        <dd><textarea rows="3" cols="30"><%=String.join("\n", ((ListSection)section).getContents())%></textarea></dd>
                    </dl>
                </c:when>
                <c:when test="${type=='EXPERIENCE'|| type=='EDUCATION'}">
                    <dl>
                        <dt><h2>${type.title}</h2></dt><br/>
                        <c:forEach var="company" items="<%=((CompanySection)section).getCompanies()%>">
                            <dl>
                                <dt>Компания</dt>
                                <dd><input type="text" name="company" size=30 value="${company.link.name}"></dd>
                            </dl>
                            <dl>
                                <dt>Ссылка url</dt>
                                <dd><input type="text" name="url" size=30 value="${company.link.url}"></dd>
                            </dl>
                            <h4>Список должностей:</h4>
                            <c:forEach var="post" items="${company.postList}">
                                <dl>
                                    <dt>Должность</dt>
                                    <dd><input type="text" name="position" size=30 value="${post.position}"></dd>
                                </dl>
                                <dl>
                                    <dt>Описание</dt>
                                    <dd><input type="text" name="description" size=30 value="${post.description}"></dd>
                                </dl>
                                <dl>
                                    <dt>Начало работы</dt>
                                    <dd><input type="text" name="startWork" size=30 value="${post.startWork}"></dd>
                                </dl>
                                <dl>
                                    <dt>Конец работы</dt>
                                    <dd><input type="text" name="endWork" size=30 value="${post.endWork}"></dd>
                                </dl>
                                <br/>
                            </c:forEach>
                        </c:forEach>
                    </dl>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
