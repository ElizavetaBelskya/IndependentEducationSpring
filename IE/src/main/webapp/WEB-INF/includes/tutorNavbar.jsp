<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid">

        <a class="navbar-brand" href="${spring:mvcUrl('MC#login').build()}">
            <img src="<c:url value="/images/free-icon-studying-1903172.png"/>" alt="IE" width="40" height="40">
        </a>

        <a class="navbar-brand text-white" href="${spring:mvcUrl('MC#login').build()}">IndependentEducation</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active text-white" aria-current="page" href="${spring:mvcUrl('TC#getMyStudents').build()}">My students</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active text-white" aria-current="page" href="${spring:mvcUrl('TC#getOrders').build()}">New orders</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active text-white" aria-current="page" href="${spring:mvcUrl('TC#getProfile').build()}">My profile</a>
                </li>
            </ul>
        </div>

    </div>
</nav>
