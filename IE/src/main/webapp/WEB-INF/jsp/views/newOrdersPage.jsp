<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%--<%@ taglib prefix="mt" uri="myTags" %>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:baseHead title="New orders"/>

<body>
<header>
    <%@include file="/WEB-INF/includes/tutorNavbar.jsp" %>
</header>
<c:if test="${orders.size() > 0}">
<div id="order-row" class="row">
            <c:forEach var="i" begin="0" end="2">
                <c:if test="${orders.size() > i}">
                        <div class="col">
                            <div class="card" id="card-order">
                                <div class="card-body">
                                    <h5 class="card-title">${orders.get(i).getSubject()}</h5>
                                    <h3 class="card-text">${orders.get(i).getPrice()}</h3>
                                    <p class="card-text">${orders.get(i).getDescription()}</p>
                                </div>
                            </div>
                        </div>
                </c:if>
                </c:forEach>
<div/>

</c:if>

<c:if test="${orders.size() == 0}">
    <t:infoText bigText="There are no suitable orders for you" middleText="Wait for a while, please"/>
</c:if>


                <form action="" method="GET">
                    <div class="order-pagination">
                        <c:if test = "${countOfPages > 1}">
                            <nav aria-label="Page navigation example">
                                <ul class="pagination">
                                    <c:if test = "${page > 1}">
                                        <li class="page-item">
                                            <input type="submit" name="page" class="page-link" value="${page-1}">
                                        </li>
                                    </c:if>
                                    <input type="submit" name="page" class="page-link" value="${page}">
                                    <c:if test="${page < countOfPages}">
                                        <input type="submit" name="page" class="page-link" value="${page+1}">
                                    </c:if>
                                </ul>
                            </nav>
                        </c:if>
                    </div>
                </form>


</body>
</html>
