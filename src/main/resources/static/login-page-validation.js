document.addEventListener("DOMContentLoaded", function () {
   const username = document.getElementById("username");
   const password = document.getElementById("password");
   const error_message = document.getElementById("somethingWrong");
   const form = document.getElementById("login-form");
   const EMAIL_REGEXP = /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/iu;
   const usernameFormatError = document.getElementById("invalidUsernameFormat");

    function isEmailValid(value) {
        return EMAIL_REGEXP.test(value);
    }


   async function checkFormData() {
       const usernameValue = username.value;
       const passwordValue = password.value;

       username.classList.remove("is-invalid");
       usernameFormatError.style.display = "none";
       error_message.style.display = "none";

       if (!isEmailValid(usernameValue)) {
           username.classList.add("is-invalid");
           usernameFormatError.style.display = "block";
           return false;
       }
       try {
           const response = await fetch('/login', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/json',
               },
               body: {
                   login: usernameValue,
                   password: passwordValue
               }
           });
           if (response.status === 404 || response.status === 400) {
               error_message.style.display = 'block';
               return false;
           } else {
               error_message.style.display = 'none';
               return true;
           }
       } catch (error) {
           console.log('Error with login', error);
       }
   }

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formDataValid = await checkFormData();

        if (formDataValid) {
            form.submit();
        }
    });
});