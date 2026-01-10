const payBtn = document.getElementById("payBtn");
const successMsg = document.getElementById("successMsg");

payBtn.addEventListener("click", () => {
  successMsg.style.display = "block";

  // simulate payment processing
  setTimeout(() => {
    window.location.href = "homepage.html";
  }, 3000);
});
