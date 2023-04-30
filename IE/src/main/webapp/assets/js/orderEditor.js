async function updateOrders() {
    let result = await fetch(contextName + "/api/all", {
        method: 'GET'
    });

    let subjects = await fetch(contextName + "/api/subjects", {
        method: 'GET'
    }).then(response => response.json());

    console.log(subjects)

    if (result.status === 404) {
        let carousel = document.querySelector(".carousel");
        carousel.remove();
        let container = document.querySelector('.no-container');
        container.innerHTML = '<div class = "empty-list"> ' +
            '<h2 class="empty-list-text">You have not created any orders</h2>' +
        '<form action="'+ contextName  + "/new_order" + '" method="GET">' +
            '<input type="submit" value="Create" class="btn btn-outline-info"/>' +
    '</form></div>'

    }

    if (result.ok) {
        let orders = await result.json();

        const container = document.querySelector(".carousel-inner");
        container.innerHTML = '';


        for (let i = 0; i < orders.length; i += 3) {
            const item = document.createElement("div");
            item.classList.add("carousel-item");
            if (i === 0) {
                item.classList.add("active");
            }
            const row = document.createElement("div");
            row.classList.add("row");

            for (let j = i; j < i + 3 && j < orders.length; j++) {
                order = orders[j];
                console.log("ОРДЕР" + order.id)
                const col = document.createElement("div");
                col.classList.add("col");
                let card = document.createElement("div");
                card.classList.add("card", "order-card-for-delete");
                card.innerHTML = `
                <div class="card-body">
                    <h6 class="card-subtitle mb-2 text-muted">Order #${order.id}</h6>
                    <h5 class="card-title order-card-editable">${order.subject}</h5>
                    <h6 class="card-subtitle mb-2 text-muted">${order.description}</h6>
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item">Price: ${order.price}</li>
                        <li class="list-group-item">${order.online}</li>
                        <li class="list-group-item">${order.tutor == null ? "We are looking for a tutor" : "The order is closed"}</li>
                    </ul>
                    
                    <input type="hidden" class="order-id" value="${order.id}">
                    <input type="submit" value="Edit" class="btn btn-outline-info edit-btn"/>
                    <input type="submit" value="Delete" class="btn btn-outline-info delete-btn"/>
                </div>
            `;
                let deleteBtn = card.querySelector('.delete-btn')
                let updateBtn = card.querySelector('.edit-btn')
                updateBtn.addEventListener('click', (function(order) {
                    return function() {
                        createOrderForm(order, subjects);
                    }
                })(order));


                let orderId = card.querySelector(".order-id").value;
                deleteBtn.addEventListener("click", async function (e) {
                    e.preventDefault();
                    let result = await fetch(contextName + "/api/" + orderId, {
                        method: 'DELETE'
                    });

                    if (result.ok) {
                        console.log("Order deleted");
                        updateOrders();
                    }
                });

                col.appendChild(card);
                row.appendChild(col);
            }
            item.appendChild(row);
            container.appendChild(item);

        }

    }
}


function createOrderForm(order, subjects) {
    let editorContainer = `
    <div class="edit-order-form">
  <div class="form-group">
    <label for="subject-select" class="form-label">Subject:</label>
    <select id="subject-select" name="subject" class="form-control">
    </select>
  </div>
  
  <div class="form-group">
    <label class="form-label">Online:</label>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" id="online" name="online" value="ONLINE" ${order.online === 'ONLINE' ? 'checked' : ''}>
      <label class="form-check-label" for="online">Online</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" id="offline" name="online" value="OFFLINE" ${order.online === 'OFFLINE' ? 'checked' : ''}>
      <label class="form-check-label" for="offline">Offline</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" id="both" name="online" value="BOTH" ${order.online === 'BOTH' ? 'checked' : ''}>
      <label class="form-check-label" for="both">Both</label>
    </div>
  </div>

  <div class="form-group">
    <label class="form-label">Tutor Gender:</label>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" id="male" name="gender" value="MALE" ${order.tutorGender === 'MALE' ? 'checked' : ''}>
      <label class="form-check-label" for="male">Male</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" id="female" name="gender" value="FEMALE" ${order.tutorGender === 'FEMALE' ? 'checked' : ''}>
      <label class="form-check-label" for="female">Female</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" id="both" name="gender" value="BOTH" ${order.tutorGender === 'BOTH' ? 'checked' : ''}>
      <label class="form-check-label" for="both">Both</label>
    </div>
  </div>

  <div class="form-group">
    <div class="form-check">
      <input class="form-check-input" type="checkbox" id="rating" name="rating" ${parseFloat(order.minRating) >= 4.0 ? 'checked' : ''}>
      <label class="form-check-label" for="rating">Only with a rating greater than 4.0</label>
    </div>
  </div>

  <div class="form-group">
    <label for="description" class="form-label">Description:</label>
    <textarea id="description" name="description" maxLength="120" class="form-control">${order.description}</textarea>
  </div>

  <div class="form-group">
    <label for="price" class="form-label">Price:</label>
    <input type="number" id="price" value="${order.price}" name="price" min="100" max="10000"/>
  </div>  

 <input type="submit" class="btn btn-primary btn-edit-this" value="Edit">  
  </div>

`;

    let container = document.querySelector('.no-container');
    container.innerHTML = '';
    container.innerHTML = editorContainer;

    const subjectSelect = document.getElementById("subject-select");
    subjects.forEach((subject, index) => {
        const option = document.createElement("option");
        option.text = subject.title;
        option.value = subject.title;
        subjectSelect.add(option);

        if (subject === order.subject) {
            subjectSelect.selectedIndex = index;
        }
    });

    const editButton = document.querySelector('.btn-edit-this');
    editButton.addEventListener('click', async (event) => {
        event.preventDefault();
        const subject = document.getElementById('subject-select').value;
        const online = document.querySelector('input[name="online"]:checked').value;
        const gender = document.querySelector('input[name="gender"]:checked').value;
        const minRating = document.getElementById('rating').checked ? 4.0 : 0.0;
        const description = document.getElementById('description').value;
        const price = document.getElementById('price').value;

        const updatedOrder = {
            subject,
            online,
            gender,
            rating: minRating,
            description,
            price
        };

        let result = await fetch(contextName + "/api/" + order.id, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedOrder)
        });


        if (result.ok) {
            updateOrders();
            container.innerHTML = '';
        }

    });

}




document.addEventListener('DOMContentLoaded', function(){
    updateOrders();
});


