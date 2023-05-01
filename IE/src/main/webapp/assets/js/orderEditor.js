async function updateOrders() {
    let result = await fetch(contextName + "/api/all", {
        method: 'GET'
    });

    let subjects = await fetch(contextName + "/api/subjects", {
        method: 'GET'
    }).then(response => response.json());

    if (result.status === 404) {
        const carousel = document.querySelector(".carousel");
        carousel.innerHTML = '';
        const editform = document.querySelector('.edit-order-form');
        editform.innerHTML = ''
        editform.style.display = 'none';
        const container = document.querySelector('.no-container');
        container.style.display = 'flex';
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

    const onlineRadios = document.querySelectorAll('input[name="online"]');
    for (let radio of onlineRadios) {
        if (radio.value === order.online) {
            radio.checked = true;
            break;
        }
    }

    const genderRadios = document.querySelectorAll('input[name="gender"]');
    for (let radio of genderRadios) {
        if (radio.value === order.tutorGender) {
            radio.checked = true;
            break;
        }
    }

    const rating = document.getElementById('rating');
    rating.checked = order.minRating >= 4.0;
    const description = document.getElementById('description');
    description.innerText = order.description
    const price = document.getElementById('price');
    price.value = order.price

    const subjectSelect = document.getElementById("subject-select");
    subjectSelect.innerHTML = '';
    subjects.forEach((subject, index) => {
        const option = document.createElement("option");
        option.text = subject.title;
        option.value = subject.title;
        subjectSelect.add(option);

        if (subject.title === order.subject) {
            subjectSelect.selectedIndex = index;
        }
    });

    let container = document.querySelector('.edit-order-form');
    container.style.display = 'flex';

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
            container.style.display = 'none';
            updateOrders();

        }

    });

}




document.addEventListener('DOMContentLoaded', function(){
    updateOrders();
});


