const searchInput = document.getElementById("searchInput");
const searchBtn = document.getElementById("searchBtn");
const categoryBtns = document.querySelectorAll(".category-bar span");
const products = document.querySelectorAll(".product-card");

// Home logo
function goHome() {
  window.location.href = "homepage.html";
}

// Search function
function filterProducts() {
  const keyword = searchInput.value.toLowerCase();

  products.forEach(product => {
    const name = product.dataset.name.toLowerCase();
    const category = product.dataset.category.toLowerCase();

    product.style.display =
      name.includes(keyword) || category.includes(keyword)
        ? "block"
        : "none";
  });
}

searchBtn.addEventListener("click", filterProducts);
searchInput.addEventListener("keyup", filterProducts);

// Category click filter
categoryBtns.forEach(btn => {
  btn.addEventListener("click", () => {
    const selected = btn.dataset.category;

    products.forEach(product => {
      product.style.display =
        selected === "all" || product.dataset.category === selected
          ? "block"
          : "none";
    });
  });
});

document.getElementById("shopNowBtn").addEventListener("click", () => {
  document.getElementById("products").scrollIntoView({
    behavior: "smooth"
  });
});


// LOGIN / PROFILE TOGGLE
document.addEventListener("DOMContentLoaded", () => {
  const loginBtn = document.getElementById("loginBtn");
  const profileBtn = document.getElementById("profileBtn");

  const isLoggedIn = localStorage.getItem("isLoggedIn");

  if (isLoggedIn === "true") {
    loginBtn.style.display = "none";
    profileBtn.style.display = "inline-block";
  } else {
    loginBtn.style.display = "inline-block";
    profileBtn.style.display = "none";
  }
});

document.addEventListener("DOMContentLoaded", () => {
  const productGrid = document.getElementById("products");

  const items = JSON.parse(localStorage.getItem("items")) || [];

  items.forEach(item => {
    const card = document.createElement("div");
    card.className = "product-card";
    card.dataset.category = item.category;
    card.dataset.name = item.name;

    card.innerHTML = `
      <img src="${item.image}" alt="${item.name}">
      <h4>${item.name}</h4>
      <p>RM ${item.price}</p>
      <small>${item.size}</small>
    `;

    productGrid.appendChild(card);
  });
});
