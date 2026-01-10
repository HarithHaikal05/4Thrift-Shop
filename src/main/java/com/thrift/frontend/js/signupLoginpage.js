// 1. Function to switch between Login and Signup forms
function switchForm(type) {
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');
    const btns = document.querySelectorAll('.toggle-btn');

    if (type === 'login') {
        // Show Login, Hide Signup
        loginForm.classList.add('active');
        signupForm.classList.remove('active');
        
        // Update the Underline on the buttons
        btns[0].classList.add('active');
        btns[1].classList.remove('active');
    } else {
        // Show Signup, Hide Login
        loginForm.classList.remove('active');
        signupForm.classList.add('active');
        
        // Update the Underline
        btns[0].classList.remove('active');
        btns[1].classList.add('active');
    }
}

// 2. Function that runs automatically when page loads
document.addEventListener("DOMContentLoaded", function() {
    
    // Check URL for error messages (e.g. ?error=Invalid Password)
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const msg = urlParams.get('msg');
    const box = document.getElementById('error-box');

    if (error) {
        box.style.display = 'block';
        box.innerText = error;
        
        // Bonus: If the error is about "Username Taken", switch to Signup tab automatically
        if (error.includes("Taken") || error.includes("Register")) {
            switchForm('signup');
        }
    } else if (msg) {
        // Success messages (Green)
        box.style.display = 'block';
        box.style.color = '#00ff00';
        box.style.borderColor = '#00ff00';
        box.style.backgroundColor = 'rgba(0, 255, 0, 0.05)';
        box.innerText = msg;
    }
});

localStorage.setItem("isLoggedIn", "true");
window.location.href = "homepage.html";
