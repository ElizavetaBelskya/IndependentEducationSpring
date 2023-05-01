<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:baseHead title="Orders" scriptLink = "/js/orderEditor.js"/>
<body>
<header>
    <%@include file="/WEB-INF/includes/studentNavbar.jsp" %>
</header>

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

<div class = "no-container">
        <t:infoTextWithButton text="You have not created any orders" action="Create" link="${spring:mvcUrl('SC#addOrderGet').build()}"/>
</div>


<div class="edit-order-form">
    <div class="form-group">
        <label for="subject-select" class="form-label">Subject:</label>
        <select id="subject-select" name="subject" class="form-control">
        </select>
    </div>

    <div class="form-group">
        <label class="form-label">Online:</label>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="online" name="online" value="ONLINE">
            <label class="form-check-label" for="online">Online</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="offline" name="online" value="OFFLINE">
            <label class="form-check-label" for="offline">Offline</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="format-both" name="online" value="BOTH">
            <label class="form-check-label" for="format-both">Both</label>
        </div>
    </div>

    <div class="form-group">
        <label class="form-label">Tutor Gender:</label>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="male" name="gender" value="MALE">
            <label class="form-check-label" for="male">Male</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="female" name="gender" value="FEMALE" >
            <label class="form-check-label" for="female">Female</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" id="gender-both" name="gender" value="BOTH" >
            <label class="form-check-label" for="gender-both">Both</label>
        </div>
    </div>

    <div class="form-group">
        <div class="form-check">
            <input class="form-check-input" type="checkbox" id="rating" name="rating">
            <label class="form-check-label" for="rating">Only with a rating greater than 4.0</label>
        </div>
    </div>

    <div class="form-group">
        <label for="description" class="form-label">Description:</label>
        <textarea id="description" name="description" maxLength="120" class="form-control"></textarea>
    </div>

    <div class="form-group">
        <label for="price" class="form-label">Price:</label>
        <input type="number" id="price" value="" name="price" min="100" max="10000"/>
    </div>

    <input type="submit" class="btn btn-primary btn-edit-this" value="Edit">
</div>


</body>
</html>
