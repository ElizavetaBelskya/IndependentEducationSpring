document.addEventListener('DOMContentLoaded', function() {
    const cardBodies = document.querySelectorAll(".card-body");

    if (cardBodies) {
        cardBodies.forEach(function(cardBody) {
            const btn = cardBody.querySelector('.card-btn');
            const orderId = cardBody.querySelector('.order-id').value;

            if (btn && orderId) {
                btn.addEventListener('click', async function(e) {
                    e.preventDefault();
                    const result = await fetch(contextName + "/api/" + orderId, {
                        method: 'PATCH'
                    });
                    if (result.ok) {
                        btn.value = 'Taken';
                    }
                });
            }
        });
    }
});

