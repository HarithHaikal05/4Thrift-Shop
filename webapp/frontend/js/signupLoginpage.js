/* ===============================
   LOGIN / SIGNUP TOGGLE
================================ */
function switchForm(type) {
  const loginForm = document.getElementById("login-form");
  const signupForm = document.getElementById("signup-form");
  const btns = document.querySelectorAll(".toggle-btn");

  if (type === "login") {
    loginForm.classList.add("active");
    signupForm.classList.remove("active");

    btns[0].classList.add("active");
    btns[1].classList.remove("active");
  } else {
    loginForm.classList.remove("active");
    signupForm.classList.add("active");

    btns[0].classList.remove("active");
    btns[1].classList.add("active");
  }
}

/* ===============================
   PAGE LOAD LOGIC
================================ */
document.addEventListener("DOMContentLoaded", () => {

  /* ----- URL MESSAGE HANDLING ----- */
  const urlParams = new URLSearchParams(window.location.search);
  const error = urlParams.get("error");
  const msg = urlParams.get("msg");
  const box = document.getElementById("error-box");

  if (error) {
    box.style.display = "block";
    box.style.color = "#ff4444";
    
    if (error === "invalid") box.innerText = "Invalid Username or Password";
    else if (error === "server") box.innerText = "Server Error. Try again.";
    else box.innerText = error;

    if (error.includes("Taken") || error.includes("Register")) {
      switchForm("signup");
    }
  } else if (msg) {
    box.style.display = "block";
    box.style.color = "#00ff00"; // Green
    box.style.borderColor = "#00ff00";
    box.style.backgroundColor = "rgba(0,255,0,0.05)";
    
    if (msg === "registered") box.innerText = "Account created! Please Login.";
    else box.innerText = msg;
  }

  // NOTE: We removed the "loginForm.addEventListener" block here.
  // The form submission is now fully handled by the HTML <form> action 
  // submitting to the Java Servlet.
});