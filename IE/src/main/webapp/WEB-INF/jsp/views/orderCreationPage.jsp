<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:baseHead title="Create order" scriptLink="/js/orderCreationScript.js"/>
<body>
<header>
    <%@include file="/WEB-INF/includes/studentNavbar.jsp" %>
</header>
<div class="container">
    <div class="row main-form">
        <div class="card" id="order-creation-card" >
            <div class="card-body">
                <h5 class="card-title">Please, fill in all fields</h5>

                <form:form method="POST" modelAttribute="order">

                    <form:label class="form-label" path="subject">Subject</form:label>
                    <form:select path="subject" items="${subjects}"/>
                    <br>


                    <form:label class="form-label" path="online">Online</form:label>
                    <form:radiobutton path="online" value="ONLINE"/>

                    <form:label class="form-label" path="online">Offline</form:label>
                    <form:radiobutton path="online" value="OFFLINE"/>

                    <form:label class="form-label" path="online">Both</form:label>
                    <form:radiobutton path="online" value="BOTH"/>
                    <br>


                    <form:label class="form-label" path="gender">Male</form:label>
                    <form:radiobutton path="gender" value="MALE"/>

                    <form:label class="form-label" path="gender">Female</form:label>
                    <form:radiobutton path="gender" value="FEMALE"/>

                    <form:label class="form-label" path="gender">Both</form:label>
                    <form:radiobutton path="gender" value="BOTH"/>
                    <br>


                    <form:label class="form-label" path="rating">
                        Only with a rating greater than 4.0
                    </form:label>
                    <form:checkbox path="rating"/>
                    <form:errors path="rating" />
                    <br>

                    <form:label path="description">
                        Description
                    </form:label>
                    <br>
                    <form:textarea maxLength="120" path="description"/>
                    <form:errors path="description" />
                    <br>

                    <form:label path="price"/>
                    <form:input type="number" min="100" max="10000" path="price"/>
                    <form:errors path="price" />
                    <br>

                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <input type="submit" class="btn btn-primary" value='Create' />

                </form:form>


            </div>
        </div>

    </div>
</div>


<c:if test="${answer != null}">
    <t:modal answer="${answer}" answerTitle = "${answerTitle}"/>
</c:if>

</div>

</body>
</html>


