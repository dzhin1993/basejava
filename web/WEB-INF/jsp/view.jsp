<%@ page import="model.*" %>
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
    <h1>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h1>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <p>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<model.SectionType, model.Section>"/>
            <c:set var="sectionName" value="<%=sectionEntry.getKey().getTitle()%>"/>
            <%SectionType sectionType = sectionEntry.getKey();%>
            <c:choose>
                <c:when test="<%=(sectionType == SectionType.PERSONAL)||(sectionType == SectionType.OBJECTIVE)%>">
                    <%TextSection textSection = (TextSection) sectionEntry.getValue();%>
                    <c:choose>
                        <c:when test="<%=textSection != TextSection.TEXT_EMPTY%>">
                            <tr>
                                <th><h3>${sectionName}</h3></th>
                                <th><%=textSection.getContent()%>
                                </th>
                            </tr>
                        </c:when>
                    </c:choose>
                </c:when>
                <c:when test="<%=(sectionType == SectionType.ACHIEVEMENT)||(sectionType == SectionType.QUALIFICATIONS)%>">
                    <%ListSection listSection = (ListSection) sectionEntry.getValue();%>
                    <c:choose>
                        <c:when test="<%=listSection != ListSection.LIST_EMPTY%>">
                            <tr>
                                <th><h3>${sectionName}</h3></th>
                                <th><%=String.join("<br/>", listSection.getContents())%>
                                </th>
                            </tr>
                        </c:when>
                    </c:choose>
                </c:when>
                <c:when test="<%=(sectionType == SectionType.EDUCATION)||(sectionType == SectionType.EXPERIENCE)%>">
                    <%CompanySection companySection = (CompanySection) sectionEntry.getValue();%>
                    <c:choose>
                        <c:when test="<%=companySection != CompanySection.COMPANY_EMPTY%>">
                            <tr>
                            <th><h3>${sectionName}</h3></th>
                            <c:forEach var="company" items="<%=companySection.getCompanies()%>">
                                <tr>
                                <th><h4>Компания: </h4></th>
                                <h4><a href="${company.link.name}"> ${company.link.name}</a></h4>
                                <c:forEach var="post" items="${company.postList}">
                                    <tr>
                                        <th> Должность: ${post.position}</th>
                                        <br/>
                                        <th> Описание: ${post.description}</th>
                                        <br/>
                                        <th> Продолжительность работы: ${post.startWork} - ${post.startWork}</th>
                                        <br/>
                                    </tr>
                                </c:forEach>
                                </tr>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                    </tr>
                </c:when>
            </c:choose>
        </c:forEach>
    </p>
    <button onclick="window.history.back()">Назад</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
