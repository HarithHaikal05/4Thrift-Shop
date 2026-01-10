const sizeFilter = document.getElementById("sizeFilter");
const typeFilter = document.getElementById("typeFilter");
const products = document.querySelectorAll(".product-card");

function filterProducts() {
  const size = sizeFilter.value;
  const type = typeFilter.value;

  products.forEach(product => {
    const matchSize = size === "all" || product.dataset.size === size;
    const matchType = type === "all" || product.dataset.type === type;

    product.style.display = matchSize && matchType ? "block" : "none";
  });
}

sizeFilter.addEventListener("change", filterProducts);
typeFilter.addEventListener("change", filterProducts);
