<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:baseHead title="Sign up" scriptLink="/js/registration.js"/>
<body>
<header>
    <%@include file="/WEB-INF/includes/anonNavbar.jsp" %>
</header>
<form:form modelAttribute="userForm" method='POST'>
            <div class="container h-100" id="reg-container">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                        <div class="card reg-card">
                            <div class="card-body p-5">
                                <h2 class="text-uppercase text-center mb-5">Create an account as student</h2>

                                <form:label class="form-label" path="name">Passport name since big letter</form:label>
                                <form:input path="name" type="text" id = "name-reg-student" class="form-control form-control-lg" name="name"
                                            placeholder="Name" aria-label="Username" pattern="[A-Z][a-z]{1,30}"  required="true" maxlength="31" aria-describedby="basic-addon1"/>
                                <form:errors path="name"  class="error-message" />

                                <form:label class="form-label" path="email">Email</form:label>
                                <form:input id="email-reg-student" class="form-control form-control-lg" name='email' required="true" placeholder="email@example.com"
                                          maxlength="76" type="email" path="email"/>
                                <form:errors path="email" class="error-message" /><br>

                                <form:label path="phone" class="form-label" for="phone-reg-student">Phone</form:label>
                                <form:input path="phone" type="text" id="phone-reg-student" class="form-control form-control-lg" required="true" name='phone' placeholder="89000000000"
                                       pattern="[0-9]{11}" minlength="11" maxlength="11"/>
                                <form:errors path="phone"  class="error-message" />


                                <div class="mb-4 pb-2">
                                    <label class="form-label" for="city-reg-tutor">City</label>
                                    <form:select path="city" id="city-reg-tutor" cssClass="form-select form-control-lg">
                                        <c:forEach items="${cities}" var="city" varStatus="status">
                                            <c:choose>
                                                <c:when test="${status.index == 0 && city == null}">
                                                    <form:option value="${city}" selected="true">${city.title}</form:option>
                                                </c:when>
                                                <c:when test="${city == city}">
                                                    <form:option value="${city}" selected="true">${city.title}</form:option>
                                                </c:when>
                                                <c:otherwise>
                                                    <form:option value="${city}">${city.title}</form:option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </form:select>
                                </div>


                                <form:label path="password" class="form-label" >Password</form:label>
                                <form:input type="password" path="password" required="true" placeholder="Password123" class="form-control form-control-lg" id="password" />
                                <form:errors path="password" class="error-message" />

                                <div>
                                    <input type='submit' class="gradient-btn" value='Sign in'>
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
