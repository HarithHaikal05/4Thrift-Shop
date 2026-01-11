document.addEventListener("DOMContentLoaded", () => {
    loadCartFromServer();
});

function loadCartFromServer() {
    // Call our new "Reader" Servlet
    fetch('../../GetCartServlet')
    .then(res => res.json())
    .then(cartItems => {
        renderCart(cartItems);
    })
    .catch(err => console.error("Error loading cart:", err));
}

function renderCart(items) {
    const container = document.querySelector(".cart");
    // Remove any old/dummy items first
    const existingItems = document.querySelectorAll(".cart-item");
    existingItems.forEach(el => el.remove());

    let total = 0;
    
    // We insert items after the H1 title
    const header = container.querySelector("h1");
    
    if (items.length === 0) {
        const emptyMsg = document.createElement("p");
        emptyMsg.innerText = "Your cart is empty.";
        emptyMsg.style.textAlign = "center";
        emptyMsg.style.color = "#888";
        header.after(emptyMsg);
        document.getElementById("cartTotal").innerText = "0.00";
        return;
    }

    // Loop through JSON from Java and create HTML
    items.forEach(item => {
        total += item.price;
        const imagePath = item.image ? `../../assets/products/${item.image}` : "https://via.placeholder.com/120x150";

        const itemHTML = document.createElement("div");
        itemHTML.classList.add("cart-item");
        itemHTML.innerHTML = `
            <img src="${imagePath}" width="120" onerror="this.src='https://via.placeholder.com/120'">
            <div class="item-info">
                <h3>${item.name}</h3>
                <p>RM <span class="price">${item.price.toFixed(2)}</span></p>
            </div>
            <div class="item-total">
                RM ${item.price.toFixed(2)}
            </div>
        `;
        header.after(itemHTML);
    });

    document.getElementById("cartTotal").innerText = total.toFixed(2);
}