const itemNameEl = document.getElementById("itemName");
const itemPriceEl = document.getElementById("itemPrice");
const totalPriceEl = document.getElementById("totalPrice");

const payBtn = document.getElementById("payBtn");
const successMsg = document.getElementById("successMsg");

// ✅ LOAD FROM localStorage
const cartItem = JSON.parse(localStorage.getItem("cartItem"));

if (cartItem) {
  itemNameEl.textContent = `${cartItem.name} (x${cartItem.quantity})`;
  itemPriceEl.textContent = `RM ${cartItem.total}`;
  totalPriceEl.textContent = cartItem.total;
}

payBtn.addEventListener("click", () => {
  successMsg.style.display = "block";

  // Optional: clear cart after payment
  localStorage.removeItem("cartItem");

  setTimeout(() => {
    window.location.href = "homepage.html";
  }, 3000);
});
