<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:baseHead title="My profile"/>
<body>
<header>
  <%@include file="/WEB-INF/includes/tutorNavbar.jsp" %>
</header>
<div class="container h-100" id="reg-container">
  <div class="row d-flex justify-content-center align-items-center h-100">
    <div class="col-12 col-md-9 col-lg-7 col-xl-6">
      <div class="card reg-card">
        <div class="card-body p-5">
          <h2 class="text-uppercase text-center mb-5">Tutor profile</h2>

          <div>
            <p>
              Name: ${account.name}
            </p>
            <p>
              Email: ${tutor.email}
            </p>
            <p>
              Phone: ${tutor.phone}
            </p>
            <p>
              City: ${account.city.title}
            </p>
            <p>
              Rating: ${tutor.rating}
            </p>
            <p>
              Gender: ${tutor.gender}
            </p>
            <p>
              Description: ${tutor.description}
            </p>
            <p>
              Total number of students: ${studentCount}
            </p>
            <p>
              Subjects:
              <ul>
              <c:forEach var="entry" items="${mapSubjectToAmount}">
                <li>${entry.key} - ${entry.value} students</li>
              </c:forEach>
          </ul>
            </p>
          </div>
          <div class="button-row">
            <form method="GET" action="<c:url value='/logout'/>">
              <input type='submit' class="gradient-btn" value='Log out'/>
            </form>

            <form method="POST" action="${spring:mvcUrl('TC#deleteProfile').build()}">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
              <input type='submit' class="gradient-btn" value='Delete account'/>
            </form>

          </div>

          <form method="POST" action="#">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="form-floating form-description">
              <input type="text" name="description" class="form-control" placeholder="Leave a comment here" id="floatingTextarea2" value="${tutor.description}"/>
              <label for="floatingTextarea2">Profile description</label>
            </div>
            <input type='submit' class="gradient-btn" value='Update description'/>
          </form>

        </div>
      </div>
    </div>
  </div>
</div>


<c:if test="${reviewsList != null}">
  <c:forEach var="review" items="${reviewsList}">
    <t:comment author="Student #${review.student.id}" rate="${review.rating}" comment="${review.comment}"/>
  </c:forEach>
</c:if>


</body>
</html>

