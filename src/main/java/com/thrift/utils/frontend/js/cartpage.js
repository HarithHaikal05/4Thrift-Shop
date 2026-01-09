const minusBtn = document.querySelector(".minus");
const plusBtn = document.querySelector(".plus");
const quantityEl = document.querySelector(".quantity");
const priceEl = document.querySelector(".price");
const itemTotalEl = document.querySelector(".itemTotal");
const cartTotalEl = document.getElementById("cartTotal");

let quantity = 1;
const price = parseInt(priceEl.textContent);

function updateCart() {
  const total = quantity * price;
  quantityEl.textContent = quantity;
  itemTotalEl.textContent = total;
  cartTotalEl.textContent = total;
}

plusBtn.addEventListener("click", () => {
  quantity++;
  updateCart();
});

minusBtn.addEventListener("click", () => {
  if (quantity > 1) {
    quantity--;
    updateCart();
  }
});
