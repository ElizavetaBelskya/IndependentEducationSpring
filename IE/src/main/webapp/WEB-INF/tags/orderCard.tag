<%@ tag description = "information placeholder with button" pageEncoding="utf-8"%>
<%@ attribute name="order" type="ru.kpfu.itis.belskaya.models.Order" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card-body">
    <h6 class="card-subtitle mb-2 text-muted">${"Order #".concat(order.id.toString())}</h6>
    <h5 class="card-title order-card-editable">${order.subject}</h5>
    <h6 class="card-subtitle mb-2 text-muted">${order.description}</h6>
    <ul class="list-group list-group-flush">
        <li class="list-group-item">Price: ${order.price}</li>
        <li class="list-group-item">
            ${order.online}
        </li>
        <li class="list-group-item">
            ${order.tutor == null? "We are looking for a tutor": "The order is closed"}
        </li>
    </ul>
    <p class="card-text">
        ${order.creationDate}
    </p>

    <input type="hidden" class="order-id" value="${order.id}">
    <input type="submit" value="Edit" class="btn btn-outline-info edit-btn"/>
    <input type="submit" value="Delete" class="btn btn-outline-info delete-btn"/>

</div>