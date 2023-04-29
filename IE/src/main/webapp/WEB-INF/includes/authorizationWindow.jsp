<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div id="sign-in-menu">
<form:form action='' method='POST' modelAttribute="loginForm">
        <div class="sign-in-form">
            <form:label path="email" class="form-label">Email address</form:label>
            <form:input path="email" required="true" class="form-control" id="Email1" placeholder="email@example.com"
                        pattern="[A-Za-z0-9-]{2,50}@[a-z]{2,20}.[a-z]{2,4}" value="${email}"/>
        </div>
        <div class="sign-in-form">
            <form:label path="password" class="form-label">Password</form:label>
            <form:password path="password" required="true" class="form-control" id="Password1"
                           pattern="[A-Za-z0-9]{2,50}" title="The password is in incorrect format" placeholder="Password" value=""/>
        </div>

        <div>
            <form:radiobutton path="role" value="_____Tutor" id="radio1" checked="checked" />
            <form:label for="radio1" class="form-check-label" path="role">Tutor</form:label>
            <form:radiobutton path="role" value="_____Student" id="radio2" />
            <form:label for="radio2" class="form-check-label" path="role">Student</form:label>
        </div>

        <form:hidden path="emailAndRole" id="emailAndRole"/>

        <input type="submit" class="btn-login" id="btn-login" value='Sign in' />

        <c:if test="${error != null}">
            <div class="alert alert-warning" role="alert">${error}</div>
        </c:if>

</form:form>
</div>