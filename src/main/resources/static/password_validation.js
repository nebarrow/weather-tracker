const password = document.getElementById("password")
const repeatPassword = document.getElementById("repeat-password")

document.getElementById("registrationForm").addEventListener("submit", function (event) {
    let errorMessage = document.getElementById("passwordMismatchError");
    if (password.value !== repeatPassword.value) {
        event.preventDefault()
        errorMessage.style.display = "block";
    } else {
        errorMessage.style.display = "none";
    }
});