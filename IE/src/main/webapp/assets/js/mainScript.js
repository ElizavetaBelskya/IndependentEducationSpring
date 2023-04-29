document.addEventListener('DOMContentLoaded', function() {

    signIn = document.getElementById('sign-in-menu');
    back = document.getElementById('gray');

    if (signIn != null) {
        document.getElementById('nav-sign-in').addEventListener('click', function() {
            signIn.style.display = 'block';
            back.style.display = 'block';
        });

        back.addEventListener('click', function() {
            if (back.style.display == 'block') {
                back.style.display = 'none';
                signIn.style.display = 'none'
            }
        });


    }

    if (document.querySelector('.alert') != null) {
        signIn.style.display = 'block';
        back.style.display = 'block';
    }

    const emailInput = document.getElementById("Email1");
    const roleInputs = document.querySelectorAll('input[name="role"]');

    emailInput.addEventListener('input', function() {
        updateHiddenInput();
        return false;
    });

    for (let i = 0; i < roleInputs.length; i++) {
        roleInputs[i].addEventListener('input', function() {
            updateHiddenInput();
            return false;
        });
    }

    function updateHiddenInput() {
        const emailValue = emailInput.value;
        const roleValue = document.querySelector('input[name="role"]:checked').value;
        const hiddenInput = document.getElementById("emailAndRole");
        hiddenInput.value = `${emailValue}${roleValue}`;
        console.log(hiddenInput.value)
        return false;
    }



})


