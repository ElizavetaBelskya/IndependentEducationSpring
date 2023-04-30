<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:baseHead title="Orders" scriptLink = "/js/orderEditor.js"/>
<body>
<header>
    <%@include file="/WEB-INF/includes/studentNavbar.jsp" %>
</header>

<c:if test="${ orders != null}">
<div id="carousel-orders" class="carousel slide" data-bs-ride="carousel">
    <div class="carousel-inner">
    </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carousel-orders" data-bs-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carousel-orders" data-bs-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
        </button>
    </div>
</div>
</c:if>

<div class = "no-container">
    <c:if test="${orders == null}">
        <t:infoTextWithButton text="You have not created any orders" action="Create" link="${spring:mvcUrl('SC#addOrderGet').build()}"/>
    </c:if>

</div>


</body>
</html>
