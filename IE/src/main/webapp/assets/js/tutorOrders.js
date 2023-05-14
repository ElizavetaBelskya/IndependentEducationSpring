function getCookieValue(cookieName) {
    const cookieArray = document.cookie.split(';');
    for (let i = 0; i < cookieArray.length; i++) {
        const cookie = cookieArray[i].trim();
        if (cookie.startsWith(cookieName + '=')) {
            return cookie.substring(cookieName.length + 1);
        }
    }
    return null;
}


document.addEventListener('DOMContentLoaded', function() {
    const cardBodies = document.querySelectorAll(".card-body");
    const csrfToken = getCookieValue("XSRF-TOKEN");
    const headers = new Headers();
    headers.append('X-XSRF-TOKEN', csrfToken);

    if (cardBodies) {
        cardBodies.forEach(function(cardBody) {
            const btn = cardBody.querySelector('.card-btn');
            const orderId = cardBody.querySelector('.order-id').value;
            if (btn && orderId) {
                btn.addEventListener('click', async function(e) {
                    e.preventDefault();
                    const result = await fetch(contextName + "/api/orders/" + orderId, {
                        method: 'PATCH', headers: headers
                    });
                    if (result.ok) {
                        btn.value = 'Taken';
                    }
                });
            }
        });
    }
});

