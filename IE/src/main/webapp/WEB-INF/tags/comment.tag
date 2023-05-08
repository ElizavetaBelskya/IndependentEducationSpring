<%@ tag description = "student comment to tutor" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="rate"%>
<%@ attribute name="comment"%>
<%@ attribute name="author"%>
<div class="review-card">
    <div class="author">${author}</div>
    <div class="rating">
        <div class="stars">
            <c:forEach var="i" begin="1" end="${rate}">
                <div class="star active"></div>
            </c:forEach>
            <c:forEach var="i" begin="${rate+1}" end="5">
                <div class="star"></div>
            </c:forEach>
        </div>
    </div>
    <div class="comment">${comment}</div>
</div>