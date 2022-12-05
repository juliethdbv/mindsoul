const password = document.getElementById("password").value; 

const confirmPassword = document.getElementById("confirmPassword").value;

const button = document.getElementById("buttonForm");

button.addEventListener("click", (e)=>{
    e.preventDefault();
    console.log(password + confirmPassword)
})