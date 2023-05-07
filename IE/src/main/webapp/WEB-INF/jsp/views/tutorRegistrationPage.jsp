<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:baseHead title="Sign up" scriptLink="/js/registration.js"/>
<body>
<header>
    <%@include file="/WEB-INF/includes/anonNavbar.jsp" %>
</header>

<form:form modelAttribute="userForm" method='POST'>
    <div class="container py-5 h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12">
                <div class="card card-registration card-registration-2">
                    <div class="card-body p-0">
                        <div class="row g-0">
                            <div class="col-lg-6">
                                <div class="p-5">
                                    <h3 class="fw-normal mb-5" id="title-reg-tutor">General Infomation</h3>

                                    <div class="row">
                                        <div class="col-md-6 mb-4 pb-2">

                                            <form:label class="form-label"
                                                        path="name">Passport name since big letter</form:label>
                                            <form:input path="name" type="text" id="name-reg-student"
                                                        class="form-control form-control-lg" name="name"
                                                        placeholder="Name" aria-label="Username"
                                                        pattern="[A-Z][a-z]{1,30}" maxlength="31"
                                                        aria-describedby="basic-addon1"/>
                                            <form:errors path="name" class="error-message"/>


                                        </div>

                                        <form:label class="form-label" path="email">Email</form:label>
                                        <form:input id="email-reg-student" class="form-control form-control-lg"
                                                    name='email' placeholder="email@example.com"
                                                    pattern="[A-Za-z0-9-]{2,50}@[a-z]{2,20}.[a-z]{2,4}"
                                                    maxlength="76" type="email" path="email"/>
                                        <form:errors path="email" class="error-message"/><br>

                                    </div>

                                    <div class="col-md-6 mb-4 pb-2">

                                        <form:label path="phone" class="form-label"
                                                    for="phone-reg-student">Phone</form:label>
                                        <form:input path="phone" type="text" id="phone-reg-student"
                                                    class="form-control form-control-lg" name='phone'
                                                    placeholder="89000000000"
                                                    pattern="[0-9]{11}" minlength="11" maxlength="11"/>
                                        <form:errors path="phone" class="error-message"/>

                                    </div>

                                    <div class="mb-4 pb-2">
                                        <label class="form-label" for="city-reg-tutor">City</label>
                                        <form:select path="city" id="city-reg-tutor"
                                                     cssClass="form-select form-control-lg">
                                            <c:forEach items="${cities}" var="city" varStatus="status">
                                                <c:choose>
                                                    <c:when test="${status.index == 0 && city == null}">
                                                        <form:option value="${city}"
                                                                     selected="true">${city.title}</form:option>
                                                    </c:when>
                                                    <c:when test="${city == city}">
                                                        <form:option value="${city}"
                                                                     selected="true">${city.title}</form:option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form:option value="${city}">${city.title}</form:option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </form:select>
                                    </div>

                                    <div class="mb-4 pb-2">
                                        <form:label path="password" class="form-label">Password</form:label>
                                        <form:input type="password" path="password" placeholder="Password123"
                                                    class="form-control form-control-lg" id="password"/>
                                        <form:errors path="password" class="error-message"/>
                                    </div>

                                    <div class="form-check">
                                        <form:radiobutton path="gender" value="1" checked="true"
                                                          class="form-check-input"/>
                                        <label class="form-check-label">Male</label>
                                    </div>
                                    <div class="form-check">
                                        <form:radiobutton path="gender" value="0" class="form-check-input"/>
                                        <label class="form-check-label">Female</label>
                                    </div>


                                    <div class="form-check">
                                        <form:checkbox path="isWorkingOnline" id="onlineCheckbox"
                                                       class="form-check-input"/>
                                        <label class="form-check-label" for="onlineCheckbox">Working online</label>
                                    </div>


                                </div>
                            </div>
                            <div class="col-lg-6 bg-indigo text-white">
                                <div class="p-5">
                                    <h3 class="fw-normal mb-5">Subjects</h3>


                                    <c:if test="${not empty subjects}">
                                        <c:forEach var="subject" items="${subjects}" varStatus="status">
                                            <div class="form-check">
                                                <form:checkbox path="subjects" value="${subject}" id="subject_${status.index}" class="form-check-input"/>
                                                <label class="form-check-label" for="subject_${status.index}">${subject.title}</label>
                                            </div>
                                        </c:forEach>
                                    </c:if>

                                    <input type='submit' class="gradient-btn" value='Sign in'>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</form:form>

<c:if test="${message != null}">
    <t:modal answer="${message}" answerTitle="${message}"/>
</c:if>

</body>
</html>

