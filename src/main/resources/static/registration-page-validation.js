document.addEventListener("DOMContentLoaded", function () {
   const usernameField = document.getElementById("username");
   const usernameError = document.getElementById("invalidUsernameError");
   const password = document.getElementById("password");
   const repeatPassword = document.getElementById("repeat-password");
   const form = document.getElementById("registrationForm");
   const errorMessage = document.getElementById("passwordAlert");
   const EMAIL_REGEXP = /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/iu;
   const usernameFormatError = document.getElementById("invalidUsernameFormat");

   function isEmailValid(value) {
      return EMAIL_REGEXP.test(value);
   }

   async function checkUsername() {
      const username = usernameField.value;
      usernameField.classList.remove("is-invalid");
      usernameError.style.display = "none";
      usernameFormatError.style.display = "none";

      if (isEmailValid(username)) {
         try {
            const response = await fetch('/registration', {
               method: 'POST',
               headers: {
                  'Content-Type': 'application/json',
               },
               body: JSON.stringify({
                  login: username,
                  password: password.value,
                  repeatPassword: repeatPassword.value,
               })
            });
            if (response.status === 409) {
               usernameField.classList.add("is-invalid");
               usernameError.style.display = "block";
               return false;
            } else {
               usernameField.classList.remove("is-invalid");
               usernameError.style.display = "none";
               return true;
            }
         } catch (error) {
            console.log(`Error with username ${usernameField}`, error);
            return false;
         }
      } else {
         usernameField.classList.add("is-invalid");
         usernameFormatError.style.display = "block";
         return false;
      }
   }

   function checkPasswords() {
      const passwordValue = password.value;
      const repeatPasswordValue = repeatPassword.value;

      password.classList.remove("is-invalid");
      repeatPassword.classList.remove("is-invalid");

      if (passwordValue && repeatPasswordValue && passwordValue !== repeatPasswordValue) {
         errorMessage.style.display = "block";
         return false;
      } else {
         errorMessage.style.display = "none";
         return true;
      }
   }

   function checkLength() {
      const passwordValue = password.value;
      if (passwordValue.length <= 4) {
         password.classList.add("is-invalid");
         return false;
      } else {
         password.classList.remove("is-invalid");
         return true;
      }
   }

   form.addEventListener('submit', async function (event) {
      event.preventDefault();

      const passwordsValid = checkPasswords();
      if (!passwordsValid) return;
      const lengthValid = checkLength();
      if (!lengthValid) return;
      const usernameValid = await checkUsername();

      if (usernameValid && passwordsValid && lengthValid) {
         form.submit();
      }
   });
});
